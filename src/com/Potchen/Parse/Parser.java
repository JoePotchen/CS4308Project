package com.Potchen.Parse;

import java.io.FileNotFoundException;

import com.Potchen.Lexer.LexicalAnalyzer;
import com.Potchen.Lexer.LexicalException;
import com.Potchen.Interpreter.Program;
import com.Potchen.Lexer.Token;
import com.Potchen.Lexer.TokenType;

/**
 * Created by joepo on 3/30/2018.
 */
public class Parser {
    private LexicalAnalyzer lex;

    public Parser (String fileName) throws FileNotFoundException, LexicalException {
        lex = new LexicalAnalyzer(fileName);
    }


    /**
     * This assumes that the beginning of the lua file is syntactically correct
     * it first looks for the keyword function, then the function name
     * then the 2 parens
     * It then creates a block that holds the entire lua program
     * @return Program object that contains the entire parse tree for the lua file
     * @throws ParserException
     * implements <program> -> function id () <block> end
     */
    public Program parse() throws ParserException{
        Token tok = getNextToken();
        match(tok, TokenType.FUNCTION_TOK);
        ArithmeticExpression.Id functionName = getId();
        tok = getNextToken();
        match(tok, TokenType.PARENL_TOK);
        tok = getNextToken();
        match(tok, TokenType.PARENR_Tok);
        Block blk = getBlock();
        tok = getNextToken();
        match(tok, TokenType.END_TOK);
        tok = getNextToken();
        if(tok.getTokenType() != TokenType.EOS_TOK)
            throw new ParserException("Garbage at end of file");
        return new Program(blk);
    }

    /**
     * This is called everytime the parser needs a new block
     * Because <program> -> function id () <block> end
     * This is essentially the "top" function, it creates a new Statement, goes through the
     * tokens to add that statement to the block.
     * However, statements can also have <blocks> in them, so this is recursive.
     * @return Block object
     * @throws ParserException
     */
    private Block getBlock() throws ParserException{
        Block blk = new Block();
        Token tok = getLookaheadToken();
        while(isValidStartOfStatement(tok)){
            Statement stmt = getStatement();
            blk.add(stmt);
            tok = getLookaheadToken();
        }
        return blk;
    }

    /**
     * Determines what type of Statement the token is then calls the appropriate function to retrieve it.
     * @return Statement which can contain Blocks
     * @throws ParserException
     * implements <statement> → <if_statement> | <assignment_statement> | <while_statement> | <print_statement> | <repeat_statement>
     */
    private Statement getStatement() throws ParserException {
        Statement stmt;
        Token tok = getLookaheadToken();
        if(tok.getTokenType() == TokenType.IF_TOK)
            stmt = getIfStatement();
        else if(tok.getTokenType() == TokenType.WHILE_TOK)
            stmt = getWhileStatement();
        else if(tok.getTokenType() == TokenType.PRINT_TOK)
            stmt = getPrintStatement();
        else if(tok.getTokenType() == TokenType.REPEAT_TOK)
            stmt = getRepeatStatement();
        else if(tok.getTokenType() == TokenType.ID_TOK)
            stmt = getAssignmentStatement();
        else
            throw new ParserException("Invalid Statement at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
        return stmt;
    }

    /**
     * First retrieves the ID it's assigning to
     * It then makes sure the next token is an ASSIGN_TOK
     * Then it calls getArithmeticExpression() to determine what the ID is assigning to
     * @return Statement.AssignmentStatement containing a Id Variable and ArithmeticExpression
     * @throws ParserException
     * implements <assignment_statement> -> id <assignment_operator> <arithmetic_expression>
     */
    private Statement getAssignmentStatement() throws ParserException{
        ArithmeticExpression.Id var = getId();
        Token tok = getNextToken();
        match(tok, TokenType.ASSIGN_TOK);
        ArithmeticExpression expr = getArithmeticExpression();
        return new Statement.AssignmentStatement(var, expr);

    }

    /**
     * First retrieves the Block that it will be repeating with getBlock()
     * Then it retrieves the BooleanExpression that it will repeat to
     * @return Statement.RepeatStatemeant that contains a Block and a BooleanExpression
     * @throws ParserException
     * implements <repeat_statement> -> repeat <block> until <boolean_expression>
     */
    private Statement getRepeatStatement() throws ParserException{
        Token tok = getNextToken();
        match(tok, TokenType.REPEAT_TOK);
        Block blk = getBlock();
        tok = getNextToken();
        match(tok, TokenType.UNTIL_TOK);
        BooleanExpression expr = getBooleanExpression();
        return new Statement.RepeatStatement(blk, expr);
    }

    /**
     * checks that it is a valid printStatement
     * Creates an ArithmeticExpression that contains the Expression it is printing
     * @return Statement.PrintStatement that contains an Expression
     * @throws ParserException
     * implements <print_statement> → print ( <arithmetic_expression> )
     */
    private Statement getPrintStatement() throws ParserException{
        Token tok = getNextToken();
        match(tok, TokenType.PRINT_TOK);
        tok = getNextToken();
        match(tok, TokenType.PARENL_TOK);
        ArithmeticExpression expr = getArithmeticExpression();
        tok = getNextToken();
        match(tok,TokenType.PARENR_Tok);
        return new Statement.PrintStatement(expr);
    }

    /**
     * Creates an ArithmeticExpression before it determines what type of ArithmeticExpression it is
     * Based on that it calls the appropriate method
     * @return ArithmeticExpression that is either an Id, BinaryExpression or LiteralInteger
     * @throws ParserException
     * implements <arithmetic_expression> → <id> | <literal_integer> | <arithmetic_op> <arithmetic_expression> <arithmetic_expression>
     */
    private ArithmeticExpression getArithmeticExpression() throws ParserException{
        ArithmeticExpression expr;
        Token tok = getLookaheadToken();
        if(tok.getTokenType() == TokenType.ID_TOK)
            expr = getId();
        else if (tok.getTokenType() == TokenType.INT_TOK)
            expr = getInteger();
        else
            expr = getBinaryExpression();
        return expr;
    }

    /**
     * Creates an ArithmeticOperator and calls getArithmeticOperator()
     * Creates 2 ArithmeticExpressions and calls getArithmeticExpression()
     * @return ArithmeticExpression.BinaryExpression that contains an ArithmeticOperator and 2 ArithmeticExpressions
     * @throws ParserException
     * implements the grammar expression <arithmetic_op> <arithmetic_expression> <arithmetic_expression> from <arithmetic_expression>
     */
    private ArithmeticExpression.BinaryExpression getBinaryExpression() throws ParserException{

        ArithmeticExpression.BinaryExpression.ArithmeticOperator op = getArithmeticOperator();
        ArithmeticExpression expr1 = getArithmeticExpression();
        ArithmeticExpression expr2 = getArithmeticExpression();
        return new ArithmeticExpression.BinaryExpression(op, expr1, expr2);
    }

    /**
     * Looks at the current token and determines what operator it is based on the TokenType enum
     * @return ArithmeticExpression.BinaryExpression.ArithmeticOperator Enum
     * @throws ParserException
     */
    private ArithmeticExpression.BinaryExpression.ArithmeticOperator getArithmeticOperator() throws ParserException{
        ArithmeticExpression.BinaryExpression.ArithmeticOperator op;
        Token tok = getNextToken();
        if(tok.getTokenType() == TokenType.ADD_TOK)
            op = ArithmeticExpression.BinaryExpression.ArithmeticOperator.ADD_OP;
        else if(tok.getTokenType() == TokenType.SUB_TOK)
            op = ArithmeticExpression.BinaryExpression.ArithmeticOperator.SUB_OP;
        else if(tok.getTokenType() == TokenType.MULT_TOK)
            op = ArithmeticExpression.BinaryExpression.ArithmeticOperator.MUL_OP;
        else if(tok.getTokenType() == TokenType.DIV_TOK)
            op = ArithmeticExpression.BinaryExpression.ArithmeticOperator.DIV_OP;
        else
            throw new ParserException("Arithmetic operator expected at row" + tok.getRowNumber() + " and column " + tok.getColumnNumber());
        return op;

    }

    /**
     * Pulls the Integer from the Token lexeme
     * @return ArithmeticExpression.LiteralInteger that contains an Integer
     * @throws ParserException
     */
    private ArithmeticExpression.LiteralInteger getInteger() throws ParserException{

        Token tok = getNextToken();
        if (tok.getTokenType() != TokenType.INT_TOK)
            throw new ParserException("Integer expected at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
        int value = Integer.parseInt(tok.getLexeme());
        return new ArithmeticExpression.LiteralInteger(value);

    }

    /**
     * Determines if it is a valid While statement
     * Retrieves a BooleanExpression with getBooleanExpression()
     * Retreieves a Block with getBlock()
     * @return Statement.WhileStatement that contains a BooleanExpression and a Block
     * @throws ParserException
     */
    private Statement getWhileStatement() throws ParserException{
        Token tok = getNextToken();
        match(tok, TokenType.WHILE_TOK);
        BooleanExpression expr = getBooleanExpression();
        tok = getNextToken();
        match(tok, TokenType.DO_TOK);
        Block blk = getBlock();
        tok = getNextToken();
        match(tok, TokenType.END_TOK);
        return new Statement.WhileStatement(expr, blk);

    }

    /**
     * Gets a RelationshipOperator with getRelationalOperator()
     * Get 2 Arithmetic Expressions with getArithmeticExpression()
     * @return BooleanExpression with the Operator and 2 ArithmeticExpressions
     * @throws ParserException
     */
    private BooleanExpression getBooleanExpression() throws ParserException{

        BooleanExpression.RelationshipOperator op = getRelationalOperator();
        ArithmeticExpression expr1 = getArithmeticExpression();
        ArithmeticExpression expr2 = getArithmeticExpression();


        return new BooleanExpression(op, expr1, expr2);

    }

    /**
     * Looks at the current token and determines what operator it is based on the TokenType enum
     * @return BooleanExpression.RelationshipOperator Enum
     * @throws ParserException
     */
    private BooleanExpression.RelationshipOperator getRelationalOperator() throws ParserException{
        BooleanExpression.RelationshipOperator op;
        Token tok = getNextToken();
        if(tok.getTokenType() == TokenType.EQ_TOK)
            op = BooleanExpression.RelationshipOperator.EQ_OP;
        else if(tok.getTokenType() == TokenType.NOTEQ_TOK)
            op = BooleanExpression.RelationshipOperator.NOTEQ_OP;
        else if(tok.getTokenType() == TokenType.GREATER_TOK)
            op = BooleanExpression.RelationshipOperator.GREATER_OP;
        else if(tok.getTokenType() == TokenType.GREATEREQ_TOK)
            op = BooleanExpression.RelationshipOperator.GREATEREQ_OP;
        else if(tok.getTokenType() == TokenType.LESS_TOK)
            op = BooleanExpression.RelationshipOperator.LESS_OP;
        else if(tok.getTokenType() == TokenType.LESSEQ_TOK)
            op = BooleanExpression.RelationshipOperator.LESSEQ_OP;
        else
            throw new ParserException("Relational operator expected at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
        return op;
    }

    /**
     * Determines if it's a valid IfStatement
     * Retrieves a BooleanExpression with getBooleanExpression()
     * Retrieves 2 Blocks using getBlock()
     * @return Statement.IfStatement that contains a BooleanExpression and 2 Blocks
     * @throws ParserException
     */
    private Statement getIfStatement() throws ParserException{
        Token tok = getNextToken();
        match(tok, TokenType.IF_TOK);
        BooleanExpression expr = getBooleanExpression();
        tok = getNextToken();
        match(tok, TokenType.THEN_TOK);
        Block blk1 = getBlock();
        tok = getNextToken();
        match(tok, TokenType.ELSE_TOK);
        Block blk2 = getBlock();
        tok = getNextToken();
        match(tok, TokenType.END_TOK);
        return new Statement.IfStatement(expr, blk1, blk2);

    }

    /**
     * It looks at the start of the Statement to make sure
     * the keyword is valid
     * @param tok
     * @return Boolean
     */
    private boolean isValidStartOfStatement(Token tok) {
        assert(tok != null);
        return tok.getTokenType() == TokenType.ID_TOK ||
                tok.getTokenType() == TokenType.IF_TOK ||
                tok.getTokenType() == TokenType.WHILE_TOK ||
                tok.getTokenType() == TokenType.PRINT_TOK ||
                tok.getTokenType() == TokenType.REPEAT_TOK;
    }


    /**
     *
     * @return ArithmeticExpression.Id that contains the character lexeme stored in the current Token
     * @throws ParserException
     */
    private ArithmeticExpression.Id getId() throws ParserException{

        Token tok = getNextToken();
        if (tok.getTokenType() != TokenType.ID_TOK)
            throw new ParserException("Identifier expected at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
        return new ArithmeticExpression.Id(tok.getLexeme().charAt(0));
    }


    /**
     * Makes sure that the current token matches the appropriate token
     * @param tok
     * @param tokenType
     * @throws ParserException
     */
    private void match(Token tok, TokenType tokenType) throws ParserException{
        assert (tok != null);
        assert (tokenType != null);
        if(tok.getTokenType() != tokenType)
            throw new ParserException(tokenType + " expected at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
    }

    /**
     * Used to look at the next Token without deleting the token using lex.getLookaheadToken()
     * @return Token
     * @throws ParserException
     */
    private Token getLookaheadToken() throws ParserException{

        Token tok;
        try{
            tok = lex.getLookaheadToken();
        }catch (LexicalException e){
            throw new ParserException("No more tokens");
        }
        return tok;
    }

    /**
     * Looks at the current token using lex.getNextToken() which then deletes the token from the Token array
     * @return Token
     * @throws ParserException
     */
    private Token getNextToken() throws ParserException{
        Token tok = null;
        try{
            tok = lex.getNextToken();
        }catch (LexicalException e){
            throw new ParserException("No more tokens");
        }
        return tok;
    }

}

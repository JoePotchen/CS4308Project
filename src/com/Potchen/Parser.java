package com.Potchen;

import java.io.FileNotFoundException;

/**
 * Created by joepo on 3/30/2018.
 */
public class Parser {
    private LexicalAnalyzer lex;

    public Parser (String fileName) throws FileNotFoundException, LexicalException{
        lex = new LexicalAnalyzer(fileName);
    }


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

    private Statement getAssignmentStatement() throws ParserException{
        ArithmeticExpression.Id var = getId();
        Token tok = getNextToken();
        match(tok, TokenType.ASSIGN_TOK);
        ArithmeticExpression expr = getArithmeticExpression();
        return new Statement.AssignmentStatement(var, expr);

    }

    private Statement getRepeatStatement() throws ParserException{
        Token tok = getNextToken();
        match(tok, TokenType.REPEAT_TOK);
        Block blk = getBlock();
        tok = getNextToken();
        match(tok, TokenType.UNTIL_TOK);
        BooleanExpression expr = getBooleanExpression();
        return new Statement.RepeatStatement(blk, expr);
    }

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

    private ArithmeticExpression getArithmeticExpression() throws ParserException{//todo doesn't do math only assigns
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

    private ArithmeticExpression.BinaryExpression getBinaryExpression() throws ParserException{

        ArithmeticExpression.BinaryExpression.ArithmeticOperator op = getArithmeticOperator();
        ArithmeticExpression expr1 = getArithmeticExpression();
        ArithmeticExpression expr2 = getArithmeticExpression();
        return new ArithmeticExpression.BinaryExpression(op, expr1, expr2);
    }

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

    private ArithmeticExpression.LiteralInteger getInteger() throws ParserException{

        Token tok = getNextToken();
        if (tok.getTokenType() != TokenType.INT_TOK)
            throw new ParserException("Integer expected at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
        int value = Integer.parseInt(tok.getLexeme());
        return new ArithmeticExpression.LiteralInteger(value);

    }

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

    private BooleanExpression getBooleanExpression() throws ParserException{


        ArithmeticExpression expr1 = getArithmeticExpression();
        BooleanExpression.RelationshipOperator op = getRelationalOperator();
        ArithmeticExpression expr2 = getArithmeticExpression();
        return new BooleanExpression(op, expr1, expr2);

    }


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

    private boolean isValidStartOfStatement(Token tok) {
        assert(tok != null);
        return tok.getTokenType() == TokenType.ID_TOK ||
                tok.getTokenType() == TokenType.IF_TOK ||
                tok.getTokenType() == TokenType.WHILE_TOK ||
                tok.getTokenType() == TokenType.PRINT_TOK ||
                tok.getTokenType() == TokenType.REPEAT_TOK;
    }

    private Token getLookaheadToken() throws ParserException{

        Token tok;
        try{
            tok = lex.getLookaheadToken();
        }catch (LexicalException e){
            throw new ParserException("No more tokens");
        }
        return tok;
    }

    private ArithmeticExpression.Id getId() throws ParserException{

        Token tok = getNextToken();
        if (tok.getTokenType() != TokenType.ID_TOK)
            throw new ParserException("Identifier expected at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
        return new ArithmeticExpression.Id(tok.getLexeme().charAt(0));
    }


    private void match(Token tok, TokenType tokenType) throws ParserException{
        assert (tok != null);
        assert (tokenType != null);
        if(tok.getTokenType() != tokenType)
            throw new ParserException(tokenType + " expected at row " + tok.getRowNumber() + " and column " + tok.getColumnNumber());
    }

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

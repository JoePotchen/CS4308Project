package com.Potchen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/*



 */



/**
 * Created by Joe on 2/22/2018.
 */
public class LexicalAnalyzer {
    private List<Token> tokens;

    public LexicalAnalyzer(String fileName) throws FileNotFoundException, LexicalException{
        assert (fileName != null);
        tokens = new ArrayList<Token>();
        Scanner sourceCode = new Scanner(new File(fileName));
        int lineNumber = 0;
        while(sourceCode.hasNext()){
            String line = sourceCode.nextLine();
            processLine(line, lineNumber);
            lineNumber++;
        }
        tokens.add(new Token(lineNumber, 1, "EOS", TokenType.EOS_TOK));
        sourceCode.close();

    }


    /**
     * Prints a formatted version of the tokens List.
     */
    public void printTokens(){
        System.out.printf("%-20s %-15s %3s\n", "LEXEME", "TOKEN TYPE", "ROW NUMBER");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.printf("%-20s %-15s %3s\n", tokens.get(i).getLexeme(), tokens.get(i).getTokenType(), tokens.get(i).getRowNumber());
        }
    }

    public ArrayList<Token> returnTokens(){
        return (ArrayList<Token>) tokens;
    }

    public Token getToken(int index){
        return tokens.get(index);

    }


    /**
     * This goes through a line and finds a lexeme using the whitespace that surrounds it.
     * It then calls getTokenType to determine the token of the lexeme before moving onto the next lexeme.
     * @param line
     * @param lineNumber
     * @throws LexicalException
     */
    private void processLine(String line, int lineNumber) throws LexicalException{
        assert (line != null && lineNumber >= 1);
        int index = 0;
        index = skipWhiteSpace(line, index);
        while(index < line.length()){
            String lexeme = getLexeme(line, lineNumber, index);
            TokenType tokType = getTokenType (lexeme, lineNumber, index);
            tokens.add(new Token(lineNumber + 1, index +1, lexeme, tokType));
            index += lexeme.length();
            index = skipWhiteSpace(line, index);
        }
    }

    /**
     * Determines the tokenType of a given lexeme.
     * @param lexeme
     * @param lineNumber
     * @param columnNumber
     * @return
     * @throws LexicalException
     */
    private TokenType getTokenType(String lexeme, int lineNumber, int columnNumber) throws LexicalException{
        assert (lexeme != null && lineNumber >= 1 && columnNumber >= 1);
        TokenType tokType = null;


        /**
         * Determines what the token is based on the lexeme
         */
        switch (lexeme){
            case "=":
                return TokenType.ASSIGN_TOK;
            case "==":
                return TokenType.EQ_TOK;
            case "<":
                return TokenType.LESS_TOK;
            case "<=":
                return TokenType.LESSEQ_TOK;
            case ">":
                return TokenType.GREATER_TOK;
            case ">=":
                return TokenType.GREATEREQ_TOK;
            case "~=":
                return TokenType.NOTEQ_TOK;
            case "+":
                return TokenType.ADD_TOK;
            case "-":
                return TokenType.SUB_TOK;
            case "*":
                return TokenType.MULT_TOK;
            case "/":
                return TokenType.DIV_TOK;
            case "(":
                return TokenType.PARENL_TOK;
            case ")":
                return TokenType.PARENR_Tok;
            default:

                /**
                 * Checks to see if the first character is a letter. If it is it then determines
                 * if it's an ID or a statement token.
                 */
                if (Character.isLetter(lexeme.charAt(0))){
                    if(lexeme.length() == 1){
                        tokType = TokenType.ID_TOK;
                    } else{

                        if(lexeme.equalsIgnoreCase("While")){
                            return TokenType.WHILE_TOK;

                        }else if (lexeme.matches("print")){
                            return TokenType.PRINT_TOK;
                        }else if (lexeme.equalsIgnoreCase("If")){
                            return TokenType.IF_TOK;
                        }else if(lexeme.equalsIgnoreCase("Repeat")){
                            return TokenType.REPEAT_TOK;
                        }else if (lexeme.equalsIgnoreCase("Do")){
                            return TokenType.DO_TOK;
                        }else if (lexeme.equalsIgnoreCase("Until")){
                            return TokenType.UNTIL_TOK;
                        }else if (lexeme.equalsIgnoreCase("End")){
                            return TokenType.END_TOK;
                        }else if (lexeme.equalsIgnoreCase("Then")){
                            return TokenType.THEN_TOK;
                        }else if (lexeme.equalsIgnoreCase("Else")){
                            return TokenType.ELSE_TOK;
                        }else if(lexeme.equalsIgnoreCase("Function")){
                            return TokenType.FUNCTION_TOK;
                        }
                        else {
                            throw new LexicalException("invalid lexeme at row number " + (lineNumber + 1) + "and column" + columnNumber + 1);
                        }

                    }
                }

                /**
                 * Checks to see if the first character is a digit. It then checks the entire lexeme
                 * to make sure there are no non-digit characters.
                 */
                else if(Character.isDigit(lexeme.charAt(0))){
                    if(allDigits(lexeme))
                        return TokenType.INT_TOK;
                    else
                        throw new LexicalException("Invalid lexeme at row number " + (lineNumber + 1) + " and column " + (columnNumber + 1));
                }

                else {
                    throw new LexicalException("Invalid lexeme at row number " + (lineNumber + 1) + " and column " + (columnNumber + 1));
                }
        }

        return tokType;
    }

    /**
     * Checks a lexeme to determine whether it is a valid integer.
     * @param s
     * @return true if all digits
     */
    private boolean allDigits(String s){
        assert (s != null);
        int i = 0;
        while (i < s.length() && Character.isDigit(s.charAt(i)))
            i++;
        return i == s.length();
    }

    /**
     * Finds a lexeme by using whitespace around unbroken characters.
     * If lexeme starts with " it then goes through the string until it
     * finds the next " and assigns the lexeme from the first " to the next.
     * @param line
     * @param lineNumber
     * @param index
     * @return
     */
    private String getLexeme(String line, int lineNumber, int index) {
        assert (line != null && lineNumber >= 1 && index >= 0);
        int i = index;
            while (i < line.length() && !Character.isWhitespace(line.charAt(i)))
                i++;


/*        if (line.substring(index,i).equals("print")){
            while(line.charAt(i) != ')' && i<= line.length())
                i++;
            i++;

        }*/

        return line.substring(index, i);
    }


    /**
     * Skips over unnecessary whitespace.
     * @param line
     * @param index
     * @return
     */
    private int skipWhiteSpace(String line, int index) {
        assert (line != null && index >= 0);
        while (index < line.length() && Character.isWhitespace(line.charAt(index)))
            index++;
        return index;
    }

    public Token getLookaheadToken() throws LexicalException{
        if(tokens.isEmpty()){
            throw new LexicalException("no more tokens");
        }
        return tokens.get(0);
    }

    public Token getNextToken() throws LexicalException{
        if(tokens.isEmpty())
            throw new LexicalException("No more tokens");
        return tokens.remove(0);
    }

}

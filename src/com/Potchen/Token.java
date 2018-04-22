package com.Potchen;

/**
 * Created by Joe on 2/22/2018.
 */
public class Token {

    private int rowNumber;
    private int columnNumber;
    private String lexeme;
    private TokenType tokenType;

    /**
     * @param rowNumber - must be positive
     * @param columnNumber - must be positive
     * @param lexeme - cannot be null nor empty
     * @param tokenType - cannot be null
     * @throws IllegalArgumentException if any precondition is not satisfied
     */


    public Token(int rowNumber, int columnNumber, String lexeme, TokenType tokenType){
        if (rowNumber <= 0)
            throw new IllegalArgumentException("invalid row number argument");
        if (columnNumber <= 0)
            throw new IllegalArgumentException("invalid column number argument");
        if (lexeme == null || lexeme.length() == 0)
            throw new IllegalArgumentException("invalid lexeme argument");
        if (tokenType == null)
            throw new IllegalArgumentException("invalid TokenType argument");
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.lexeme = lexeme;
        this .tokenType = tokenType;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getLexeme() {
        return lexeme;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

}

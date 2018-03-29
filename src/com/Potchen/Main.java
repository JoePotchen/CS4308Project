package com.Potchen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

        String fname = "./LUA/sample.lua";
        LexicalAnalyzer lexicalAnalyzer = null;
        int count = 0;
        int tempCount = 0;
        int tempLineNum = 0;


        try{
            lexicalAnalyzer = new LexicalAnalyzer(fname);


            lexicalAnalyzer.printTokens();



        }catch (FileNotFoundException e){
            System.err.println("FileNotFoundException" + e);
        }catch (LexicalException e){
            System.err.println("LexicalException" + e);
        }



        while(lexicalAnalyzer.getToken(count).getTokenType() != TokenType.EOS_TOK){
            switch(lexicalAnalyzer.getToken(count).getTokenType()){
                case ID_TOK:
                    System.out.println("Assignment Statement Lexemes: " );
                    tempCount = count;
                    while(lexicalAnalyzer.getToken(tempCount).getRowNumber() == lexicalAnalyzer.getToken(count).getRowNumber()){

                        System.out.print(lexicalAnalyzer.getToken(tempCount).getLexeme() + " ");
                        tempCount++;
                    }
                    System.out.print("\n");
                    System.out.println("Assignment Statement Tokens: " );
                    tempCount = count;
                    while(lexicalAnalyzer.getToken(tempCount).getRowNumber() == lexicalAnalyzer.getToken(count).getRowNumber()){

                        System.out.print(lexicalAnalyzer.getToken(tempCount).getTokenType() + " ");
                        tempCount++;
                    }
                    spaceText();

                    count = tempCount;
                    break;
                case WHILE_TOK:
                    do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count).getTokenType() != TokenType.END_TOK);
                    System.out.print("is a while statement");
                    spaceText();
                    break;
                case IF_TOK:
                    do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count).getTokenType() != TokenType.END_TOK);
                    System.out.print("is an if statement");
                    spaceText();
                    break;
                case REPEAT_TOK:
                    do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count).getTokenType() != TokenType.EQ_TOK);
                    System.out.print(lexicalAnalyzer.getToken(count).getTokenType() + " ");
                    System.out.print("is a repeat statement");
                    count++;
                    spaceText();
                    break;
                case PRINT_TOK:
                    do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count).getTokenType() != TokenType.PARENR_Tok);
                    System.out.print("is a print statement");
                    count++;
                    spaceText();
                    break;
            }
        }



    }

    private static void spaceText() {
        for (int i = 0; i < 3; i++) {
            System.out.print("\n");
        }
    }
}



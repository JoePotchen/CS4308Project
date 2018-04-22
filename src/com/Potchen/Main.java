package com.Potchen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

        String fname = "./LUA/sample.lua";
        LexicalAnalyzer lexicalAnalyzer = null;





        try{
            Parser p = new Parser(fname);
            Program program = p.parse();
            program.execute();
        }catch (FileNotFoundException e){
            System.err.println("FileNotFoundException" + e);
            e.printStackTrace();
        }catch (LexicalException e){
            System.err.println("LexicalException" + e);
            e.printStackTrace();
        }catch (ParserException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }catch (InterpreterException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        //parser(lexicalAnalyzer, count);


    }

    private static void parser(LexicalAnalyzer lexicalAnalyzer, int count) {
        int tempCount;
        while(lexicalAnalyzer.getToken(count).getTokenType() != TokenType.EOS_TOK){
            switch(lexicalAnalyzer.getToken(count).getTokenType()){

                case ID_TOK:
                    tempCount = count;
                    while(lexicalAnalyzer.getToken(tempCount).getRowNumber() == lexicalAnalyzer.getToken(count).getRowNumber()){

                        System.out.print(lexicalAnalyzer.getToken(tempCount).getTokenType() +"(" + lexicalAnalyzer.getToken(tempCount).getLexeme() + ")" + " ");
                        tempCount++;
                    }
                    System.out.print("is an assignment statement");
                    spaceText();

                    count = tempCount;
                    break;

                case WHILE_TOK:
                    do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() +"(" + lexicalAnalyzer.getToken(count).getLexeme() + ")" + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count - 1).getTokenType() != TokenType.END_TOK);
                    System.out.print("is a while statement");
                    spaceText();
                    break;

                case IF_TOK:
                    do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() +"(" + lexicalAnalyzer.getToken(count).getLexeme() + ")" + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count -1).getTokenType() != TokenType.END_TOK);
                    System.out.print("is an if statement");
                    spaceText();
                    break;

                case REPEAT_TOK:
                    do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() +"(" + lexicalAnalyzer.getToken(count).getLexeme() + ")" + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count - 1).getTokenType() != TokenType.EQ_TOK);
                    System.out.print(lexicalAnalyzer.getToken(count).getTokenType() + " ");
                    System.out.print("is a repeat statement");
                    count++;
                    spaceText();
                    break;

                case PRINT_TOK:
                    /*do{
                        System.out.print(lexicalAnalyzer.getToken(count).getTokenType() + " ");
                        count++;
                    }while(lexicalAnalyzer.getToken(count - 1).getTokenType() != TokenType.PARENR_Tok);*/
                    System.out.print(lexicalAnalyzer.getToken(count).getTokenType() +"(" + lexicalAnalyzer.getToken(count).getLexeme() + ")" + " ");
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



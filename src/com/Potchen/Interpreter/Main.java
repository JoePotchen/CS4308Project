package com.Potchen.Interpreter;

import java.io.FileNotFoundException;

import com.Potchen.Lexer.LexicalAnalyzer;
import com.Potchen.Lexer.LexicalException;
import com.Potchen.Lexer.TokenType;
import com.Potchen.Parse.Parser;
import com.Potchen.Parse.ParserException;


public class Main {
    public static void main(String[] args) {

        String fname = "./LUA/sample.lua";
        LexicalAnalyzer lexicalAnalyzer = null;


        try {
            Parser p = new Parser(fname);
            Program program = p.parse();
            program.execute();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException" + e);
            e.printStackTrace();
        } catch (LexicalException e) {
            System.err.println("LexicalException" + e);
            e.printStackTrace();
        } catch (ParserException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (InterpreterException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        //parser(lexicalAnalyzer, count);


    }
}
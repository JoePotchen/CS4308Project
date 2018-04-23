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

        try {
            Parser p = new Parser(fname);
            Program program = p.parse();
            program.execute();
        } catch (FileNotFoundException | LexicalException | ParserException | InterpreterException e) {
            e.printStackTrace();
        }

    }
}
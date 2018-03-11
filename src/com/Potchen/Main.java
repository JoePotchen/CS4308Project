package com.Potchen;

import java.io.File;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args) {

        String fname = args[0];


        try{
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(fname);


            lexicalAnalyzer.printTokens();



        }catch (FileNotFoundException e){
            System.err.println("FileNotFoundException" + e);
        }catch (LexicalException e){
            System.err.println("LexicalException" + e);
        }




    }
}



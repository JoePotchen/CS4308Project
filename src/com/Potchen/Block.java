package com.Potchen;

import java.util.ArrayList;

/**
 * Created by joepo on 3/30/2018.
 */
public class Block {

    private ArrayList<Statement> stmts = new ArrayList<Statement>();
    private int index;

    public void add(Statement stmt) {
        stmts.add(stmt);
    }

    public Statement getStatement() throws InterpreterException{
        if(stmts.isEmpty())
            throw new InterpreterException("No more tokens");
        return stmts.remove(0);
    }

    boolean empty(){
        if (stmts.size() == 0)
            return true;
        else
            return false;
    }

}

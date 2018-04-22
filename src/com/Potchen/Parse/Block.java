package com.Potchen.Parse;

import java.util.ArrayList;

import com.Potchen.Interpreter.InterpreterException;

/**
 * Used to store the list of Statements, which in turn can store Blocks
 */
public class Block {

    private ArrayList<Statement> stmts = new ArrayList<>();

    public void add(Statement stmt) {
        stmts.add(stmt);
    }

    /**
     *
     * @return Returns a statement from the Array and then removes it from the Array
     * @throws InterpreterException
     */
    public Statement getStatement() throws InterpreterException {
        if(stmts.isEmpty())
            throw new InterpreterException("No more tokens");
        return stmts.remove(0);
    }

    /**
     *
     * @param index
     * @return Returns an indexed Statement from the Array
     * @throws InterpreterException
     */
    public Statement getNonDestructiveStatement(int index) throws InterpreterException{
        if(stmts.isEmpty())
            throw new InterpreterException("No more tokens");
        return stmts.get(index);
    }

    /**
     *
     * @return stmts.size
     */
    public int getListSize(){
        return stmts.size();
    }

    /**
     *
     * @return true if stmts is empty, else false
     */
    public boolean empty(){
        if (stmts.size() == 0)
            return true;
        else
            return false;
    }

}

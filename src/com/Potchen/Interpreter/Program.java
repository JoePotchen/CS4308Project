package com.Potchen.Interpreter;

import com.Potchen.Parse.Block;
import com.Potchen.Parse.Statement;

/**
 * This contains the main Block as well as implements the emulated Memory
 */
public class Program {

    private Block blk;
    private Memory memory = new Memory();

    public Program(Block blk) {
        this.blk = blk;
    }

    /**
     * Until the main Block is empty this iterates through it Statement by Statement.
     * @throws InterpreterException
     */
    public void execute() throws InterpreterException{
        Statement temp;
        while(!blk.empty()){
            temp = blk.getStatement();
            temp.logic(memory);

        }
    }


}

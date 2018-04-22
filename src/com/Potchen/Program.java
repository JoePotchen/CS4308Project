package com.Potchen;

public class Program {

    private Block blk;
    private Memory memory = new Memory();

    public Program(Block blk) {
        this.blk = blk;
    }

    public void execute() throws InterpreterException{
        Statement temp;
        while(!blk.empty()){
            temp = blk.getStatement();
            temp.logic(memory);

        }
    }


}

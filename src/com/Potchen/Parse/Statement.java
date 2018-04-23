package com.Potchen.Parse;


import java.util.ArrayList;

import com.Potchen.Interpreter.InterpreterException;
import com.Potchen.Interpreter.Memory;

/**
 * An interface that contains different Statements
 * Stores <statement> → <if_statement> | <assignment_statement> | <while_statement> | <print_statement> | <repeat_statement>
 */
public interface Statement {

    /**
     * An abstract method that will perform some logic based on the type of Statement
     * @param memory
     * @throws InterpreterException
     */
    void logic(Memory memory) throws InterpreterException;

    /**
     * Holds a variable and an expression
     */
    class AssignmentStatement implements Statement{

        ArithmeticExpression.Id var;
        ArithmeticExpression expr;

        AssignmentStatement(ArithmeticExpression.Id var, ArithmeticExpression expr){
            super();
            this.var = var;
            this.expr = expr;
        }


        /**
         * Creates or updates a variable in memory and sets it equal to the expression
         * @param memory
         */
        @Override
        public void logic(Memory memory) throws InterpreterException{
            if(expr instanceof ArithmeticExpression.Id){
                memory.addVar(var, memory.varEquals(((ArithmeticExpression.Id) expr).key()));//COULD BE VERY WRONG
            }else if(expr instanceof ArithmeticExpression.LiteralInteger){
                memory.addVar(var, (ArithmeticExpression.LiteralInteger)expr);
            }else if(expr instanceof ArithmeticExpression.BinaryExpression){
                memory.addVar(var, expr.value(memory));
            }
        }



    }

    /**
     * Holds a BooleanExpression and 2 Blocks
     */
    class IfStatement implements Statement{

        BooleanExpression expr;
        Block blk1;
        Block blk2;


        public IfStatement(BooleanExpression expr, Block blk1, Block blk2) {
            super();
            this.expr = expr;
            this.blk1 = blk1;
            this.blk2 = blk2;
        }

        /**
         * Executes a block based on the BooleanExpression
         * Blk1 executes if true
         * Blk2 executes if false
         * @param memory
         * @throws InterpreterException
         */
        @Override
        public void logic(Memory memory) throws InterpreterException{
            Statement temp;
            if (expr.value(memory)){
                blockLoop(memory, blk1);
            }else {
                blockLoop(memory, blk2);
            }
        }

        /**
         * Iterates through the Block until it is empty
         * @param memory
         * @param blk
         * @throws InterpreterException
         */
        private void blockLoop(Memory memory, Block blk) throws InterpreterException {
            Statement temp;
            while(!blk.empty()){
                temp = blk.getStatement();
                temp.logic(memory);
            }
        }
    }

    /**
     * Holds an ArithmeticExpression
     */
    class PrintStatement implements Statement{

        private ArithmeticExpression expr;

        public PrintStatement(ArithmeticExpression expr) {
            super();
            this.expr = expr;
        }

        /**
         * Prints an ArithmeticExpression
         * Calls ArithmeticExpression.value() and either retrieves the Variable from memory
         * Or prints it directly if LiteralInteger
         * @param memory
         */
        @Override
        public void logic(Memory memory) throws InterpreterException {
            System.out.println(expr.value(memory).getNum());
        }
    }

    /**
     * Holds a Block and a BooleanExpression
     */
    class RepeatStatement implements Statement{

        Block blk;
        BooleanExpression expr;

        RepeatStatement(Block blk, BooleanExpression expr){
            super();
            this.blk = blk;
            this.expr = expr;
        }

        /**
         * Repeats a forloop that is the size of the block
         * It calls each statement from the forloop non-destructively so that it can call the block repeatedly
         * Once the expression is satisfied it terminates
         * @param memory
         * @throws InterpreterException
         */
        @Override
        public void logic(Memory memory) throws InterpreterException{
            int size = blk.getListSize();

            do{
                for (int i = 0; i < size; i++) {
                    blk.getNonDestructiveStatement(i).logic(memory);
                }
            }while (!expr.value(memory));
        }
    }

    /**
     * Contains a Block and BooleanExpression
     */
    class WhileStatement implements Statement{

        Block blk;
        BooleanExpression expr;

        WhileStatement(BooleanExpression expr, Block blk){
            this.expr = expr;
            this.blk = blk;
        }


        /**
         * Repeats a forloop while the BooleanExpression is true
         * It calls each statement from the forloop non-destructively so that it can call the block repeatedly
         * @param memory
         * @throws InterpreterException
         */
        @Override
        public void logic(Memory memory) throws InterpreterException{
            int size = blk.getListSize();

            while(expr.value(memory)){
                for (int i = 0; i < size; i++) {
                    blk.getNonDestructiveStatement(i).logic(memory);
                }
            }
        }
    }
}

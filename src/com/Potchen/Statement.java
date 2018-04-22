package com.Potchen;


/**
 * Created by joepo on 3/29/2018.
 */
public interface Statement {


    void logic(Memory memory) throws InterpreterException;

    class AssignmentStatement implements Statement{

        ArithmeticExpression.Id var;
        ArithmeticExpression expr;

        AssignmentStatement(ArithmeticExpression.Id var, ArithmeticExpression expr){
            super();
            this.var = var;
            this.expr = expr;
        }


        /**
         * This assigns an ID to a Literal Integer in memory.
         * @param memory
         */
        @Override
        public void logic(Memory memory) {
            if(expr instanceof ArithmeticExpression.Id){
                memory.addVar(var, memory.varEquals(((ArithmeticExpression.Id) expr).key()));//COULD BE VERY WRONG
            }else if(expr instanceof ArithmeticExpression.LiteralInteger){
                memory.addVar(var, (ArithmeticExpression.LiteralInteger)expr);
            }else if(expr instanceof ArithmeticExpression.BinaryExpression){
                memory.addVar(var, ((ArithmeticExpression.BinaryExpression) expr).value(memory));
            }
        }



    }

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

        @Override
        public void logic(Memory memory) throws InterpreterException{
            Statement temp;
            if (expr.value(memory)){
                blockLoop(memory, blk1);
            }else {
                blockLoop(memory, blk2);
            }
        }

        private void blockLoop(Memory memory, Block blk) throws InterpreterException {
            Statement temp;
            while(!blk.empty()){
                temp = blk.getStatement();
                temp.logic(memory);
            }
        }
    }

    public static class PrintStatement implements Statement{

        private ArithmeticExpression expr;

        public PrintStatement(ArithmeticExpression expr) {
            super();
            this.expr = expr;
        }

        @Override
        public void logic(Memory memory) {
            if (expr instanceof ArithmeticExpression.LiteralInteger){
                System.out.println(expr.value(memory).getNum());
            }else if(expr instanceof ArithmeticExpression.Id){
                ArithmeticExpression.Id temp = (ArithmeticExpression.Id)expr;
                System.out.println(memory.varEquals(((ArithmeticExpression.Id) expr).key()).getNum());

            }
        }
    }

    public static class RepeatStatement implements Statement{

        Block blk;
        BooleanExpression expr;

        RepeatStatement(Block blk, BooleanExpression expr){
            super();
            this.blk = blk;
            this.expr = expr;
        }

        @Override
        public void logic(Memory memory) throws InterpreterException{
            do{
                blk.getStatement().logic(memory);
            }while (expr.value(memory));
        }
    }

    public static class WhileStatement implements Statement{

        Block blk;
        BooleanExpression expr;

        WhileStatement(BooleanExpression expr, Block blk){
            this.expr = expr;
            this.blk = blk;
        }

        @Override
        public void logic(Memory memory) throws InterpreterException{
            while(expr.value(memory)){
                blk.getStatement().logic(memory);
            }
        }
    }
}

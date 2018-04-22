package com.Potchen;

/**
 * Contains all expressions Id, Binary, Boolean
 *
 */
public interface ArithmeticExpression {


    LiteralInteger value(Memory memory);



    public static class Id implements ArithmeticExpression{

        private char iDChar;


        public Id(char iDChar){
            this.iDChar = iDChar;
        }

        public Character key(){
            return iDChar;
        }

        @Override
        public LiteralInteger value(Memory memory) {
            return memory.varEquals(key());
        }
    }

    public static class BinaryExpression implements ArithmeticExpression{


        ArithmeticOperator op;
        ArithmeticExpression expr1;
        ArithmeticExpression expr2;

        BinaryExpression (ArithmeticOperator op, ArithmeticExpression expr1, ArithmeticExpression expr2){
            this.op = op;
            this.expr1 = expr1;
            this.expr2 = expr2;
        }

        /**
         * This is a recursive method, it checks each expression,
         * If it is an ID it calls the ID from memory and assigns it,
         * If it is a number it assigns it,
         * If it is an expression it calls itself again
         * @param memory
         * @return
         */
        @Override
        public LiteralInteger value(Memory memory){
            int num1;
            int num2;

            if(expr1 instanceof Id){
                num1 = memory.varEquals(((Id) expr1).key()).getNum();
            }if(expr1 instanceof LiteralInteger){
                num1 = ((LiteralInteger)expr1).getNum();
            }else{
                num1 = value(memory).getNum();
            }

            if(expr2 instanceof Id){
                num2 = memory.varEquals(((Id) expr2).key()).getNum();
            }if(expr2 instanceof LiteralInteger){
                num2 = ((LiteralInteger)expr2).getNum();
            }else{
                num2 = value(memory).getNum();
            }

            switch (op){
                case ADD_OP:
                    return new LiteralInteger(num1 + num2);
                case SUB_OP:
                    return new LiteralInteger(num1 - num2);
                case MUL_OP:
                    return new LiteralInteger(num1 * num2);
                case DIV_OP:
                    return new LiteralInteger(num1 / num2);
            }

            return null;
        }


        public ArithmeticExpression getExpr1() {
            return expr1;
        }

        public ArithmeticExpression getExpr2() {
            return expr2;
        }

        public ArithmeticOperator getOp() {
            return op;
        }

        public enum ArithmeticOperator {
            ADD_OP, SUB_OP, MUL_OP, DIV_OP
        }
    }

    public static class LiteralInteger implements ArithmeticExpression{

        private int num;

        LiteralInteger (int num){
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        @Override
        public LiteralInteger value(Memory memory) {
            return new LiteralInteger(num);
        }
    }
}

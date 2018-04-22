package com.Potchen.Parse;

import com.Potchen.Interpreter.Memory;

/**
 * Contains all expressions Id, Binary, Boolean
 *
 */
public interface ArithmeticExpression {


    LiteralInteger value(Memory memory);


    /**
     * Contains a char
     */
    class Id implements ArithmeticExpression{

        private char iDChar;


        public Id(char iDChar){
            this.iDChar = iDChar;
        }

        public Character key(){
            return iDChar;
        }

        /**
         *
         * @param memory
         * @return The value of the Id from memory
         */
        @Override
        public LiteralInteger value(Memory memory) {
            return memory.varEquals(key());
        }
    }

    /**
     * Contains an ArithmeticOperator as well as 2 ArithmeticExpressions
     */
    class BinaryExpression implements ArithmeticExpression{


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
         * If it is an expression it calls itself again in getNum
         * @param memory
         * @return
         */
        @Override
        public LiteralInteger value(Memory memory){
            int num1;
            int num2;

            num1 = getNum(memory, expr1);

            num2 = getNum(memory, expr2);

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

        /**
         *
         * @param memory
         * @param expr
         * @return An Integer based on the type of ArithmeticExpression
         */
        private int getNum(Memory memory, ArithmeticExpression expr) {
            int num;
            if(expr instanceof Id){
                num = memory.varEquals(((Id) expr).key()).getNum();
            }else if(expr instanceof LiteralInteger){
                num = ((LiteralInteger) expr).getNum();
            }else{
                num = value(memory).getNum();
            }
            return num;
        }

        public enum ArithmeticOperator {
            ADD_OP, SUB_OP, MUL_OP, DIV_OP
        }
    }

    /**
     * Contains an integer
     */
    class LiteralInteger implements ArithmeticExpression{

        private int num;

        LiteralInteger (int num){
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        /**
         * Doesn't use memory since the LiteralInteger is known
         * @param memory
         * @return
         */
        @Override
        public LiteralInteger value(Memory memory) {
            return new LiteralInteger(num);
        }
    }
}

package com.Potchen;

public class BooleanExpression{

    RelationshipOperator op;
    ArithmeticExpression expr1;
    ArithmeticExpression expr2;
    Boolean returnBool;

    BooleanExpression(RelationshipOperator op, ArithmeticExpression expr1, ArithmeticExpression expr2){
        this.op = op;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public boolean value(Memory memory){
        switch (op){
            case EQ_OP:
                if(expr1.value(memory).getNum() == expr2.value(memory).getNum()) {
                    return true;
                }else
                    return false;
            case LESS_OP:
                if(expr1.value(memory).getNum() < expr2.value(memory).getNum()){
                    return true;
                }else
                    return false;
            case NOTEQ_OP:
                if(expr1.value(memory).getNum() != expr2.value(memory).getNum()){
                    return true;
                }else
                    return false;
            case LESSEQ_OP:
                if(expr1.value(memory).getNum() <= expr2.value(memory).getNum()){
                    return true;
                }else
                    return false;
            case GREATER_OP:
                if(expr1.value(memory).getNum() > expr2.value(memory).getNum()) {
                    return true;
                }else
                    return false;
            case GREATEREQ_OP:
                if(expr1.value(memory).getNum() >= expr2.value(memory).getNum()) {
                    return true;
                }else
                    return false;
        }

        return false;
    }

    public enum RelationshipOperator{
        EQ_OP, NOTEQ_OP, GREATER_OP, GREATEREQ_OP, LESS_OP, LESSEQ_OP
    }


}

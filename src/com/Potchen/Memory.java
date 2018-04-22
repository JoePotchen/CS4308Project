package com.Potchen;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joepo on 3/29/2018.
 */
public class Memory {

    private HashMap<Character, ArithmeticExpression.LiteralInteger> varMap = new HashMap<>();

    public Memory(){

    }


    public void addVar(ArithmeticExpression.Id id, ArithmeticExpression.LiteralInteger integer){
        varMap.put(id.key(), integer);
    }

    public void removeVar(ArithmeticExpression.Id id){
        varMap.remove(id);
    }


    public ArithmeticExpression.LiteralInteger varEquals(Character character){
        return varMap.get(character);
    }




}

package com.Potchen.Interpreter;

import java.util.HashMap;

import com.Potchen.Parse.ArithmeticExpression;

/**
 * This is our Memory emulator. It is a Hashmap.
 * The Key is a variable we need to keep in memory, the value is the value for that variable
 */
public class Memory {

    private HashMap<Character, ArithmeticExpression.LiteralInteger> varMap = new HashMap<>();

    /**
     * Used to add a variable to Memory as well as update a variable
     * @param id
     * @param integer
     */
    public void addVar(ArithmeticExpression.Id id, ArithmeticExpression.LiteralInteger integer){
        varMap.put(id.key(), integer);
    }

    /**
     * Used to remove a variable from Memory when it is no longer in scope
     * This is currently not used as we are only dealing with a single scope
     * @param id
     */
    public void removeVar(ArithmeticExpression.Id id){
        varMap.remove(id);
    }

    /**
     * Used to retrieve the value of a variable
     * @param character
     * @return
     */
    public ArithmeticExpression.LiteralInteger varEquals(Character character){
        return varMap.get(character);
    }




}

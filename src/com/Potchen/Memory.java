package com.Potchen;

import java.util.ArrayList;

/**
 * Created by joepo on 3/29/2018.
 */
public class Memory {

    private ArrayList<Token> tokList;
    private int count;

    public Memory(ArrayList<Token> tokList, int count){
        this.tokList = tokList;
        this.count = count;
    }

    public Token getNextToken(int count){
        Token returnTok = tokList.get(count);
        this.count = this.count + count;
        return returnTok;
    }

    public void increaseCount(int increase){
        count = count + increase;
    }


}

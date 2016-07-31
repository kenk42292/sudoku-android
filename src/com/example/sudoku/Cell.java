package com.example.sudoku;

import java.util.ArrayList;

public class Cell {
    public Cell(String stringVal) {
        __confirmedVal = (stringVal.equals("x"))?-1:Integer.parseInt(stringVal);
        __possibleVals = new ArrayList<Integer>();
        if (__confirmedVal == -1) {
            for (int val = 1; val <= 9; val++) {
                __possibleVals.add(val);
            }
        }
        __confirmed = (__confirmedVal == -1)?false:true;
    }
    
    public Cell(int confirmedVal, ArrayList<Integer> possibleVals, boolean confirmed) {
        __confirmedVal = confirmedVal;
        __possibleVals = new ArrayList<Integer>();
        for (int val : possibleVals) {
            __possibleVals.add(val);
        }
        __confirmed = confirmed;
    }
        
    //removes val from set of possibilities. If only one possible value remains, makes it the confirmed value
    void removePossibleVal(int val) {
        if (!__confirmed) {
            if (__possibleVals.indexOf(val) != -1) {
                __possibleVals.remove(__possibleVals.indexOf(val));
            }
            if (__possibleVals.size() == 1) {
                __confirmedVal = __possibleVals.get(0);
                __confirmed = true;
            }
        }
    }
        
        
    int getConfirmedVal() {
        return __confirmedVal;
    }
        
    public String toString() {
        if (__confirmedVal == -1) {
            return " ";
        }
        return ""+__confirmedVal;
    }
    
    public boolean equals(Cell c) {
        if (__confirmedVal == c.__confirmedVal
            && __confirmed == c.__confirmed
                && __possibleVals.equals(c.__possibleVals)) {
            return true;
        }
        return false;
    }
    
    int __confirmedVal;
    ArrayList<Integer> __possibleVals;
    boolean __confirmed;
}

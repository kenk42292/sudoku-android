package com.example.sudoku;

public class SudokuBrain {
    //args should be a string with a number for a given numbers and an 'x' for an empty space
    //*original puzzle should be read row-majorem
    public static Grid main(String[] args) {
        Grid puzzle = new Grid(args[0]);
        puzzle.solve(0);
        return puzzle;
    }
}

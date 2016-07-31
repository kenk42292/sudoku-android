package com.example.sudoku;

import android.annotation.SuppressLint;
import java.util.Arrays;

public class Grid {
    public Grid(String argString) {
        __grid = createGrid(argString);
   }
   
   public Grid(Cell[][] grid) {
       __grid = copyGrid(grid);
   }
       
   //solves(mutates) grid --> also returns if the grid was solvable to begin with
   boolean solve(int depth) {
       if (gridState(__grid) == COMPLETE) {
           return true;
       }
       if (gridState(__grid) == INVALID) {
           return false;
       }
       while(gridState(__grid) == INCOMPLETE) {
           if (gridState(__grid) == INVALID) {
               return false;
           }
           Cell[][] oldGrid = copyGrid(__grid);
           this.rowFilter();
           this.colFilter();
           this.subGridFilter();
           this.spotter();
           // this.associationFilter();
           // this.twinExclusion();
           // this.tripletExclusion();
           // this.quadrupletExclusion();
           
           if (gridState(__grid) == INVALID) return false;
           if (equalGrids(__grid, oldGrid)) {
               boolean foundUnconfirmed = false;
               for (int row = 0; row < 9; row++) {
                 for (int col = 0; col < 9; col++) {
                   if (foundUnconfirmed) {
                     return false;
                   }
                   if (!__grid[row][col].__confirmed) {
                     foundUnconfirmed = true;
                     for (int possibleVal : __grid[row][col].__possibleVals) {
                       Grid newGrid = new Grid(__grid);
                       newGrid.__grid[row][col].__confirmed = true;
                       newGrid.__grid[row][col].__confirmedVal = possibleVal;
                       boolean solved = newGrid.solve(depth +1);
                       if (solved) {
                         __grid = newGrid.__grid;
                         return true;
                       } else {
                         continue;
                       }
                     }
                   }
                 }
               }
           }
       }
       return true;
   }
           
   
   //Filters all confirmed values within a row
   void rowFilter() {
       for (Cell[] row : __grid) {
           for (Cell current : row) {
               if (current.__confirmed) {
                   for (Cell other : row) {
                       other.removePossibleVal(current.getConfirmedVal());
                   }
               }
           }
       }
   }
   
   //Filters all confirmed values within a column
   void colFilter() {
       for (int col = 0; col < 9; col++) {
           for (int row = 0; row < 9; row++) {
               if (__grid[row][col].__confirmed) {
                   for (int indRow = 0; indRow < 9; indRow++) {
                       __grid[indRow][col].removePossibleVal(__grid[row][col].getConfirmedVal());
                   }
               }
            }
        }
    }
    
    //Filters all confirmed values within a subGrid (the 3x3 squares)
    void subGridFilter() {
       for (int subGridStartRow = 0; subGridStartRow < 9; subGridStartRow += 3) {
         for (int subGridStartCol = 0; subGridStartCol < 9; subGridStartCol += 3) {
         
           for (int subRow = subGridStartRow; subRow < subGridStartRow+3; subRow++) {
             for (int subCol = subGridStartCol; subCol < subGridStartCol+3; subCol++) {
             
               if (__grid[subRow][subCol].__confirmed) {
                 for (int indSubRow = subGridStartRow; indSubRow < subGridStartRow+3; indSubRow++) {
                   for (int indSubCol = subGridStartCol; indSubCol < subGridStartCol+3; indSubCol++) {
                     __grid[indSubRow][indSubCol].removePossibleVal(__grid[subRow][subCol].getConfirmedVal());
                   }
                 }
               }
             }
           }
         }
       }
   }
   
   void spotter() {
       int[] rowPresenceCount;
       for (int row = 0; row < 9; row++) {
           rowPresenceCount = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
           for (int col = 0; col < 9; col++) {
               Cell current = __grid[row][col];
               if (current.__confirmed) {
                   rowPresenceCount[current.getConfirmedVal()-1]+=2;
               } else {
                   for (int val : current.__possibleVals) {
                       rowPresenceCount[val-1] += 1;
                   }
               }
           }
           for (int i = 0; i < 9; i++) {
               if (rowPresenceCount[i] == 1) {
                   int singleVal = i+1;
                   for (int col = 0; col < 9; col++) {
                       Cell current = __grid[row][col];
                       if (!current.__confirmed && current.__possibleVals.contains(singleVal)) {
                           current.__confirmedVal = singleVal;
                           current.__confirmed = true;
                       }
                   }
               }
           }
       }

       //counts the number a times a number occurs (in possibilities) in a column
       int[] colPresenceCount;
       for (int col = 0; col < 9; col++) {
           colPresenceCount = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
           for (int row = 0; row < 9; row++) {
               Cell current = __grid[row][col];
               if (current.__confirmed) {
                   colPresenceCount[current.getConfirmedVal()-1]+=2;
               } else {
                   for (int val : current.__possibleVals) {
                       colPresenceCount[val-1] += 1;
                   }
               }
           }
           for (int i = 0; i < 9; i++) {
               if (colPresenceCount[i] == 1) {
                   int singleVal = i+1;
                   for (int row = 0; row < 9; row++) {
                       Cell current = __grid[row][col];
                       if (!current.__confirmed && current.__possibleVals.contains(singleVal)) {
                           current.__confirmedVal = singleVal;
                           current.__confirmed = true;
                       }
                   }
               }
           }
       }
       
       //counts the number a times a number occurs (in possibilities) in a subGrid - (sub)row major
       int[] sgPresenceCount;
       for (int startRow = 0; startRow < 9; startRow += 3) {
           for (int startCol = 0; startCol < 9; startCol += 3) {
           
               sgPresenceCount = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
           
               for (int row = startRow; row < startRow+3; row++) {
                   for (int col = startCol; col < startCol+3; col++) {
                   
                       Cell current = __grid[row][col];
                       if (current.__confirmed) {
                           sgPresenceCount[current.getConfirmedVal()-1]+=2;
                       } else {
                           for (int val : current.__possibleVals) {
                               sgPresenceCount[val-1] += 1;
                           }
                       }
                   }
               }
               for (int i = 0; i < 9; i++) {
                   if (sgPresenceCount[i] == 1) {
                       int singleVal = i+1;
                       for (int row = startRow; row < startRow+3; row++) {
                           for (int col = startCol; col < startCol+3; col++) {
                               Cell current = __grid[row][col];
                               if (!current.__confirmed && current.__possibleVals.contains(singleVal)) {
                                   current.__confirmedVal = singleVal;
                                   current.__confirmed = true;
                               }
                           }
                       }
                   }
               }
           }
       }
   }

   
   
   boolean sameSubGrid(int[] loc1, int[] loc2) {
       if (loc1[0]/3 == loc2[0]/3 && loc1[1]/3 == loc2[1]/3) {
           return true;
       }
       return false;
   }
          
   
   public String toString() {
       String toPrint = "";
       for (Cell[] row : __grid) {
           toPrint += Arrays.toString(row) + "\n";
       }
       return toPrint;
   }

   @SuppressLint("NewApi")
static Cell[][] createGrid(String stringRep) {
       Cell[][] grid = new Cell[9][9];
       String[] stringArr = stringRep.trim().split("");
       stringArr = Arrays.copyOfRange(stringArr, 1, stringArr.length); 
       for (int row = 0; row < 9; row++) {
           for (int col = 0; col < 9; col++) {
               grid[row][col] = new Cell(stringArr[9*row+col]);    //each cell initialized with an x or a number
           }
       }
       return grid;
   }
   
   static Cell[][] copyGrid(Cell[][] grid) {
       Cell[][] gridCopy = new Cell[9][9];
       for (int row = 0; row < 9; row++) {
           for (int col = 0; col < 9; col++) {
               gridCopy[row][col] = new Cell(grid[row][col].__confirmedVal, grid[row][col].__possibleVals, grid[row][col].__confirmed);
           }
       }
       return gridCopy;
   }
   
   static boolean equalGrids(Cell[][] grid1, Cell[][] grid2) {
       for (int row = 0; row < 9; row++) {
           for (int col = 0; col < 9; col++) {
               if (!grid1[row][col].equals(grid2[row][col])) {
                   return false;
               }
           }
       }
       return true;
   }
   
   static int gridState(Cell[][] grid) {
       boolean incomplete = false;
       for (Cell[] row : grid) {
           boolean[] rowPresence = new boolean[9];
           Arrays.fill(rowPresence, false);
           for (Cell current : row) {
               if (current.__confirmed) {
                   if (rowPresence[current.getConfirmedVal()-1]) {
                       return INVALID;
                   }
                   rowPresence[current.getConfirmedVal()-1] = true;
               } else {
                   incomplete = true;
               }
           }
       }
       
       for (int col = 0; col < 9; col++) {
           boolean[] colPresence = new boolean[9];
           Arrays.fill(colPresence, false);
           for (int row = 0; row < 9; row++) {
               Cell current = grid[row][col];
               if (current.__confirmed) {
                   if (colPresence[current.getConfirmedVal()-1]) {
                       return INVALID;
                   }
                   colPresence[current.getConfirmedVal()-1] = true;
               } else {
                   incomplete = true;
               }
           }
       }
       
       
       for (int rowStart = 0; rowStart < 9; rowStart+=3) {
           for (int colStart = 0; colStart < 9; colStart+=3) {
               boolean[] sgPresence = new boolean[9];
               Arrays.fill(sgPresence, false);
               for (int subRow = rowStart; subRow < rowStart+3; subRow++) {
                   for (int subCol = colStart; subCol < colStart+3; subCol++) {
                       Cell current = grid[subRow][subCol];
                       if (current.__confirmed) {
                           if (sgPresence[current.getConfirmedVal()-1]) {
                               return INVALID;
                           }
                           sgPresence[current.getConfirmedVal()-1] = true;
                       } else {
                           incomplete = true;
                       }
                   }
               }
           }
       }
       
       if (incomplete) {
           return INCOMPLETE;
       }
       return COMPLETE;
       
   }
   
   Cell[][] __grid;
   
   static final int COMPLETE = 1;
   static final int INCOMPLETE = 2;
   static final int INVALID = 3;
}

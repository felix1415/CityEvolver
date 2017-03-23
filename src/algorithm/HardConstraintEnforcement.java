/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import cityevolver.Block;
import cityevolver.BlockType;
import java.util.ArrayList;

/**
 *
 * @author alexgray
 */
public class HardConstraintEnforcement
{
    private static Block[][][] gene;
    private static int xLength;
    private static int yLength;
    private static int zLength;
    
    protected HardConstraintEnforcement() 
    {
        gene = null;
    }
    
    public static Block[][][] applyConstraints(int xLength, int yLength, int zLength, Block[][][] gene)
    {
        HardConstraintEnforcement.gene = gene;
        HardConstraintEnforcement.xLength = xLength;
        HardConstraintEnforcement.yLength = yLength;
        HardConstraintEnforcement.zLength = zLength;
        
        ArrayList<Block> possibleDeletions = new ArrayList<>();
        
        //note the constraints are applied to the first 
        //level first, incrementaly up (y is the outer loop)
        for (int y = 0; y < HardConstraintEnforcement.yLength; y++)
        {
            for (int x = 0; x < HardConstraintEnforcement.xLength; x++)
            {
                for (int z = 0; z < HardConstraintEnforcement.zLength; z++)
                {
                    //if not the lowest level of the map
                    if(!isLowestLevel(x, y, z))
                    {
                        //if it doesn't have a block below
                        if(!hasJoinableBlockBelow(x, y, z))
                        { 
                            //if it's connected to 2 or more blocks that have 
                            //a legal base it becomes air
                            if(!isAdjacentlyConnectedToLegalBlocks(x, y, z))
                            {
                                gene[x][y][z] = new Block(x, y, z, BlockType.AIR);
                            }
                        }
                    }
                }
            }
        }
        return gene;
    }
    
    private static boolean isLowestLevel(int x, int y, int z)
    {
        return y == 0;
    }
    
    private static boolean hasJoinableBlockBelow(int x, int y, int z)
    {
        if(y != 0)
        {
            if(HardConstraintEnforcement.gene[x][y - 1][z].isJoinableBlock())
            {
                return true;
            }
        }
        else
        {
            // the ground is legal
            return true;
        }
        return false;
    }
    
    private static boolean isAdjacentlyConnectedToLegalBlocks(int x, int y, int z)
    {
        int numberOfAdjacentLegalBlocks = 0;
        if(x + 1 < HardConstraintEnforcement.xLength)
        {
            if(HardConstraintEnforcement.gene[x + 1][y][z].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x + 1, y, z))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        if(z + 1 < zLength)
        {
            if(HardConstraintEnforcement.gene[x][y][z + 1].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x, y, z + 1))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        else if(x != 0)
        {
            if(HardConstraintEnforcement.gene[x - 1][y][z].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x - 1, y, z))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        else if(z != 0)
        {                        
            if(HardConstraintEnforcement.gene[x][y][z - 1].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x, y, z - 1))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        
        return numberOfAdjacentLegalBlocks >= 2;
    }
//    public boolean adjacentBlockAreRoadBlocks(int x, int y, int z, Block[][][] gene)
//    {
//        if(x + 1 < CityEvolver)
//        {
//            if(gene[i + 1][j][k].isRoad())
//            {
//                numberOfRoadBlocks++;
//            }
//        }
//        if(k + 1 < zLength)
//        {
//            if(this.gene[i][j][k + 1].isRoad())
//            {
//                numberOfRoadBlocks++;
//            }
//        }
//        else if(i != 0)
//        {
//            if(this.gene[i - 1][j][k].isRoad())
//            {
//            numberOfRoadBlocks++;
//            }
//        }
//        else if(k != 0)
//        {                        
//            if(this.gene[i][j][k - 1].isRoad())
//            {
//                numberOfRoadBlocks++;
//            }
//        }
//    }
    
    public static boolean groupingRoadBlocksOnThisBlock(int x, int y, int z)
    {
        return false; 
    }
    
    public static boolean isAllRoadBlocksConnected()
    {
        return false;
    }
}



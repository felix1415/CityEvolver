/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import cityevolver.Block;

/**
 *
 * @author alexgray
 */
public class HardConstraintEnforcement
{
    Block[][][] gene;
    
    protected HardConstraintEnforcement() 
    {
        gene = null;
    }
    
    public Block[][][] applyConstraints(Block[][][] gene)
    {
        return gene;
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



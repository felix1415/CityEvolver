/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityevolver;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author alexgray
 */
public class Utils
{
    
    private static Random random = new Random(1);
    public static float [] concatenateFloatArrays(float [] array1, float [] array2)
    {
        if(array1 == null)
        {
            return array2;
        }
        else if(array2 == null)
        {
            return array1;
        }
        else if(array2 == null && array1 == null)
        {
            return null;
        }
        else
        {
            float [] concat = new float[array1.length + array2.length];
            System.arraycopy(array1, 0, concat, 0, array1.length);
            System.arraycopy(array2, 0, concat, array1.length, array2.length);
            return concat;
        }   
    }
    
    public static BlockType getRandomBlockExcludeImmovable()
    {
        BlockType type = null;
        while(true)
        {
            type = BlockType.values()[random.nextInt(BlockType.values().length)];
            
            if(type != BlockType.IMMOVABLE)
            {
                return type;
            }
        }
    }
    //a list of blocks can be excluded from the types returned
    //a weighted block has a 50 percent chance of being returned
    public static BlockType getRandomBlock(ArrayList<BlockType> blocks, BlockType weighted, float weight)
    {
        if(weighted != null)
        {
            if(random.nextFloat() > weight)
            {
                return weighted;
            }
        }
        BlockType type = blocks.get(random.nextInt(blocks.size()));
        return BlockType.valueOf(type.name());
    }
    
    public static int roundFloat(float number)
    {
        return Math.round(number);
    }
    
    public static BlockType getRandomBlock(ArrayList<BlockType> blocksForSearch)
    {
        return getRandomBlock(blocksForSearch, null, 0);
    }
}

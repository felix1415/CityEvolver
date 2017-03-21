/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityevolver;

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
    
    public static BlockType getRandomBlock()
    {
        return BlockType.values()[random.nextInt(BlockType.values().length)];
    }
}

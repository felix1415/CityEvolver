/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

/**
 *
 * @author alexgray
 */
public class GeneticAlgorithm implements Runnable
{
    private static GeneticAlgorithm instance = null;
    
    public static GeneticAlgorithm getInstance() 
    {
       if(instance == null) 
       {
          instance = new GeneticAlgorithm();
       }
       return instance;
    }
    
    private int generations;
    private int individuals;
    private float mutation;
    
    private int xBound;
    private int yBound;
    private int zBound;
    
    protected GeneticAlgorithm() 
    {
    }

    @Override
    public void run()
    {
        
    }
    
    public void setBounds(int x, int y, int z)
    {
        this.xBound = x;
        this.yBound = y;
        this.zBound = z;
    }
    
    
}

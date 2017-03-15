/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import java.util.ArrayList;

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
    private int populationNumber;
    private Population population;
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
        this.population.run();
    }
    
    public void setBounds(int x, int y, int z)
    {
        this.xBound = x;
        this.yBound = y;
        this.zBound = z;
    }

    public void setGenerations(int generations)
    {
        this.generations = generations;
    }

    public void setPopulation(int populationNum)
    {
        this.populationNumber = populationNum;
    }

    public void setMutation(float mutation)
    {
        this.mutation = mutation;
    }
    
    public synchronized boolean hasSolutionMapArrayChanged(ArrayList<String> old)
    {
        return this.population.compareArrayList(old);
    }
    
    public synchronized ArrayList<String> getSolutionMapArrayList()
    {
        return this.population.getSolutionMapList();
    }

    public void initialise()
    {
        this.population = new Population(xBound, yBound, zBound, populationNumber, generations, mutation);
    }
    
    
    
    
}

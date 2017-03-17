/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import gui.GUIForm;
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
    
    private boolean running;
    
    private int xBound;
    private int yBound;
    private int zBound;
    
    protected GeneticAlgorithm() 
    {
        running = false;
    }

    @Override
    public void run()
    {
        this.running = true;
        this.population.calculateFitness();
        while(this.running)
        {
            this.population.populationStep();
        }
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
        try
        {
            this.population = new Population(xBound, yBound, zBound, populationNumber, generations, mutation);
        }
        catch(Exception e)
        {
            GUIForm.getInstance().log("Set initial map value", true);
        }
    }
    
    public synchronized boolean isInitialised()
    {
        return this.population != null;
    }

    public synchronized void updateGeneratedPopulationList()
    {
        this.population.updateGeneratedPopulationList();
    }

    public synchronized boolean getRunning()
    {
        return running;
    }
    
    public synchronized void setRunning(boolean running)
    {
        this.running = running;
    }

}

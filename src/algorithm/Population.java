/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;


import gui.GUIForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author alexgray
 */
public class Population
{
    public int populationNumber;
    public final int X_LENGTH;
    public final int Y_LENGTH;
    public final int Z_LENGTH;
    public int totalGenerations;
    public double mutationRate;
    
    private final Individual [] population;
    
    private final Random random;
    
    private double meanFitness;
    private int fittest;
    private int fittestIndividual;
    private int currentGeneration;

    public Population(int x, int y, int z, int populationNumber, int generations, float mutationRate)
    {
        if (x <= 0 || y <= 0 || z <= 0 || populationNumber <= 0 || generations <= 0)
        {
            throw new IllegalArgumentException("" + String.valueOf(x) + 
                    String.valueOf(y) + "" + String.valueOf(populationNumber));
        }
        
        this.X_LENGTH = x;
        this.Y_LENGTH = y;
        this.Z_LENGTH = z;
        this.populationNumber = populationNumber;
        this.totalGenerations = generations;
        this.mutationRate = mutationRate;
        
        this.population = new Individual[this.populationNumber];
        this.random = new Random();
        currentGeneration = 0;
        
        for (int i = 0; i < this.populationNumber; i++)
        {
            this.population[i] = new Individual(X_LENGTH, Y_LENGTH, Z_LENGTH, i + 1);
        }
    }
    
    public void populationStep()
    {
        if(currentGeneration > totalGenerations)
        {
            return;
        }
        int parent1 = random.nextInt(populationNumber);
        int parent2 = random.nextInt(populationNumber);

        //crossover
        Individual offspring = this.crossover(parent1, parent2);

        //mutation
        offspring.mutation(mutationRate);

        //calculate fitness
        offspring.calcFitness();

        //replacement
        this.selection(offspring);

        this.calculateFitness();
        
        currentGeneration++;
        
        GUIForm.getInstance().log("Completed " + currentGeneration + " generation");
    }

    public void run()
    {
        this.calculateFitness();
        for (int i = 0; i < totalGenerations; i++)
        {
            this.populationStep();
        }
    }
    
    void selection(Individual offspring)
    {
        int randomMember = random.nextInt(populationNumber);
        if(offspring.getFitness() > population[randomMember].getFitness())
        {
            population[randomMember] = offspring;
        }
    }
    
    Individual crossover(int parent1, int parent2)
    {
        int xCross = random.nextInt(X_LENGTH);
        int yCross = random.nextInt(Y_LENGTH);
        int zCross = random.nextInt(Z_LENGTH);
        int newIndex = this.currentGeneration + this.populationNumber;
        return new Individual(population[parent1], population[parent2], xCross, yCross, zCross, newIndex);
    }
        
    public void calculateFitness()
    {
        this.meanFitness = 0;
        this.fittest = 0;
        for (int i = 0; i < populationNumber; i++)
        {
            this.population[i].calcFitness();
            int fitness = this.population[i].getFitness();
            if(fitness > this.fittest)
            {
                this.fittest = fitness;
                this.fittestIndividual = i;
            }
            this.meanFitness += fitness;
        }
        this.meanFitness = this.meanFitness / populationNumber;
    }
    
    void print()
    {
        System.out.println("Mean Fitness: " + this.meanFitness + " Fittest: " + this.fittest);
    }
    
    public void printPopulation()
    {
        for (int i = 0; i < populationNumber; i++)
        {
            this.population[i].print();
        }
    }

    public Individual getBestIndividual()
    {
        return this.population[fittestIndividual];
    }
    
    public ArrayList<String> getSolutionMapList()
    {
        String[] mapNamesArray = new String[population.length];
        int[] fitnessArray = new int[population.length];
        for (int i = 0; i < population.length; i++)
        {
            mapNamesArray[i] = population[i].getName();
            fitnessArray[i] = population[i].getFitness();
        }
        
        // bubble sort, quick and easy to implement, it's O(n^2), 
        // but it'll only deal with a maximum of 50 elements
        int i;
        boolean flag = true;
        int tempNum;
        String tempString;

        while (flag)
        {
            flag = false;
            for (i = 0; i < population.length - 1; i++)
            {
                if (fitnessArray[i] < fitnessArray[i + 1])
                {
                    tempNum = fitnessArray[i];
                    fitnessArray[i] = fitnessArray[i + 1];
                    fitnessArray[i + 1] = tempNum;

                    tempString = mapNamesArray[i];
                    mapNamesArray[i] = mapNamesArray[i + 1];
                    mapNamesArray[i + 1] = tempString;

                    flag = true;
                }
            }
        }
        return new ArrayList<>(Arrays.asList(mapNamesArray));
    }
    
    public boolean compareArrayList(ArrayList<String> old)
    {
        ArrayList<String> current = getSolutionMapList();
        return current.equals(old);
    }
    
    protected synchronized void updateGeneratedPopulationList()
    {
        GUIForm.getInstance().setGeneratedPopulationListPopulation(this);
    }
    
    

    public Individual getIndividual(int index)
    {
        return this.population[index];
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;


import java.util.Random;

/**
 *
 * @author alexgray
 */
public class Population
{
    public final int POPULATION_NUM = 40;
    public final int X_LENGTH;
    public final int Y_LENGTH;
    public final int Z_LENGTH;
    public final int GENERATIONS = 5000;
    public final double MUTATION_NUM = 0.2;
    
    private Individual [] population;
    
    private final Random random;
    
    private double meanFitness;
    private int fittest;
    private int fittestIndividual;
    private int currentGeneration;

    public Population(int x, int y, int z)
    {
        X_LENGTH = x;
        Y_LENGTH = y;
        Z_LENGTH = z;
                
        this.population = new Individual[POPULATION_NUM];
        this.random = new Random();
        currentGeneration = 0;
        
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i] = new Individual(X_LENGTH, Y_LENGTH, Z_LENGTH, i);
        }
    }
    
    void populationStep()
    {
        if(currentGeneration > GENERATIONS)
        {
            return;
        }
        int parent1 = random.nextInt(POPULATION_NUM);
        int parent2 = random.nextInt(POPULATION_NUM);

        //crossover
        Individual offspring = this.crossover(parent1, parent2);

        //mutation
        offspring.mutation(MUTATION_NUM);

        //calculate fitness
        offspring.calcFitness();

        //replacement
        this.selection(offspring);

        this.calculateFitness();
        
        currentGeneration++;
    }

    public void run()
    {
        this.calculateFitness();
//        this.printPopulation();
//        this.print();
        for (int i = 0; i < GENERATIONS; i++)
        {
            // selection
            int parent1 = random.nextInt(POPULATION_NUM);
            int parent2 = random.nextInt(POPULATION_NUM);
            
            //crossover
            Individual offspring = this.crossover(parent1, parent2);
            
            //mutation
            offspring.mutation(MUTATION_NUM);
            
            //calculate fitness
            offspring.calcFitness();
            
            //replacement
            this.selection(offspring);
            
            this.calculateFitness();
            
//            this.printPopulation();
//            this.print();
        }
    }
    
    void selection(Individual offspring)
    {
        int randomMember = random.nextInt(POPULATION_NUM);
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
        return new Individual(population[parent1], population[parent2], xCross, yCross, zCross);
    }
        
    public void calculateFitness()
    {
        this.meanFitness = 0;
        this.fittest = 0;
        for (int i = 0; i < POPULATION_NUM; i++)
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
        this.meanFitness = this.meanFitness / POPULATION_NUM;
        System.out.println("algorithm.Popultation.calculateFitness() " + this.fittest);
    }
    
    void print()
    {
        System.out.println("Mean Fitness: " + this.meanFitness + " Fittest: " + this.fittest);
    }
    
    public void printPopulation()
    {
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i].print();
        }
    }

    public Individual best()
    {
        return this.population[fittestIndividual];
    }
}

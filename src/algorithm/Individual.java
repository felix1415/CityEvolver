/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import cityevolver.Cube;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author alexgray
 */
public class Individual
{
    
    private Cube[][][] gene;
    private final int xLength;
    private final int yLength;
    private final int zLength;
    private int fitness;
    private Random r;
    private final int index;

    public Individual(int xLength, int yLength, int zLength, int index)
    {
        this.index = index;
        this.gene = new Cube[xLength][yLength][zLength];
        this.fitness = 0;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
        this.r = new Random();
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    this.gene[i][j][k] = new Cube(i, j, k, this.r.nextBoolean());
//                    this.gene[i][j][k].print();
                }
            }
        }
    }

    public Individual(Individual in)
    {
        this.index = in.getIndex() + 100;
        this.gene = in.getGene().clone();
        this.fitness = 0;
        this.xLength = in.getXLength();
        this.yLength = in.getYLength();
        this.zLength = in.getZLength();  
        
        this.r = new Random();
    }

    Individual(Individual individual1, Individual individual2, int xCross, int yCross, int zCross, int index)
    {
        this.gene = new Cube[individual1.getXLength()][individual1.getYLength()][individual1.getZLength()];
        this.xLength = individual1.getXLength();
        this.yLength = individual1.getYLength();
        this.zLength = individual1.getZLength();  
        this.index = index;
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    if (i < xCross && j < yCross && k < zCross)
                    {
                        this.gene[i][j][k] = individual1.getGene()[i][j][k];
                    }
                    else
                    {
                        this.gene[i][j][k] = individual2.getGene()[i][j][k];
                    }
                    
                }
            }
        }
        
        this.fitness = 0;
        this.r = new Random();
    }

    public int getIndex()
    {
        return index;
    }
    
    public int getFitness()
    {
        return fitness;
    }

    public synchronized void calcFitness()
    {
        this.fitness = 0;
        int numberOfRoadBlocks = 0;
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    if(this.gene[i][j][k].isRoad())
                    {
                        if(i + 1 < xLength)
                        {
                            if(this.gene[i + 1][j][k].isRoad())
                            {
                                numberOfRoadBlocks++;
                            }
                        }
                        if(k + 1 < zLength)
                        {
                            if(this.gene[i][j][k + 1].isRoad())
                            {
                                numberOfRoadBlocks++;
                            }
                        }
                        else if(i != 0)
                        {
                            if(this.gene[i - 1][j][k].isRoad())
                            {
                            numberOfRoadBlocks++;
                            }
                        }
                        else if(k != 0)
                        {                        
                            if(this.gene[i][j][k - 1].isRoad())
                            {
                                numberOfRoadBlocks++;
                            }
                        }
                    }
                }
            }
        }
        int numberOfBlocks = xLength * yLength * zLength;
        if((numberOfBlocks / 2) > numberOfRoadBlocks)
        {
            numberOfRoadBlocks = numberOfRoadBlocks * 2;
        }
        this.fitness = numberOfRoadBlocks;
        
    }

    public Cube[][][] getGene()
    {
        return gene;
    }

    public int getXLength()
    {
        return xLength;
    }

    public int getYLength()
    {
        return yLength;
    }

    public int getZLength()
    {
        return zLength;
    }  
    
    public void mutation(double mutationVal)
    {
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    if(mutationVal > r.nextDouble())
                    {
                        this.gene[i][j][k] = new Cube(i, j, k, this.r.nextBoolean());
                    }
                }
            }
        }
    }
    
    public String getName()
    {
        return "Soloution Map " + Integer.toString(this.index) + " Fitness: " + this.getFitness();
    }

    public void print()
    {
        System.out.print(this.index + "*");
        System.out.print(Arrays.toString(this.gene));
        System.out.println("-" + this.fitness);
    }

    public void draw()
    {
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    
                    this.gene[i][j][k].draw();
                }
            }
        }
    }
    
    
    
    
    
}

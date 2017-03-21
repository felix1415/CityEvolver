/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import cityevolver.Block;
import cityevolver.BlockType;
import static cityevolver.Utils.concatenateFloatArrays;
import static cityevolver.Utils.getRandomBlock;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author alexgray
 */
public class Individual
{
    
    private Block[][][] gene;
    private final int xLength;
    private final int yLength;
    private final int zLength;
    private int fitness;
    private final Random r;
    private final int index;
    
    private boolean individualChanged = true;
    private int numberOfVertices;
    FloatBuffer vertexData;
    FloatBuffer colourData;
    int VBOVertexHandle;
    int VBOColourHandle;

    public Individual(int xLength, int yLength, int zLength, int index)
    {
        this.index = index;
        this.gene = new Block[xLength][yLength][zLength];
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
                    this.gene[i][j][k] = new Block(i, j, k, getRandomBlock());
                }
            }
        }
        this.numberOfVertices = 0;
    }
    
    public Individual() // test
    {
        this.index = -1;
        this.gene = new Block[1][1][4];
        this.fitness = 0;
        this.xLength = 1;
        this.yLength = 1;
        this.zLength = 3;
        this.r = new Random();
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    this.gene[i][j][k] = new Block(i, j, k, BlockType.values()[k+2]);
                }
            }
        }
        this.numberOfVertices = 0;
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
        this.gene = new Block[individual1.getXLength()][individual1.getYLength()][individual1.getZLength()];
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

    public Block[][][] getGene()
    {
        return gene;
    }
    
    public Block getGene(int x, int y, int z)
    {
        return gene[x][y][z];
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
                        this.gene[i][j][k] = new Block(i, j, k, getRandomBlock());
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
    
    private int calcNumberOfVertices()
    {
        if(individualChanged)
        {
            for (int i = 0; i < xLength; i++)
            {
                for (int j = 0; j < yLength ; j++)
                {
                    for (int k = 0; k < zLength ; k++)
                    {
                        numberOfVertices += this.gene[i][j][k].getNumberOfVertices();
                    }
                }
            }
        }
        return numberOfVertices;
    }
    
    public float [] getVertexBuffer()
    {
        float [] vertices = new float[0];
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    vertices = concatenateFloatArrays(vertices, this.gene[i][j][k].getVertexData());
                }
            }
        }
        return vertices;
    }
    
    public float [] getColourBuffer()
    {
        float [] colour = null;
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    colour = concatenateFloatArrays(colour, this.gene[i][j][k].getColourData());
                }
            }
        }
        return colour;
    }
    
    public float [] calculateBuffers()
    {
        float [] vertices = new float[0];
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    this.gene[i][j][k].calculateBuffers();
                }
            }
        }
        return vertices;
    }

    public void render()
    {
        if(individualChanged)
        {
            calculateBuffers();
            calcNumberOfVertices();
            
            vertexData = BufferUtils.createFloatBuffer(numberOfVertices);
            colourData = BufferUtils.createFloatBuffer(numberOfVertices);
            
            vertexData.put(getVertexBuffer());
            colourData.put(getColourBuffer());
            
            vertexData.flip();
            colourData.flip();
            
            VBOVertexHandle = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
            VBOColourHandle = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, VBOColourHandle);
            glBufferData(GL_ARRAY_BUFFER, colourData, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
            individualChanged = false;
        }
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0l);

        glBindBuffer(GL_ARRAY_BUFFER, VBOColourHandle);
        glColorPointer(3, GL_FLOAT, 0, 0l);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glDrawArrays(GL_TRIANGLES, 0, numberOfVertices/3);

        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    public void cleanUp()
    {
        glDeleteBuffers(VBOVertexHandle);
        glDeleteBuffers(VBOColourHandle);
    }
    
    
    
    
    
}

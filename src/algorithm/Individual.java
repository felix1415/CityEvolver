/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;


import cityevolver.Block;
import cityevolver.BlockType;
import cityevolver.Utils;
import static cityevolver.Utils.concatenateFloatArrays;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import java.util.ArrayList;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;
import static cityevolver.Utils.getRandomBlock;

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
    public Tuple connected = null;
    
    private boolean individualChanged = true;
    private int numberOfVertices;
    FloatBuffer vertexData = null;
    FloatBuffer colourData = null;
    int VBOVertexHandle = -1;
    int VBOColourHandle = -1;
    
    private final ArrayList<Tuple> blocksAndValuesForSearch;

    public Individual(int xLength, int yLength, int zLength, int index) // intial
    {
        this.blocksAndValuesForSearch = GeneticAlgorithm.getInstance().getBlocksForSearch();
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
                    this.gene[i][j][k] = new Block(i, j, k, getRandomBlock(blocksAndValuesForSearch));
                }
            }
        }
        this.numberOfVertices = 0;
    }
    
    public Individual(int x, int y, int z, Block[][][] blocks) //load in
    {
        this.xLength = x;
        this.yLength = y;
        this.zLength = z;
        this.gene = new Block[xLength][yLength][zLength];
        this.index = -1;
        this.fitness = 0;
        this.numberOfVertices = 0;
        this.r = new Random();
        this.blocksAndValuesForSearch = null;
        
        for (int i = 0; i < xLength; i++)
        {
            for (int j = 0; j < yLength ; j++)
            {
                for (int k = 0; k < zLength ; k++)
                {
                    if(blocks[i][j][k] != null)
                    {
                        this.gene[i][j][k] = blocks[i][j][k];
                    }
                    else
                    {
                        this.gene[i][j][k] = new Block(i, j, k, BlockType.AIR);
                    }
                }
            }
        }
    }
    
    public Individual() // test
    {
        this.index = -1;
        this.xLength = 3;
        this.yLength = 1;
        this.zLength = BlockType.values().length;
        this.gene = new Block[xLength][yLength][zLength];
        this.fitness = 0;
        
        this.r = new Random();
        for (int x = 0; x < xLength; x++)
        {
            for (int y = 0; y < yLength; y++)
            {
                for (int z = 0; z < zLength ; z++)
                {
                    if(x == 0 || z == 4)
                    {
                        this.gene[x][y][z] = new Block(x, y, z, BlockType.ROAD);
                    }
                    else
                    {
                        this.gene[x][y][z] = new Block(x, y, z, BlockType.LIGHTRESIDENTIAL);
                    }
                }
            }
        }
        this.numberOfVertices = 0;
        ArrayList<Tuple> test = new ArrayList<>(); 
        test.add(new Tuple(0,BlockType.ROAD));
        test.add(new Tuple(0,BlockType.LIGHTRESIDENTIAL));
        this.blocksAndValuesForSearch = test;
    }

    public Individual(Individual in) // copy
    {
        this.index = in.getIndex() + 100;
        this.gene = in.getGene().clone();
        this.fitness = 0;
        this.xLength = in.getXLength();
        this.yLength = in.getYLength();
        this.zLength = in.getZLength();  
        
        this.r = new Random();
        this.blocksAndValuesForSearch = (ArrayList<Tuple>) in.getBlocksForSearch().clone();
        this.connected = in.connected;
    }

    Individual(Individual individual1, Individual individual2, int xCross, int yCross, int zCross, int index) //crossover
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
        this.blocksAndValuesForSearch = (ArrayList<Tuple>) individual1.getBlocksForSearch().clone();
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
        
        this.gene = HardConstraintEnforcement.getInstance().applyConstraints(xLength, yLength, zLength, this.gene);
        int roadFitness = 0;
        int airfitness = 0;
        int grassfitness = 0;
        int lResidentialFitness = 0;
        int dResidentialFitness = 0;
        int lCommercialFitness = 0;
        int dCommercialFitness = 0;
        int farmlandFitness = 0;
        int industryFitness = 0;
        int hospitalFitness = 0;
        int policeFitness = 0;
        int fireFitness = 0;
        int educationFitness = 0;
        
        int numberOfRoads = 0;
        int numberOfAir = 0;
        int numberOfGrass = 0;
        int numberOfLResidential = 0;
        int numberOfDResidential = 0;
        int numberOfLCommercial = 0;
        int numberOfDCommercial = 0;
        int numberOfFarmland = 0;
        int numberOfIndustry = 0;
        int numberOfHospital = 0;
        int numberOfPolice = 0;
        int numberOfFire = 0;
        int numberOfEducation = 0;
        for (int y = 0; y < yLength ; y++)
        {
            for (int x = 0; x < xLength; x++)
            {
                for (int z = 0; z < zLength ; z++)
                {
                    if(HardConstraintEnforcement.getInstance().isLowestLevel(x,y,z))
                    {
                        if(this.gene[x][y][z].isRoad())
                        {
                            ++numberOfRoads;
                            roadFitness += HardConstraintEnforcement.getInstance().roadFitness(x, y, z);
                        }
                        else if(this.gene[x][y][z].isAir())
                        {
                            ++numberOfAir;
                        }
                        else if(this.gene[x][y][z].isRoad())
                        {
                            //roads are flat and create air space
                            ++numberOfAir;
                        }
                        else if(this.gene[x][y][z].isWater())
                        {
                            //water is flat and create air space
                            ++numberOfAir;
                        }
                        else if(this.gene[x][y][z].isGrass())
                        {
                            //grass are flat and create air space
                            ++numberOfAir;
                            ++numberOfGrass;
                        }
                        else if(this.gene[x][y][z].isLResidential())
                        {
                            ++numberOfLResidential;
                            lResidentialFitness += HardConstraintEnforcement.getInstance().lightResidentialFitness(x, y, z);
                        }
                        else if(this.gene[x][y][z].isDResidential())
                        {
                            dResidentialFitness += HardConstraintEnforcement.getInstance().denseResidentialFitness(x, y, z);
                            ++numberOfDResidential;
                        }
                        else if(this.gene[x][y][z].isLCommercial())
                        {
                            lCommercialFitness += HardConstraintEnforcement.getInstance().lightCommercialFitness(x, y, z);
                            ++numberOfLCommercial;
                        }
                        else if(this.gene[x][y][z].isDCommercial())
                        {
                            dCommercialFitness += HardConstraintEnforcement.getInstance().denseCommercialFitness(x, y, z);
                            ++numberOfDCommercial;
                        }
                        else if(this.gene[x][y][z].isFarmland())
                        {
                            farmlandFitness += HardConstraintEnforcement.getInstance().farmlandFitness(x, y, z);
                            ++numberOfLCommercial;
                        }
                        else if(this.gene[x][y][z].isIndustry())
                        {
                            industryFitness += HardConstraintEnforcement.getInstance().industryFitness(x, y, z);
                            ++numberOfDCommercial;
                        }
                        else if(this.gene[x][y][z].isHospital())
                        {
                            hospitalFitness += HardConstraintEnforcement.getInstance().hospitalFitness(x, y, z);
                            ++numberOfHospital;
                        }
                        else if(this.gene[x][y][z].isPolice())
                        {
                            hospitalFitness += HardConstraintEnforcement.getInstance().policeFitness(x, y, z);
                            ++numberOfPolice;
                        }
                        else if(this.gene[x][y][z].isFire())
                        {
                            hospitalFitness += HardConstraintEnforcement.getInstance().fireFitness(x, y, z);
                            ++numberOfFire;
                        }
                        else if(this.gene[x][y][z].isEducation())
                        {
                            hospitalFitness += HardConstraintEnforcement.getInstance().educationFitness(x, y, z);
                            ++numberOfEducation;
                        }
                        
                        
                    }
                }
            }
        }
        float numberOfLowestLevelBlocks = xLength * zLength;
        float numberOfAllBlocks = xLength * yLength * zLength;
        float roadWeight = ((float)GeneticAlgorithm.getInstance().getRoadsValue() / 100f);
        int idealRoadBlocks = Utils.roundFloat(numberOfLowestLevelBlocks * roadWeight);
        float idealAirBlocks = numberOfAllBlocks * ((float)GeneticAlgorithm.getInstance().getAirValue()/ 100f);
        // if close to the precent of road blocks required
        if(((float)idealRoadBlocks * 0.9) < numberOfRoads && (numberOfRoads < ((float)idealRoadBlocks * 1.1) )) // if 
        {
            roadFitness *= roadWeight * 4;
        }
        //if all roads are connected, bonus fitness
        roadFitness += HardConstraintEnforcement.getInstance().isAllRoadsConnectedFitness();
        this.fitness += roadFitness;
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
        for (int x = 0; x < xLength; x++)
        {
            for (int y = 0; y < yLength ; y++)
            {
                for (int z = 0; z < zLength ; z++)
                {
                    if(mutationVal > r.nextDouble())
                    {
                        if(HardConstraintEnforcement.getInstance().isLowestLevel(x, y, z))
                        {
                            this.gene[x][y][z] = new Block(x, y, z, getRandomBlock(blocksAndValuesForSearch, BlockType.ROAD));
                        }
                        else
                        {
                            this.gene[x][y][z] = new Block(x, y, z, getRandomBlock(blocksAndValuesForSearch));
                        }
                    }
                }
            }
        }
    }
    
    public String getName()
    {
        return "Soloution Map " + Integer.toString(this.index) + " Fitness: " + this.getFitness();
    }

    public ArrayList<Tuple> getBlocksForSearch()
    {
        return blocksAndValuesForSearch;
    }

    public void print()
    {
        System.out.print(this.index + "*");
        System.out.print(Arrays.toString(this.gene));
        System.out.println("-" + this.fitness);
    }
    
    private int calcNumberOfVertices()
    {
        numberOfVertices = 0;
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
    
    public void calculateBuffers()
    {
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
    }

    public void render()
    {
        if(individualChanged)
        {
            vertexData = null;
            colourData = null;
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
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}

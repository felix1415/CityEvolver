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
import gui.GUIForm;

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
    private final Random random;
    private final int index;
    public Tuple connected = null;
    
    private boolean individualChanged = true;
    private int numberOfVertices;
    FloatBuffer vertexData = null;
    FloatBuffer colourData = null;
    int VBOVertexHandle = -1;
    int VBOColourHandle = -1;
    
    private int roadFitness = 0;
    private int airFitness = 0;
    private int grassFitness = 0;
    private int lResidentialFitness = 0;
    private int dResidentialFitness = 0;
    private int lCommercialFitness = 0;
    private int dCommercialFitness = 0;
    private int farmlandFitness = 0;
    private int industryFitness = 0;
    private int hospitalFitness = 0;
    private int policeFitness = 0;
    private int fireFitness = 0;
    private int educationFitness = 0;

    private int numberOfRoads = 0;
    private int numberOfAir = 0;
    private int numberOfGrass = 0;
    private int numberOfLResidential = 0;
    private int numberOfDResidential = 0;
    private int numberOfLCommercial = 0;
    private int numberOfDCommercial = 0;
    private int numberOfFarmland = 0;
    private int numberOfIndustry = 0;
    private int numberOfHospital = 0;
    private int numberOfPolice = 0;
    private int numberOfFire = 0;
    private int numberOfEducation = 0;
    
    private int idealRoadBlocks = 0;
    private int idealAirBlocks = 0;
    private int idealGrassBlocks = 0;
    private int idealLightResidential = 0;
    private int idealDenseResidential = 0;
    private int idealLightCommercial = 0;
    private int idealDenseCommercial = 0;
    private int idealFarmland = 0;
    private int idealIndustry = 0;
    private int idealHospital = 0;
    private int idealPolice = 0;
    private int idealFire = 0;
    private int idealEducation = 0;
    
    private final ArrayList<Tuple> blocksForSearch;

    public Individual(int xLength, int yLength, int zLength, int index) // intial
    {
        this.blocksForSearch = GeneticAlgorithm.getInstance().getBlocksForSearch();
        this.index = index;
        this.gene = new Block[xLength][yLength][zLength];
        this.fitness = 0;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
        this.random = new Random();
        for (int i = 0; i < xLength; ++i)
        {
            for (int j = 0; j < yLength ; ++j)
            {
                for (int k = 0; k < zLength ; ++k)
                {
                    this.gene[i][j][k] = new Block(i, j, k, Utils.getRandomWeightedBlock(this.blocksForSearch));
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
        this.random = new Random();
        this.blocksForSearch = null;
        
        for (int i = 0; i < xLength; ++i)
        {
            for (int j = 0; j < yLength ; ++j)
            {
                for (int k = 0; k < zLength ; ++k)
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
        this.yLength = 2;
        this.zLength = 3;
        this.gene = new Block[xLength][yLength][zLength];
        this.fitness = 0;
        
        this.random = new Random();
        for (int x = 0; x < xLength; ++x)
        {
            for (int y = 0; y < yLength; ++y)
            {
                for (int z = 0; z < zLength ; ++z)
                {
                    if(x == 0 || z == 4)
                    {
                        this.gene[x][y][z] = new Block(x, y, z, BlockType.ROAD);
                    }
                    else if(y == 1)
                    {
                        this.gene[x][y][z] = new Block(x, y, z, BlockType.GRASS);
                    }
                    else
                    {
                        this.gene[x][y][z] = new Block(x, y, z, BlockType.LIGHTCOMMERCIAL);
                    }
                    
                    if(x == 0 && z == 0 && y == 0)
                    {
                        this.gene[x][y][z] = new Block(x, y, z, BlockType.GRASS);
                    }
                    if(x == 0 && z == 1 && y == 1)
                    {
                        this.gene[x][y][z] = new Block(x, y, z, BlockType.GRASS);
                    }
                    
                }
            }
        }
        this.numberOfVertices = 0;
        ArrayList<Tuple> test = new ArrayList<>(); 
        test.add(new Tuple(BlockType.ROAD, 0f));
        test.add(new Tuple(BlockType.LIGHTCOMMERCIAL, 0f));
        test.add(new Tuple(BlockType.GRASS, 0f));
        this.blocksForSearch = test;
    }

    public Individual(Individual in) // copy for view
    {
        this.index = in.getIndex() + 100;
        this.gene = in.getGene().clone();
        this.fitness = 0;
        this.xLength = in.getXLength();
        this.yLength = in.getYLength();
        this.zLength = in.getZLength();  
        
        this.random = new Random();
        this.blocksForSearch = GeneticAlgorithm.getInstance().getBlocksForSearch();
        this.connected = in.connected;
        this.roadFitness = in.roadFitness;
        this.airFitness = in.airFitness;
        this.grassFitness = in.grassFitness;
        this.lResidentialFitness = in.lResidentialFitness;
        this.dResidentialFitness = in.dResidentialFitness;
        this.lCommercialFitness = in.lCommercialFitness;
        this.dCommercialFitness = in.dCommercialFitness;
        this.farmlandFitness = in.farmlandFitness;
        this.industryFitness = in.industryFitness;
        this.hospitalFitness = in.hospitalFitness;
        this.policeFitness = in.policeFitness;
        this.fireFitness = in.fireFitness;
        this.educationFitness = in.educationFitness;
        
        this.numberOfRoads = in.numberOfRoads;
        this.numberOfAir = in.numberOfAir;
        this.numberOfGrass = in.numberOfGrass;
        this.numberOfLResidential = in.numberOfLResidential;
        this.numberOfDResidential = in.numberOfDResidential;
        this.numberOfLCommercial = in.numberOfLCommercial;
        this.numberOfDCommercial = in.numberOfDCommercial;
        this.numberOfFarmland = in.numberOfFarmland;
        this.numberOfIndustry = in.numberOfIndustry;
        this.numberOfHospital = in.numberOfHospital;
        this.numberOfPolice = in.numberOfPolice;
        this.numberOfFire = in.numberOfFire;
        this.numberOfEducation = in.numberOfEducation;
        
        this.idealRoadBlocks = in.idealRoadBlocks;
        this.idealAirBlocks = in.idealAirBlocks;
        this.idealGrassBlocks = in.idealGrassBlocks;
        this.idealLightResidential = in.idealLightResidential;
        this.idealDenseResidential = in.idealDenseResidential;
        this.idealLightCommercial = in.idealLightCommercial;
        this.idealDenseCommercial = in.idealDenseCommercial;
        this.idealFarmland = in.idealFarmland;
        this.idealIndustry = in.idealIndustry;
        this.idealHospital = in.idealHospital;
        this.idealPolice = in.idealPolice;
        this.idealFire = in.idealFire;
        this.idealEducation = in.idealEducation;
    }
    
    Individual(Individual individual1, Individual individual2, int xCross, int yCross, int zCross, int index) //crossover
    {
        this.gene = new Block[individual1.getXLength()][individual1.getYLength()][individual1.getZLength()];
        this.xLength = individual1.getXLength();
        this.yLength = individual1.getYLength();
        this.zLength = individual1.getZLength();  
        this.index = index;
        
        for (int i = 0; i < xLength; ++i)
        {
            for (int j = 0; j < yLength ; ++j)
            {
                for (int k = 0; k < zLength ; ++k)
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
        this.random = new Random();
        this.blocksForSearch = GeneticAlgorithm.getInstance().getBlocksForSearch();
    }

    public Individual(int x, int y, int z, int indexNumber, int fitness, Block[][][] blocks, ArrayList<Tuple> blocksForSearch)
    {
        this.xLength = x;
        this.yLength = y;
        this.zLength = z;
        this.index = indexNumber;
        this.fitness = fitness;
        this.gene = blocks;
        this.blocksForSearch = blocksForSearch;
        this.random = new Random();
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
        initialiseFitnessVariables();
        
        //needs to be changed back to applyConstraints
        this.gene = HardConstraintEnforcement.getInstance().applyConstraints
        (xLength, yLength, zLength, this.gene, blocksForSearch);
        
        for (int y = 0; y < yLength; ++y)
        {
            for (int x = 0; x < xLength; ++x)
            {
                for (int z = 0; z < zLength ; ++z)
                {
                    if(HardConstraintEnforcement.getInstance().isLowestLevel(x,y,z))
                    {
                        if(this.gene[x][y][z].isRoad())
                        {
                            ++numberOfRoads;
                            roadFitness += HardConstraintEnforcement.getInstance().roadFitness(x, y, z);
                        }
                    }
                    
                    if(this.gene[x][y][z].isAir())
                    {
                        ++numberOfAir;
                        ++airFitness;
                    }
                    else if(this.gene[x][y][z].isWater())
                    {
                        //water is flat and create air space
                        ++numberOfAir;
                        ++airFitness;
                    }
                    else if(this.gene[x][y][z].isGrass())
                    {
                        //grass are flat and create air space
                        ++numberOfAir;
                        ++numberOfGrass;
                        ++grassFitness;
                        ++airFitness;
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
                        ++numberOfFarmland;
                    }
                    else if(this.gene[x][y][z].isIndustry())
                    {
                        industryFitness += HardConstraintEnforcement.getInstance().industryFitness(x, y, z);
                        ++numberOfIndustry;
                    }
                    else if(this.gene[x][y][z].isHospital())
                    {
                        policeFitness += HardConstraintEnforcement.getInstance().hospitalFitness(x, y, z);
                        ++numberOfHospital;
                    }
                    else if(this.gene[x][y][z].isPolice())
                    {
                        fireFitness += HardConstraintEnforcement.getInstance().policeFitness(x, y, z);
                        ++numberOfPolice;
                    }
                    else if(this.gene[x][y][z].isFire())
                    {
                        fireFitness += HardConstraintEnforcement.getInstance().fireFitness(x, y, z);
                        ++numberOfFire;
                    }
                    else if(this.gene[x][y][z].isEducation())
                    {
                        educationFitness += HardConstraintEnforcement.getInstance().educationFitness(x, y, z);
                        ++numberOfEducation;
                    }
                        
                }
            }
        }
        
        //double fitness of grass, ensure fair representation
        grassFitness *= 2;
        
        idealRoadBlocks = Utils.roundFloat(GeneticAlgorithm.getInstance().getNumberOfLowestLevelBlocks() *
                GeneticAlgorithm.getInstance().getRoadWeight());
        idealAirBlocks = Utils.roundFloat(GeneticAlgorithm.getInstance().getNumberOfAllBlocks() *
                GeneticAlgorithm.getInstance().getAirWeight());
        idealGrassBlocks = Utils.roundFloat(GeneticAlgorithm.getInstance().getNumberOfAllBlocks() *
                GeneticAlgorithm.getInstance().getGrassWeight());

        int numberOfAllRemainingBlocks = GeneticAlgorithm.getInstance().getNumberOfAllBlocks();
        numberOfAllRemainingBlocks -= idealRoadBlocks;
        numberOfAllRemainingBlocks -= idealGrassBlocks;
        
        idealLightResidential = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getLightResidentialWeight());
        idealDenseResidential = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getDenseResidentialWeight());
        idealLightCommercial = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getLightCommercialWeight());
        idealDenseCommercial = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getDenseCommercialWeight());
        idealFarmland = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getFarmlandWeight());
        idealIndustry = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getIndustryWeight());
        idealHospital = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getHospitalWeight());
        idealPolice = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getPoliceWeight());
        idealFire = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getFireWeight());
        idealEducation = Utils.roundFloat(numberOfAllRemainingBlocks *
                GeneticAlgorithm.getInstance().getEducationWeight());

        //if all roads are connected, bonus fitness
        roadFitness += HardConstraintEnforcement.getInstance().isAllBlockTypeConnectedFitness(BlockType.ROAD) * 2;
        // if close to the precent of road blocks required
        roadFitness += HardConstraintEnforcement.getInstance().fitnessMultiplier(idealRoadBlocks, numberOfRoads, roadFitness, 1.5f);
        
        airFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealAirBlocks, numberOfAir, airFitness, 2);
        grassFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealGrassBlocks, numberOfGrass, grassFitness, 2);
        lResidentialFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealLightResidential, numberOfLResidential, lResidentialFitness, 2);
        dResidentialFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealDenseResidential, numberOfDResidential, dResidentialFitness, 2);
        lCommercialFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealLightCommercial, numberOfLCommercial, lCommercialFitness, 2);
        dCommercialFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealDenseCommercial, numberOfDCommercial, dCommercialFitness, 2);
        farmlandFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealFarmland, numberOfFarmland, farmlandFitness, 2);
        industryFitness = HardConstraintEnforcement.getInstance().fitnessMultiplier(idealIndustry, numberOfIndustry, industryFitness, 2);
        
        hospitalFitness += HardConstraintEnforcement.getInstance().isAllBlockTypeConnectedFitness(BlockType.HOSPTIAL);
        hospitalFitness += HardConstraintEnforcement.getInstance().fitnessMultiplier(idealHospital, numberOfHospital, hospitalFitness, 2);
        policeFitness += HardConstraintEnforcement.getInstance().isAllBlockTypeConnectedFitness(BlockType.POLICE);
        policeFitness += HardConstraintEnforcement.getInstance().fitnessMultiplier(idealPolice, numberOfPolice, policeFitness, 2);
        fireFitness += HardConstraintEnforcement.getInstance().isAllBlockTypeConnectedFitness(BlockType.FIRE);
        fireFitness += HardConstraintEnforcement.getInstance().fitnessMultiplier(idealFire, numberOfFire, fireFitness, 2);
        educationFitness += HardConstraintEnforcement.getInstance().isAllBlockTypeConnectedFitness(BlockType.EDUCATION);
        educationFitness += HardConstraintEnforcement.getInstance().fitnessMultiplier(idealEducation, numberOfEducation, educationFitness, 2);       
        
        this.fitness += roadFitness;
        this.fitness += airFitness;
        this.fitness += grassFitness;
        this.fitness += lResidentialFitness;
        this.fitness += dResidentialFitness;
        this.fitness += lCommercialFitness;
        this.fitness += dCommercialFitness;
        this.fitness += farmlandFitness;
        this.fitness += industryFitness;
        this.fitness += hospitalFitness * 0.1;
        this.fitness += policeFitness * 0.1;
        this.fitness += fireFitness * 0.1;
        this.fitness += educationFitness * 0.1;
        
    }
    
    private int getTypeFitness(BlockType type)
    {
        if(null != type)
        switch (type)
        {
            case AIR:
                return airFitness;
            case ROAD:
                return roadFitness;
            case GRASS:
                return grassFitness;
            case LIGHTRESIDENTIAL:
                return lResidentialFitness;
            case DENSERESIDENTIAL:
                return dResidentialFitness;
            case LIGHTCOMMERCIAL:
                return lCommercialFitness;
            case DENSECOMMERCIAL:
                return dCommercialFitness;
            case FARMLAND:
                return farmlandFitness;
            case INDUSTRY:
                return industryFitness;
            case HOSPTIAL:
                return hospitalFitness;
            case POLICE:
                return policeFitness;
            case FIRE:
                return fireFitness;
            case EDUCATION:
                return educationFitness;
            default:
                break;
        }
        return 0;
    }
    
    public void displayStats()
    {
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getRoadsUtilityLabel(),
                "Roads", roadFitness, 
                numberOfRoads, idealRoadBlocks);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getAirUtilityLabel(),
                "Air", airFitness, 
                numberOfAir, idealAirBlocks);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getGrassUtilityLabel(),
                "Grass", grassFitness, 
                numberOfGrass, idealGrassBlocks);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getLightResidentialUtilityLabel(),
                "Light Residential", lResidentialFitness, 
                numberOfLResidential, idealLightResidential);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getDenseResidentialUtilityLabel(),
                "Dense Residential", dResidentialFitness, 
                numberOfDCommercial, idealDenseCommercial);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getLightCommercialUtilityLabel(),
                "Light Commercial", lCommercialFitness, 
                numberOfLCommercial, idealLightCommercial);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getDenseCommericalUtilityLabel(),
                "Dense Commercial", dCommercialFitness, 
                numberOfDCommercial, idealDenseCommercial);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getFarmlandUtilityLabel(),
                "Farmland", farmlandFitness, 
                numberOfFarmland, idealFarmland);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getIndustryUtilityLabel(),
                "Industry", industryFitness, 
                numberOfIndustry, idealIndustry);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getHospitalUtilityLabel(),
                "Hospital", hospitalFitness, 
                numberOfHospital, idealHospital);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getPoliceUtilityLabel(),
                "Police", policeFitness, 
                numberOfPolice, idealPolice);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getFireUtilityLabel(),
                "Fire", fireFitness, 
                numberOfFire, idealFire);
        GUIForm.getInstance().setUtilityLabel(
                GUIForm.getInstance().getEducationUtilityLabel(),
                "Education", educationFitness, 
                numberOfEducation, idealEducation);
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
        for (int x = 0; x < xLength; ++x)
        {
            for (int y = 0; y < yLength ; ++y)
            {
                for (int z = 0; z < zLength ; ++z)
                {
                    if(mutationVal > random.nextDouble())
                    {
                        if(HardConstraintEnforcement.getInstance().isLowestLevel(x, y, z))
                        {
                            this.gene[x][y][z] = new Block(x, y, z, Utils.getRandomBlock(this.blocksForSearch, BlockType.ROAD, 0.2f));
                        }
                        else
                        {
                            BlockType type = Utils.getRandomWeightedBlock(this.blocksForSearch);
                            int fitness = getTypeFitness(type);
                            if(HardConstraintEnforcement.getInstance().isNextToType(x, y, z, type) &&
                                    fitness > 0)
                            {
                                this.gene[x][y][z] = new Block(x, y, z, Utils.getRandomWeightedBlock(this.blocksForSearch));
                            }
                            else
                            {
                                this.gene[x][y][z] = new Block(x, y, z, Utils.getRandomWeightedBlock(this.blocksForSearch));
                            }
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
        return blocksForSearch;
    }

    public void print()
    {
        System.out.print(this.index + "*");
        System.out.print(Arrays.toString(this.gene));
        System.out.println("-" + this.fitness);
    }
    
    private void initialiseFitnessVariables()
    {
        roadFitness = 0;
        airFitness = 0;
        grassFitness = 0;
        lResidentialFitness = 0;
        dResidentialFitness = 0;
        lCommercialFitness = 0;
        dCommercialFitness = 0;
        farmlandFitness = 0;
        industryFitness = 0;
        hospitalFitness = 0;
        policeFitness = 0;
        fireFitness = 0;
        educationFitness = 0;

        numberOfRoads = 0;
        numberOfAir = 0;
        numberOfGrass = 0;
        numberOfLResidential = 0;
        numberOfDResidential = 0;
        numberOfLCommercial = 0;
        numberOfDCommercial = 0;
        numberOfFarmland = 0;
        numberOfIndustry = 0;
        numberOfHospital = 0;
        numberOfPolice = 0;
        numberOfFire = 0;
        numberOfEducation = 0;

        idealRoadBlocks = 0;
        idealAirBlocks = 0;
        idealGrassBlocks = 0;
        idealLightResidential = 0;
        idealDenseResidential = 0;
        idealLightCommercial = 0;
        idealDenseCommercial = 0;
        idealFarmland = 0;
        idealIndustry = 0;
        idealHospital = 0;
        idealPolice = 0;
        idealFire = 0;
        idealEducation = 0;
    }
    
    private int calcNumberOfVertices()
    {
        numberOfVertices = 0;
        if(individualChanged)
        {
            for (int i = 0; i < xLength; ++i)
            {
                for (int j = 0; j < yLength ; ++j)
                {
                    for (int k = 0; k < zLength ; ++k)
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
        for (int i = 0; i < xLength; ++i)
        {
            for (int j = 0; j < yLength ; ++j)
            {
                for (int k = 0; k < zLength ; ++k)
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
        for (int i = 0; i < xLength; ++i)
        {
            for (int j = 0; j < yLength ; ++j)
            {
                for (int k = 0; k < zLength ; ++k)
                {
                    colour = concatenateFloatArrays(colour, this.gene[i][j][k].getColourData());
                }
            }
        }
        return colour;
    }
    
    public void calculateBuffers()
    {
        for (int i = 0; i < xLength; ++i)
        {
            for (int j = 0; j < yLength ; ++j)
            {
                for (int k = 0; k < zLength ; ++k)
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

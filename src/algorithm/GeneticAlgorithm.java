/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import cityevolver.BlockType;
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
    
    private int roadsValue;
    private int grassValue;
    private int lightResidentialValue;
    private int denseResidentialValue;
    private int lightCommercialValue;
    private int denseCommercialValue;
    private int farmlandValue;
    private int industryValue;
    private int hospitalValue;
    private int policeValue;
    private int fireValue;
    private int educationValue;
    private int airValue;
    
    private int numberOfLowestLevelBlocks = 0;
    private int numberOfAllBlocks = 0;
    private int buildingSpace = 0;
    private float lightResidentialWeight = 0;
    private float denseResidentialWeight = 0;
    private float lightCommercialWeight = 0;
    private float denseCommercialWeight = 0;
    private float farmlandWeight = 0;
    private float industryWeight = 0;
    private float hospitalWeight = 0;
    private float policeWeight = 0;
    private float fireWeight = 0;
    private float educationWeight = 0;
    private float roadWeight = 0;
    private float airWeight = 0;
    private float grassWeight = 0;
    
    private ArrayList<Tuple> blocksForSearch;
    
    protected GeneticAlgorithm() 
    {
        this.running = false;
        this.blocksForSearch = new ArrayList<>();
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

    public int getX()
    {
        return xBound;
    }

    public int getY()
    {
        return yBound;
    }

    public int getZ()
    {
        return zBound;
    }
    
    public synchronized void setBounds(int x, int y, int z)
    {
        this.xBound = x;
        this.yBound = y;
        this.zBound = z;
    }

    public synchronized void setGenerations(int generations)
    {
        this.generations = generations;
    }

    public synchronized void setPopulationNumber(int populationNum)
    {
        this.populationNumber = populationNum;
    }

    public synchronized void setMutation(float mutation)
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

    public synchronized void initialise()
    {
        try
        {
            this.population = new Population(xBound, yBound, zBound, populationNumber, generations, mutation);
        }
        catch(Exception e)
        {
            GUIForm.getInstance().log("Set initial map value: " + e.toString(), true);
        }
    }
    
    public synchronized boolean isInitialised()
    {
        return this.population != null;
    }

    public synchronized void updateGeneratedPopulationList()
    {
        if(this.population != null)
        {
            this.population.updateGeneratedPopulationList();
        }
    }

    public synchronized boolean getRunning()
    {
        return running;
    }
    
    public synchronized void setRunning(boolean running)
    {
        this.running = running;
    }

    public synchronized void setRoadValue(int value)
    {
        roadsValue = value;
    }

    public void setAirValue(int airValue)
    {
        this.airValue = airValue;
    }

    public synchronized void setGrassValue(int value)
    {
        grassValue = value;
    }

    public synchronized void setLightResidentialValue(int value)
    {
        lightResidentialValue = value;
    }

    public synchronized void setLightCommercialValue(int value)
    {
        lightCommercialValue = value;
    }

    public synchronized void setDenseResidentialValue(int value)
    {
        denseResidentialValue = value;
    }

    public synchronized void setDenseCommercialValue(int value)
    {
        denseCommercialValue = value;
    }

    public synchronized void setFarmlandValue(int value)
    {
        farmlandValue = value;
    }

    public synchronized void setHospitalValue(int value)
    {
        hospitalValue = value;
    }

    public synchronized void setIndustryValue(int value)
    {
        industryValue = value;
    }

    public synchronized void setFireValue(int value)
    {
        fireValue = value;
    }

    public synchronized void setPoliceValue(int value)
    {
        policeValue = value;
    }

    public synchronized void setEducationValue(int value)
    {
        educationValue = value;
    }
    
    public int getAirValue()
    {
        return airValue;
    }

    public synchronized int getRoadsValue()
    {
        return roadsValue;
    }

    public synchronized int getGrassValue()
    {
        return grassValue;
    }

    public synchronized int getLightResidentialValue()
    {
        return lightResidentialValue;
    }

    public synchronized int getDenseResidentialValue()
    {
        return denseResidentialValue;
    }

    public synchronized int getLightCommercialValue()
    {
        return lightCommercialValue;
    }

    public synchronized int getDenseCommercialValue()
    {
        return denseCommercialValue;
    }

    public synchronized int getFarmlandValue()
    {
        return farmlandValue;
    }

    public synchronized int getIndustryValue()
    {
        return industryValue;
    }

    public synchronized int getHospitalValue()
    {
        return hospitalValue;
    }

    public synchronized int getPoliceValue()
    {
        return policeValue;
    }

    public synchronized int getFireValue()
    {
        return fireValue;
    }

    public synchronized int getEducationValue()
    {
        return educationValue;
    }
    
    public synchronized ArrayList<Tuple> updateBlocksForSearch()
    {
        ArrayList<Tuple> allBlockValues = new ArrayList<>();
        allBlockValues.add(new Tuple(airValue, BlockType.AIR));
        allBlockValues.add(new Tuple(roadsValue, BlockType.ROAD));
        allBlockValues.add(new Tuple(grassValue, BlockType.GRASS));
        allBlockValues.add(new Tuple(lightResidentialValue, BlockType.LIGHTRESIDENTIAL));
        allBlockValues.add(new Tuple(denseResidentialValue, BlockType.DENSERESIDENTIAL));
        allBlockValues.add(new Tuple(lightCommercialValue, BlockType.LIGHTCOMMERCIAL));
        allBlockValues.add(new Tuple(denseCommercialValue, BlockType.DENSECOMMERCIAL));
        allBlockValues.add(new Tuple(farmlandValue, BlockType.FARMLAND));
        allBlockValues.add(new Tuple(industryValue, BlockType.INDUSTRY));
        allBlockValues.add(new Tuple(hospitalValue, BlockType.HOSPTIAL));
        allBlockValues.add(new Tuple(policeValue, BlockType.POLICE));
        allBlockValues.add(new Tuple(fireValue, BlockType.FIRE));
        allBlockValues.add(new Tuple(educationValue, BlockType.EDUCATION));
        
        this.calcBlockWeights();
        
        this.blocksForSearch.clear();
        allBlockValues.stream().filter((tuple) -> ((int)tuple.x > 0)).forEach((tuple) ->
        {
            this.blocksForSearch.add(new Tuple((BlockType)tuple.y,
                                               getBlockWeight((BlockType)tuple.y)));
        });
        
        return this.blocksForSearch;
    }
    
    private void calcBlockWeights()
    {
        numberOfLowestLevelBlocks = xBound * zBound;
        numberOfAllBlocks = xBound * yBound * zBound;
        buildingSpace = GeneticAlgorithm.getInstance().getLightResidentialValue() +
                GeneticAlgorithm.getInstance().getDenseResidentialValue() + 
                GeneticAlgorithm.getInstance().getLightCommercialValue() + 
                GeneticAlgorithm.getInstance().getDenseCommercialValue() +
                GeneticAlgorithm.getInstance().getFarmlandValue() + 
                GeneticAlgorithm.getInstance().getIndustryValue() + 
                GeneticAlgorithm.getInstance().getHospitalValue() + 
                GeneticAlgorithm.getInstance().getPoliceValue() +
                GeneticAlgorithm.getInstance().getFireValue() + 
                GeneticAlgorithm.getInstance().getEducationValue();
        
        lightResidentialWeight = (float)GeneticAlgorithm.getInstance().getLightResidentialValue() / buildingSpace;
        denseResidentialWeight = (float)GeneticAlgorithm.getInstance().getDenseResidentialValue() / buildingSpace;
        lightCommercialWeight = (float)GeneticAlgorithm.getInstance().getLightCommercialValue() / buildingSpace;
        denseCommercialWeight = (float)GeneticAlgorithm.getInstance().getDenseCommercialValue() / buildingSpace;
        farmlandWeight = (float)GeneticAlgorithm.getInstance().getFarmlandValue() / buildingSpace;
        industryWeight = (float)GeneticAlgorithm.getInstance().getIndustryValue() / buildingSpace;
        hospitalWeight = (float)GeneticAlgorithm.getInstance().getHospitalValue() / buildingSpace;
        policeWeight = (float)GeneticAlgorithm.getInstance().getPoliceValue() / buildingSpace;
        fireWeight = (float)GeneticAlgorithm.getInstance().getFireValue() / buildingSpace;
        educationWeight = (float)GeneticAlgorithm.getInstance().getEducationValue() / buildingSpace;
        
        roadWeight = ((float)GeneticAlgorithm.getInstance().getRoadsValue() / 100f);
        airWeight = ((float)GeneticAlgorithm.getInstance().getAirValue() / 100f);
        grassWeight = ((float)GeneticAlgorithm.getInstance().getGrassValue() / 100f);
    }
    
    private float getBlockWeight(BlockType type)
    {
        if(null != type)
        switch (type)
        {
            case AIR:
                
                return airWeight;
            case ROAD:
                return roadWeight;
            case GRASS:
                return grassWeight;
            case LIGHTRESIDENTIAL:
                return lightResidentialWeight;
            case DENSERESIDENTIAL:
                return denseResidentialWeight;
            case LIGHTCOMMERCIAL:
                return lightCommercialWeight;
            case DENSECOMMERCIAL:
                return denseCommercialWeight;
            case FARMLAND:
                return farmlandWeight;
            case INDUSTRY:
                return industryWeight;
            case HOSPTIAL:
                return hospitalWeight;
            case POLICE:
                return policeWeight;
            case FIRE:
                return fireWeight;
            case EDUCATION:
                return educationWeight;
            default:
                break;
        }
        return 0f;
    }
    
    public synchronized ArrayList<Tuple> getBlocksForSearch()
    {
        return this.blocksForSearch;
    }
    
    public synchronized ArrayList<BlockType> getBlocksForSearchBlockType()
    {
        ArrayList<BlockType> newList = new ArrayList<>();
        blocksForSearch.stream().forEach((type) ->
        {
            newList.add((BlockType)type.x);
        });
        return newList;
    }
    
    public synchronized void setBlocksForSearch(ArrayList<Tuple> blocksForSearch)
    {
        this.blocksForSearch = blocksForSearch;
    }
    
    public synchronized void setBlocksForSearchBlockType(ArrayList<BlockType> blocksForSearchIn)
    {
        ArrayList<Tuple> newList = new ArrayList<>();
        blocksForSearchIn.stream().forEach((type) ->
        {
            newList.add(new Tuple(type, 0f));
        });
        this.blocksForSearch = newList;
    }

    public void setPopulation(Population loaded)
    {
        this.population = loaded;
    }

    public int getNumberOfLowestLevelBlocks()
    {
        return numberOfLowestLevelBlocks;
    }

    public int getNumberOfAllBlocks()
    {
        return numberOfAllBlocks;
    }

    public int getBuildingSpace()
    {
        return buildingSpace;
    }

    public float getLightResidentialWeight()
    {
        return lightResidentialWeight;
    }

    public float getDenseResidentialWeight()
    {
        return denseResidentialWeight;
    }

    public float getLightCommercialWeight()
    {
        return lightCommercialWeight;
    }

    public float getDenseCommercialWeight()
    {
        return denseCommercialWeight;
    }

    public float getFarmlandWeight()
    {
        return farmlandWeight;
    }

    public float getIndustryWeight()
    {
        return industryWeight;
    }

    public float getHospitalWeight()
    {
        return hospitalWeight;
    }

    public float getPoliceWeight()
    {
        return policeWeight;
    }

    public float getFireWeight()
    {
        return fireWeight;
    }

    public float getEducationWeight()
    {
        return educationWeight;
    }

    public float getRoadWeight()
    {
        return roadWeight;
    }

    public float getAirWeight()
    {
        return airWeight;
    }

    public float getGrassWeight()
    {
        return grassWeight;
    }
    
    
    
}

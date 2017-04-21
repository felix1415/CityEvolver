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
    
    private ArrayList<BlockType> blocksForSearch;
    
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
    
    public synchronized ArrayList<BlockType> updateBlocksForSearch()
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
        
        ArrayList<BlockType> blocksForSearch = new ArrayList<>();
        allBlockValues.stream().filter((tuple) -> ((int)tuple.x > 0)).forEach((tuple) ->
        {
            blocksForSearch.add((BlockType)tuple.y);
        });
        
        this.blocksForSearch = blocksForSearch;
        
        return this.blocksForSearch;
    }
    
    public synchronized ArrayList<BlockType> getBlocksForSearch()
    {
        return this.blocksForSearch;
    }
    
    public synchronized void setBlocksForSearch(ArrayList<BlockType> blocksForSearch)
    {
        this.blocksForSearch = blocksForSearch;
    }
    
}

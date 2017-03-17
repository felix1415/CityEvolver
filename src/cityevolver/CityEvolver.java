package cityevolver;

import algorithm.GeneticAlgorithm;
import algorithm.Individual;
import gui.GUIForm;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;

public class CityEvolver
{   
    static Thread guiInstance = new Thread(GUIForm.getInstance());
    static Thread geneticAlgorithmInstance = null;

    public static void main(String[] args)
    {
        guiInstance.start();
        while(true)
        {
            // get start/stop from GUI
            if(GUIForm.getInstance().runGeneticAlgorithm() == false)
            {
                GeneticAlgorithm.getInstance().setRunning(false);
            }
            else
            {
                GeneticAlgorithm.getInstance().setRunning(true);
            }
            
            //if run and thread not alive
            if(GUIForm.getInstance().runGeneticAlgorithm() && geneticAlgorithmInstance == null)
            {
                geneticAlgorithmInstance = new Thread(GeneticAlgorithm.getInstance());
                geneticAlgorithmInstance.start();
                
                //get initial population list
                GeneticAlgorithm.getInstance().updateGeneratedPopulationList();
            } 
            
            //if stop run and thread is alive
            if(!GUIForm.getInstance().runGeneticAlgorithm() && geneticAlgorithmInstance != null)
            {
                try
                {
                    geneticAlgorithmInstance.join();
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(GUIForm.class.getName()).log(Level.SEVERE, null, ex);
                }   
                geneticAlgorithmInstance = null;
            }
        }
    }
}

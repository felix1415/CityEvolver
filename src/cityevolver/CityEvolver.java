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
//    public static int xLength = 40;
//    public static int yLength = 5;
//    public static int zLength = 40;
    
    static Thread guiInstance = new Thread(GUIForm.getInstance());
    static Thread geneticAlgorithmInstance = null;
    static ArrayList<String> solutionMapNames = new ArrayList<>();

    public static void main(String[] args)
    {
        guiInstance.start();
        while(true)
        {
            try
            {
                Thread.sleep(10);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(CityEvolver.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
            
            if(GUIForm.getInstance().runGeneticAlgorithm() && geneticAlgorithmInstance == null)
            {
                geneticAlgorithmInstance = new Thread(GeneticAlgorithm.getInstance());
                geneticAlgorithmInstance.start();
                
                GeneticAlgorithm.getInstance().updateGeneratedPopulationList();
            }
                       
        }
        
    }
    
    private static void updatePopulationMenu()
    {
        
    }
    
    public static void renderWireFrame(boolean wireFrame, int handle)
    {
        if(!wireFrame)
        {
            return;
        }
        glEnable(GL_POLYGON_OFFSET_FILL);
        glColor3f(0, 0, 0); // Color for shape outline
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // enable wireframe for front and back
        glCallList(handle); // Call the same rendering routine for the previous polygon.
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); //fill shape again
    }
}

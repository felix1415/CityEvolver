package cityevolver;

import gui.Camera;
import algorithm.Individual;
import algorithm.Population;
import gui.GUIForm;
import gui.Renderer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

public class CityEvolver
{
    public static int xLength = 40;
    public static int yLength = 5;
    public static int zLength = 40;

    public static void main(String[] args)
    {
        GUIForm g = new GUIForm();
        g.setVisible(true);        
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

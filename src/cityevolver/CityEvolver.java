package cityevolver;

import algorithm.Individual;
import algorithm.Population;
import java.util.ArrayList;
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

    public static void main(String[] args)
    {
        Population p = new Population(20, 1, 20);
        p.run();
        Individual world = p.best();
        initDisplay();

        actionLoop(world);
        cleanUp();
    }

    public static void actionLoop(Individual world)
    {
        Camera camera = new Camera(0, 0, 0);
        
        int displayListHandle = -1;
        
        Mouse.setGrabbed(true);
        
        double time = 0.0f;
        int frames = 0;
        double frameTime = 0.0;
        double currentTime = System.currentTimeMillis();
        while (!Display.isCloseRequested())
        {
            double newTime = System.currentTimeMillis();
            double delta = newTime - currentTime;
            currentTime = newTime; 
            camera.input((float)delta);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            glLoadIdentity();
            camera.cameraView();

            glPushMatrix();
            {
                
                displayListHandle = glGenLists(1);
                                
                glNewList(displayListHandle, GL_COMPILE);
                                
                world.draw();
                
                glEndList();

                renderAsWireFrame(true, displayListHandle);
                
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glEnable(GL_BLEND);
                glEnable(GL_LINE_SMOOTH);
                glCallList(displayListHandle);
                
            }
            glPopMatrix();
            Display.update();
            time += delta;
            frameTime += delta;
            frames++;
            if(frameTime > 1000.0)
            {
                System.out.println("FPS:" + frames);
                frames = 0;
                frameTime = 0.0;
            }
        }
    }

    public static void cleanUp()
    {
        Display.destroy();
    }
    
    public static void renderAsWireFrame(boolean wireFrame, int handle)
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

    public static void initDisplay()
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.setTitle("City Evolver");
            Display.create();
            
        } catch (LWJGLException ex)
        {
            Logger.getLogger(CityEvolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        glViewport(0, 0, 800, 600);
        glMatrixMode(GL11.GL_PROJECTION);
        glLoadIdentity();
        
        glMatrixMode(GL11.GL_MODELVIEW);

        glEnable(GL_DEPTH_TEST);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_COLOR_MATERIAL);
        
    }
}

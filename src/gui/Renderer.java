/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import algorithm.Individual;
import cityevolver.CityEvolver;
import java.awt.Canvas;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.lwjgl.BufferUtils; 
import java.nio.FloatBuffer;


/**
 *
 * @author alexgray
 */
public class Renderer implements Runnable
{
    private static Renderer instance;
    private int displayListHandle;
    private int x = 305;
    private int y = 300;
    private int height = 510;
    private int width = 970;
    private boolean running;
    private Camera camera;
    private Canvas canvas;
    private Individual individual;
    
    private double time;
    private double frameTime;
    private double currentTime;
    private int frames;
    
    int vertexSize = 3; //XYZ
    int colorSize = 3; //RGB
    FloatBuffer vertexData;
    FloatBuffer colorData;
    int VBOVertexHandle;
    int VBOColorHandle;
    
    protected Renderer(Canvas canvas, int height, int width)
    {
        time = 0.0f;
        frameTime = 0.0;
        currentTime = 0;
        frames = 0;
        running = true;
        
        displayListHandle = -1;
        this.canvas = canvas;
        this.height = height;
        this.width = width;
        
        
    }

    public static Renderer getInstance() 
    {
       if(instance == null) 
       {
          instance = new Renderer();
       }
       return instance;
    }
    
    protected Renderer()
    {
        time = 0.0f;
        frameTime = 0.0;
        currentTime = 0;
        frames = 0;
        running = true;
        
        displayListHandle = -1;
    }
    
    private void embedDisplayToCanvas(Canvas parentCanvas, int height, int width)
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(height, width));
            Display.setParent(parentCanvas);
            Display.create();
            
        } catch (LWJGLException ex)
        {
            Logger.getLogger(CityEvolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initDisplay()
    {
        try
        {
            if (Display.isCreated())
            {
                try
                {
                    Display.destroy();
                }
                catch(Exception e)
                {
                    
                }
            }            
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setLocation(x, y);
            Display.create();
            //sky colour
            glClearColor( 0.53f, 0.8f, 0.92f, 1);
            Display.setResizable(false);
            Display.setTitle("City Evolver Viewer");
        } catch (LWJGLException ex)
        {
            Logger.getLogger(CityEvolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public void cleanUp()
    {
        individual.cleanUp();
        camera.cleanUp();
        Display.destroy();
    }
    
    public synchronized void viewMap(Individual individual)
    {
        this.individual = individual;
    }
    
    private void render()
    {
        double newTime = System.currentTimeMillis();
        double delta = newTime - currentTime;
        currentTime = newTime; 
        camera.input((float)delta);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glLoadIdentity();
        camera.cameraView();
        
        individual.render();        

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

    @Override
    public void run()
    {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        if(canvas != null)
        {
            embedDisplayToCanvas(canvas, width, height);
        }
        else
        {
            initDisplay();
        }
        
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        glMatrixMode(GL_MODELVIEW);

        glEnable(GL_DEPTH_TEST);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_COLOR_MATERIAL);
        
        currentTime = System.currentTimeMillis();
        camera = new Camera();
        
        while(running)
        {
            Display.setLocation(x, y);
            render();
            Display.sync(60);
            if(Display.isCloseRequested())
            {
                this.cleanUp();
                return;
            }
        }
        this.cleanUp();
    }
    
    public synchronized boolean isRunning()
    {
        return running;
    }
    
    public synchronized void setRunning(boolean running)
    {
        this.running = running;        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import algorithm.Individual;
import cityevolver.CityEvolver;
import static cityevolver.CityEvolver.renderWireFrame;
import cityevolver.Cube;
import java.awt.Canvas;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author alexgray
 */
public class Renderer implements Runnable
{
    private int displayListHandle;
    private int height;
    private int width;
    private boolean running;
    private Camera camera;
    private Canvas canvas;
    private Individual individual;
    
    private double time;
    private double frameTime;
    private double currentTime;
    private int frames;
    
    Renderer(Canvas canvas, int height, int width)
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

    public Renderer()
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
            Display.setDisplayMode(new DisplayMode(960, 540));
            Display.setLocation(312, 310);
            Display.create();
            Display.setResizable(false);
            Display.setTitle("City Evolver Viewer");            
        } catch (LWJGLException ex)
        {
            Logger.getLogger(CityEvolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void cleanUp()
    {
        camera.cleanUp();
        Display.destroy();
    }
    
    public void viewMap(Individual individual)
    {
        this.individual = individual;
    }
    
    public void render()
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

            Cube c = new Cube(0.0f, 0.0f, -5.0f, true);
            c.draw();

            glEndList();

            renderWireFrame(true, displayListHandle);

            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_BLEND);
            glEnable(GL_LINE_SMOOTH);
            glCullFace(GL_BACK);
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

    @Override
    public void run()
    {
        if(canvas != null)
        {
            embedDisplayToCanvas(canvas, 960, 540);
        }
        else
        {
            initDisplay();
        }
        
        glViewport(0, 0, 960, 540);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        glMatrixMode(GL_MODELVIEW);

        glEnable(GL_DEPTH_TEST);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_COLOR_MATERIAL);
        
        currentTime = System.currentTimeMillis();
        camera = new Camera(0, 0, 0);
        
        while(running)
        {
            render();
            if(Display.isCloseRequested())
            {
                this.cleanUp();
                this.running = false;
            }
        }
    }
    
    public synchronized boolean isRunning()
    {
        return running;
    }
    
    public synchronized void setRunning(boolean running)
    {
        this.running = running;
        this.cleanUp();
    }
}

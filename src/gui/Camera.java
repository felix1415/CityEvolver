package gui;


import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{

    Vector3f position;
    float yaw = 0.0f;
    float pitch = 0.0f;

    public Camera(float x, float y, float z)
    {
        position = new Vector3f(x, y, z);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(70, (float) 960 / (float) 540, 0.3f, 1000);
        glMatrixMode(GL_MODELVIEW);

        glEnable(GL_DEPTH_TEST);
    }

    public void input(float delta)
    {
        float multiplier = 1.0f;
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            multiplier = multiplier * 4;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            this.move(0, 0, -delta * 0.003f * multiplier);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            this.move(0, 0, delta * 0.003f * multiplier);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            this.move(-delta * 0.003f * multiplier, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            this.move(delta * 0.003f * multiplier, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
        {
            position.y += 0.003f * delta * multiplier;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E))
        {
            position.y -= 0.003f * delta * multiplier;
        }

        if (Mouse.isGrabbed())
        {
            if(Mouse.isButtonDown(GL_ONE))
            {
                Mouse.setGrabbed(false);
            }
            final float MAX_LOOK_UP = 90;
            final float MAX_LOOK_DOWN = -100;
            float mouseDX = Mouse.getDX() * 0.16f;
            float mouseDY = Mouse.getDY() * 0.16f;
            if (yaw + mouseDX >= 360) {
                yaw = yaw + mouseDX - 360;
            } else if (yaw + mouseDX < 0) {
                yaw = 360 - yaw + mouseDX;
            } else {
                yaw += mouseDX;
            }
            if (pitch - mouseDY >= MAX_LOOK_DOWN
                    && pitch - mouseDY <= MAX_LOOK_UP) {
                pitch += -mouseDY;
            } else if (pitch - mouseDY < MAX_LOOK_DOWN) {
                pitch = MAX_LOOK_DOWN;
            } else if (pitch - mouseDY > MAX_LOOK_UP) {
                pitch = MAX_LOOK_UP;
            }
        }
        else
        {
            if(Mouse.isButtonDown(GL_ONE))
            {
                Mouse.setGrabbed(true);
            }
        }
    }
    
    private void move(float dx, float dy, float dz)
    {
        position.x -= dx * (float) sin(toRadians(yaw - 90)) + dz * sin(toRadians(yaw));
        position.z += dx * (float) cos(toRadians(yaw - 90)) + dz * cos(toRadians(yaw));
        
        position.y += dy * (float) sin(toRadians(pitch - 90)) + dz * sin(toRadians(pitch));
    }

    public void cameraView()
    {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
    }

    void cleanUp()
    {
        Keyboard.destroy();
        Mouse.destroy();
    }

}

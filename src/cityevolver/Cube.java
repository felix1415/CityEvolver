package cityevolver;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Cube
{
    private float y;
    private float z;
    private boolean road;
    private float x;

    public Cube(float x, float y, float z, boolean road)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.road = road;        
    }
    
    public void draw()
    {
        glBegin(GL_QUADS);
        {
//FrontFace
            
            if(road)
            {
                glColor3f(0.5f, 0.5f, 0.5f);
            }
            else
            {
                glColor3f(0f, 1f, 0f);
            }
            glVertex3f(x, y, 1 + z);
            glVertex3f(1 + x, y, 1 + z);
            glVertex3f(1 + x, 1 + y, 1 + z);
            glVertex3f(x, 1 + y, 1 + z);

//BackFace
            glVertex3f(x, y, z);
            glVertex3f(x, 1 + y, z);
            glVertex3f(1 + x, 1 + y, z);
            glVertex3f(1 + x, y, z);

//BottomFace
            glVertex3f(x, y, z);
            glVertex3f(x, y, 1 + z);
            glVertex3f(x, 1 + y, 1 + z);
            glVertex3f(x, 1 + y, z);

//TopFace
            glVertex3f(1 + x, y, z);
            glVertex3f(1 + x, y, 1 + z);
            glVertex3f(1 + x, 1 + y, 1 + z);
            glVertex3f(1 + x, 1 + y, z);

//LeftFace
            glVertex3f(x, y, z);
            glVertex3f(1 + x, y, z);
            glVertex3f(1 + x, y, 1 + z);
            glVertex3f(x, y, 1 + z);

//Right Face
            glVertex3f(x, 1 + y, z);
            glVertex3f(1 + x, 1 + y, z);
            glVertex3f(1 + x, 1 + y, 1 + z);
            glVertex3f(x, 1 + y, 1 + z);
        }
        glEnd();
    }
}

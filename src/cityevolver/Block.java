package cityevolver;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Block
{
    private int y;
    private int z;
    private byte type;
    private int x;
    
    public enum BlockType {
        AIR, IMMOVABLE, WATER, ROAD, GRASS, 
        LIGHTRESIDENTIAL, DENSERESIDENTIAL, 
        LIGHTCOMMERCIAL, DENSECOMMERCIAL, 
        FARMLAND, INDUSTRY, HOSPTIAL, 
        POLICE, FIRE, EDUCATION
    }

    public Block(int x, int y, int z, byte type)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        
    }

    public byte getType()
    {
        return type;
    }

    public boolean isRoad()
    {
        return type == 1;
    }
    
    public void draw()
    {
        glBegin(GL_QUADS);
        {

            
            if(isRoad())
            {
                glColor3f(0.5f, 0.5f, 0.5f);
            }
            else
            {
                glColor3f(0f, 1f, 0f);
            }
            
            //FrontFace
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

    public void print()
    {
        System.out.println("x" + x + " y" + y + " z" + z);
    }
}

package cityevolver;

public class Block
{
    private final int y;
    private final int z;
    private final BlockType type;
    private final int x;
    private float [] vertexData;
    private float [] colourData;

    public Block(int x, int y, int z, BlockType type)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = BlockType.WATER;
    }

    public BlockType getType()
    {
        return type;
    }

    public boolean isRoad()
    {
        return type.ordinal() == 1;
    }
    
    public int getNumberOfVertices()
    {
        return vertexData.length;
    }
    
    public float [] getVertexData()
    {
        return vertexData;
    }
    
    public float [] getColourData()
    {
        return colourData;
    }
    
    public void calculateBuffers()
    {
        if(this.getType() == BlockType.AIR)
        {
            return;
        }
        
        if(this.getType() == BlockType.WATER || 
            this.getType() == BlockType.ROAD ||
            this.getType() == BlockType.GRASS) 
        {
            vertexData = new float[]
            {   
                //bottom face
                x, y, z,
                x, y, 1 + z,
                1 + x, y, z,
                1 + x, y, z,
                1 + x, y, 1 + z,
                x, y, z + 1
            };
            
            
//                bottom face
//                x, y, z,
//                x, y, 1 + z,
//                1 + x, y, z,
//                1 + x, y, z,
//                1 + x, y, 1 + z,
//                x, y, z + 1
            
//top face
//x, 1 + y, z,
//                x, 1 + y, 1 + z,
//                1 + x, 1 + y, z,
//                1 + x, 1 + y, z,
//                1 + x, 1 + y, 1 + z,
//                x, 1 + y, z + 1

//            left side
//            x, y, z,
//            x, y, 1 + z,
//            x, 1 + y, z,
//            x, 1 + y, z,
//            x, y + 1, z +1,
//            x, y, z + 1

//              right face
//                1 + x, y, z,
//                1 + x, y, 1 + z,
//                1 + x, 1 + y, z,
//                1 + x, 1 + y, z,
//                1 + x, y + 1, z +1,
//                1 + x, y, z + 1

//front face
//x, y, z,
//                1 + x, y, z,
//                x, 1 + y, z,
//                x, 1 + y, z,
//                1 + x, y + 1, z,
//                1 + x, y, z

//back face
//x, y, 1 + z,
//                1 + x, y, 1 + z,
//                x, 1 + y, 1 + z,
//                x, 1 + y, 1 + z,
//                1 + x, y + 1, 1 + z,
//                1 + x, y, 1 + z
            
            colourData = getColour(this.getType());
            return;
        }            
    }
    
    public float [] getColour(BlockType type)
    {
        switch(type)
        {
            case WATER:
                return new float[]
                {
                    0f, 1f, 1f,
                    0f, 1f, 1f,
                    0f, 1f, 1f,
                    0f, 1f, 1f,
                    0f, 1f, 1f,
                    0f, 1f, 1f,
                };
            case ROAD:
                return new float[]
                {
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                };
        }
        return null;
    }

    public void print()
    {
        System.out.println("x" + x + " y" + y + " z" + z);
    }
}

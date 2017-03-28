package cityevolver;

import static cityevolver.Utils.concatenateFloatArrays;

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
        this.type = type;
//        System.out.println("cityevolver.Block.<init>() " + type.name());
    }

    public BlockType getType()
    {
        return type;
    }

    public boolean isRoad()
    {
        return type == BlockType.ROAD;
    }
    
    public boolean isGrass()
    {
        return type == BlockType.GRASS;
    }
    
    public boolean isWater()
    {
        return type == BlockType.WATER;
    }
     
    public boolean isAir()
    {
        return type == BlockType.AIR;
    }
    
    public boolean isLResidential()
    {
        return type == BlockType.LIGHTRESIDENTIAL;
    }
    
    public boolean isDResidential()
    {
        return type == BlockType.DENSERESIDENTIAL;
    }
    
    public boolean isLCommercial()
    {
        return type == BlockType.LIGHTCOMMERCIAL;
    }
    
    public boolean isDCommercial()
    {
        return type == BlockType.DENSECOMMERCIAL;
    }
    
    public boolean isFarmland()
    {
        return type == BlockType.FARMLAND;
    }
    
    public boolean isIndustry()
    {
        return type == BlockType.INDUSTRY;
    }
    
    public boolean isHospital()
    {
        return type == BlockType.HOSPTIAL;
    }
    
    public boolean isPolice()
    {
        return type == BlockType.POLICE;
    }
    
    public boolean isFire()
    {
        return type == BlockType.FIRE;
    }
    
    public boolean isEducation()
    {
        return type == BlockType.EDUCATION;
    }
    
    public int getNumberOfVertices()
    {
        if(vertexData != null)
        {
            return vertexData.length;
        }
        else
        {
            return 0;
        }
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
        vertexData = null;
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
        }
        else
        {
            vertexData = new float[]
            {   
                //bottom face
                x, y, z,
                x, y, 1 + z,
                1 + x, y, z,
                1 + x, y, z,
                1 + x, y, 1 + z,
                x, y, z + 1,
                
                //top face
                x, 1 + y, z,
                x, 1 + y, 1 + z,
                1 + x, 1 + y, z,
                1 + x, 1 + y, z,
                1 + x, 1 + y, 1 + z,
                x, 1 + y, z + 1,
                
                //left side
                x, y, z,
                x, y, 1 + z,
                x, 1 + y, z,
                x, 1 + y, z,
                x, y + 1, z +1,
                x, y, z + 1,
                
                //right face
                1 + x, y, z,
                1 + x, y, 1 + z,
                1 + x, 1 + y, z,
                1 + x, 1 + y, z,
                1 + x, y + 1, z +1,
                1 + x, y, z + 1,
                
                //front face
                x, y, z,
                1 + x, y, z,
                x, 1 + y, z,
                x, 1 + y, z,
                1 + x, y + 1, z,
                1 + x, y, z,
                
                //back face
                x, y, 1 + z,
                1 + x, y, 1 + z,
                x, 1 + y, 1 + z,
                x, 1 + y, 1 + z,
                1 + x, y + 1, 1 + z,
                1 + x, y, 1 + z
            };
        }   
        colourData = getColour(this.getType());
    }
    
    public float [] getColour(BlockType type)
    {
        float [] colour = null;
        switch(type)
        {
            case IMMOVABLE:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0f, 0f, 0f} );
                }
                break;
                
            case WATER:
                for (int i = 0; i < 6; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0f, 0.756f, 0.933f} );
                }
                break;
                
            case ROAD:
                for (int i = 0; i < 6; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0.5f, 0.5f, 0.5f} );
                }
                break;
                
            case GRASS:
                for (int i = 0; i < 6; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0.063f, 0.82f, 0f} );
                }
                break;
                
            case LIGHTRESIDENTIAL:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0.541f, 1f, 0.784f} );
                }
                break;
                
            case DENSERESIDENTIAL:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0f, 0.39f, 0f} );
                }
                break;
                
            case LIGHTCOMMERCIAL:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0f, 1f, 1f} );
                }
                break;
                
            case DENSECOMMERCIAL:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0f, 0f, 1f} );
                }
                break;
                
            case FARMLAND:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {1f, 0.925f, 0.545f} );
                }
                break;
                
            case INDUSTRY:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {1f, 0.79f, 0f} );
                }
                break;
                
            case HOSPTIAL:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {1f, 1f, 1f} );
                }
                break;
                
            case POLICE:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0.749f, 0.373f, 1f} );
                }
                break;
                
            case FIRE:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {1f, 0f, 0.2f} );
                }
                break;
                
            case EDUCATION:
                for (int i = 0; i < 36; i++)
                {
                    colour = concatenateFloatArrays(colour, new float [] {0.396f, 0.263f, 0.129f} );
                }
                break;
                
        }
        return colour;
    }

    public void print()
    {
        System.out.println("x" + x + " y" + y + " z" + z);
    }

    public boolean isLowestLevel()
    {
        return this.y == 0;
    }
    
    public boolean isJoinableBlock()
    {
        return this.type != BlockType.AIR 
                && this.type != BlockType.WATER
                && this.type != BlockType.GRASS
                && this.type != BlockType.ROAD;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public int getX()
    {
        return x;
    }

    public boolean isType(BlockType type)
    {
        return type.equals(this.type);
    }

    @Override
    public String toString()
    {
        return "Block{" + "y=" + y + ", z=" + z + ", type=" + type + ", x=" + x + '}';
    }
    
    
    
    
    
    
}

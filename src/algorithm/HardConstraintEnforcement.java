/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import cityevolver.Block;
import cityevolver.BlockType;
import cityevolver.Utils;
import static cityevolver.Utils.getRandomBlock;
import java.util.ArrayList;

/**
 *
 * @author alexgray
 */
public class HardConstraintEnforcement
{
    private Block[][][] gene;
    private int xLength;
    private int yLength;
    private int zLength;
    
    private static HardConstraintEnforcement instance = null;
    
    public static HardConstraintEnforcement getInstance() 
    {
       if(instance == null) 
       {
          instance = new HardConstraintEnforcement();
       }
       return instance;
    }
    
    public Block[][][] setValue(int xLength, int yLength, int zLength, 
            Block[][][] geneIn, ArrayList<BlockType> blocksForSearch)
    {
        this.gene = geneIn;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
        return this.gene;
    }
    
    public Block[][][] applyConstraints(int xLength, int yLength, int zLength, 
            Block[][][] geneIn, ArrayList<BlockType> blocksForSearch)
    {
        this.gene = geneIn;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
        
        //note the constraints are applied to the first 
        //level first, incrementaly up (y is the outer loop)
        for (int y = 0; y < this.yLength; y++)
        {
            for (int x = 0; x < this.xLength; x++)
            {
                for (int z = 0; z < this.zLength; z++)
                {
                    //if not the lowest level of the map
                    if(!isLowestLevel(x, y, z))
                    {
                        if(blockIsIllegalAboveLevelZero(x, y, z))
                        {
                            //ensure a legal block is placed in the right place.
                            boolean isLegal = false;
                            while(!isLegal)
                            {
                                this.gene[x][y][z] = new Block(x, y, z, getRandomBlock(blocksForSearch));
                                isLegal = !blockIsIllegalAboveLevelZero(x, y, z);
                            }     
                        }
                        //if it doesn't have a block below
                        if(!hasJoinableBlockBelow(x, y, z))
                        { 
                            //if it's connected to 2 or more blocks that have 
                            //a legal base it becomes air
                            if(!isAdjacentlyConnectedToLegalBlocks(x, y, z))
                            {
                                this.gene[x][y][z] = new Block(x, y, z, BlockType.AIR);
                            }
                        }
                    }
                    else
                    {
                        if(isBlockAir(x, y, z))
                        {
                            boolean isLegal = false;
                            while(!isLegal)
                            {
                                this.gene[x][y][z] = new Block(x, y, z, getRandomBlock(blocksForSearch));
                                isLegal = !isBlockAir(x, y, z);
                            } 
                        }
                    }
                }
            }
        }
        return this.gene;
    }
    
    public boolean isLowestLevel(int x, int y, int z)
    {
        return y == 0;
    }
    
    public ArrayList<Block> getRoads()
    {
        ArrayList<Block> roads = new ArrayList<>();
        for (int x = 0; x < this.xLength; x++)
        {
            for (int z = 0; z < this.zLength; z++)
            {
                if(this.gene[x][0][z].isRoad())
                {
                    roads.add(this.gene[x][0][z]);
                }
            }
        }
        return roads;
    }
    
    public boolean isNextToType(int x, int y, int z, BlockType type)
    {
        return adjacentToBlockOfType(x, y, z, type) > 0;
    }
    
    public int isAllRoadsConnectedFitness()
    {
        ArrayList<Block> roads = getRoads();
        ArrayList<Block> queue = new ArrayList<>();
        ArrayList<Block> visited = new ArrayList<>();
        
        if(roads.isEmpty())
        {
            return 0;
        }
        
        int connectionFitness = 0;
        for (Block block : roads)
        {
            queue.add(block);
            while(!queue.isEmpty()) {
                    Block node = queue.remove(0);
                    ArrayList<Block> unvisited = getUnvisitedChildrenNode(node, visited);
                    visited.add(node);

                    if(!unvisited.isEmpty())
                    {
                        queue.addAll(unvisited);
                    }
            }
            connectionFitness += visited.size();
            if(roads.size() == visited.size())
            {
                return roads.size() * 2;
            }
        }
        
        return Utils.roundFloat((float)connectionFitness / (float)roads.size());
    }
    
    private ArrayList<Block> getUnvisitedChildrenNode(Block node, ArrayList<Block> visited)
    {
        ArrayList<Block> blocks = getRoadConnections(node);
        ArrayList<Block> unvisited = new ArrayList<>();
        for (Block vBlock : visited)
        {
            if(isSameBlock(node, vBlock))
            {
                //this blocks children are already in the list
                return unvisited;
            }
        }
        
        for (Block block : blocks)
        {
            boolean visitedBlock = false;
            for (Block vBlock : visited)
            {
                if(isSameBlock(block, vBlock))
                {
                    visitedBlock = true;
                }
            }
            if(!visitedBlock)
            {
                unvisited.add(block);
            }
        }        
        return unvisited;
    }
    
    private ArrayList<Block> getRoadConnections(Block node)
    {
        ArrayList<Block> blocks = new ArrayList<>();
        
        if(node.getX() + 1 < xLength)
        {
            if(this.gene[node.getX() + 1][node.getY()][node.getZ()].isRoad())
            {
                blocks.add(new Block(node.getX() + 1, node.getY(), node.getZ(), node.getType()));
            }
        }
        if(node.getZ() + 1 < zLength)
        {
            if(this.gene[node.getX()][node.getY()][node.getZ() + 1].isRoad())
            {
                blocks.add(new Block(node.getX(), node.getY(), node.getZ() + 1, node.getType()));
            }
        }
        if(node.getX() != 0)
        {
            if(this.gene[node.getX() - 1][node.getY()][node.getZ()].isRoad())
            {
                blocks.add(new Block(node.getX() - 1, node.getY(), node.getZ(), node.getType()));
            }
        }
        if(node.getZ() != 0)
        {                        
            if(this.gene[node.getX()][node.getY()][node.getZ() - 1].isRoad())
            {
                blocks.add(new Block(node.getX(), node.getY(), node.getZ() - 1, node.getType()));
            }
        }
        return blocks;
    }
    
    private boolean isSameBlock(Block block1, Block block2)
    {
        return block1.getX() == block2.getX() 
                && block1.getY() == block2.getY()
                && block1.getZ() == block2.getZ();
    }
    
    private boolean isRoad(int x, int y, int z)
    {
        return this.gene[x][y][z].isRoad();
    }
    
    private boolean hasJoinableBlockBelow(int x, int y, int z)
    {
        if(y != 0)
        {
            if(this.gene[x][y - 1][z].isJoinableBlock())
            {
                return true;
            }
        }
        else
        {
            // the ground is legal
            return true;
        }
        return false;
    }
    
    private boolean blockIsIllegalAboveLevelZero(int x, int y, int z)
    {
        return this.gene[x][y][z].isRoad() 
                || this.gene[x][y][z].isWater();
    }
    
    private boolean isBlockAir(int x, int y, int z)
    {
        return this.gene[x][y][z].isAir();
    }
    
    private boolean isAdjacentlyConnectedToLegalBlocks(int x, int y, int z)
    {
        int numberOfAdjacentLegalBlocks = 0;
        if(x + 1 < this.xLength)
        {
            if(this.gene[x + 1][y][z].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x + 1, y, z))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        if(z + 1 < zLength)
        {
            if(this.gene[x][y][z + 1].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x, y, z + 1))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        else if(x != 0)
        {
            if(this.gene[x - 1][y][z].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x - 1, y, z))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        else if(z != 0)
        {                        
            if(this.gene[x][y][z - 1].isJoinableBlock())
            {
                if(hasJoinableBlockBelow(x, y, z - 1))
                {
                    numberOfAdjacentLegalBlocks++;
                }
            }
        }
        
        return numberOfAdjacentLegalBlocks >= 2;
    }
    
    public static boolean groupingRoadBlocksOnThisBlock(int x, int y, int z)
    {
        return false; 
    }
    
    private boolean withinTwoBlocksOfRoad(int x, int y, int z)
    {
        //one block away
        if((x + 1 < xLength) && (this.gene[x + 1][y][z].isRoad()))
        {
            return true;
        }
        if((z + 1 < zLength) && (this.gene[x][y][z + 1].isRoad()))
        {
            return true;
        }
        if((x != 0) && (this.gene[x - 1][y][z].isRoad()))
        {
            return true;
        }
        if((z != 0) && this.gene[x][y][z - 1].isRoad())
        {                        
            return true;
        }
        
        //two blocks aways
        if((x + 2 < xLength) && (this.gene[x + 2][y][z].isRoad()))
        {
            return true;
        }
        if((z + 2 < zLength) && (this.gene[x][y][z + 2].isRoad()))
        {
            return true;
        }
        if((x > 1) && (this.gene[x - 2][y][z].isRoad()))
        {
            return true;
        }
        if((z > 1) && this.gene[x][y][z - 2].isRoad())
        {                        
            return true;
        }
        
        //diagonal
        if((x + 1 < xLength) && (z + 1 < zLength) && (this.gene[x + 1][y][z + 1].isRoad()))
        {
            return true;
        }
        if((z + 1 < zLength) && (x != 0) && (this.gene[x - 1][y][z + 1].isRoad()))
        {
            return true;
        }
        if((z != 0) && (x + 1 < xLength) && this.gene[x + 1][y][z - 1].isRoad())
        {
            return true;
        }
        if((z != 0) && (x != 0) && (this.gene[x - 1][y][z - 1].isRoad()))
        {
            return true;
        }
        
        return false;
    }
    
    public int adjacentToBlockOfType(int x, int y, int z, BlockType type)
    {
        int numberOfBlocks = 0;
        //if adjacent to any residential
        if(x + 1 < xLength)
        {
            if(this.gene[x + 1][y][z].isType(type))
            {
                ++numberOfBlocks;
            }
        }
        if(z + 1 < zLength)
        {
            if(this.gene[x][y][z + 1].isType(type))
            {
                ++numberOfBlocks;
            }
        }
        if(x != 0)
        {
            if(this.gene[x - 1][y][z].isType(type))
            {
                ++numberOfBlocks;
            }
        }
        if(z != 0)
        {                        
            if(this.gene[x][y][z - 1].isType(type))
            {
                ++numberOfBlocks;
            }
        }
        if(y + 1 < yLength)
        {
            if(this.gene[x][y + 1][z].isType(type))
            {
                ++numberOfBlocks;
            }
        }
        if(y != 0)
        {
            if(this.gene[x][y - 1][z].isType(type))
            {
                ++numberOfBlocks;
            }
        }
        return numberOfBlocks;
    }
    
    public int nextToBlockOfType(int x, int y, int z, BlockType type)
    {
        int numberOfBlocks = adjacentToBlockOfType(x, y, z, type);
        //if adjacent to any residential
        
        if((x + 1 < xLength) && (z + 1 < zLength) && (this.gene[x + 1][y][z + 1].isType(type)))
        {
            ++numberOfBlocks;
        }
        if((z + 1 < zLength) && (x != 0) && (this.gene[x - 1][y][z + 1].isType(type)))
        {
            ++numberOfBlocks;
        }
        if((z != 0) && (x + 1 < xLength) && this.gene[x + 1][y][z - 1].isType(type))
        {
            ++numberOfBlocks;
        }
        if((z != 0) && (x != 0) && (this.gene[x - 1][y][z - 1].isType(type)))
        {
            ++numberOfBlocks;
        }
        return numberOfBlocks;
    }
    
    public int lightResidentialFitness(int x, int y, int z)
    {
        int lightResidentialFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isLResidential())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++lightResidentialFitness;
                }
            }
            lightResidentialFitness += adjacentToBlockOfType(x, y, z, BlockType.LIGHTRESIDENTIAL);
//            lightResidentialFitness += adjacentToBlockOfType(x, y, z, BlockType.DENSERESIDENTIAL);              
        }
        return lightResidentialFitness;
    }
    
    public int denseResidentialFitness(int x, int y, int z)
    {
        int denseResidentialFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isDResidential())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++denseResidentialFitness;
                }
            }
            denseResidentialFitness += adjacentToBlockOfType(x, y, z, BlockType.DENSERESIDENTIAL);
//            denseResidentialFitness += adjacentToBlockOfType(x, y, z, BlockType.LIGHTRESIDENTIAL);   
            
        }
        return denseResidentialFitness;
    }
    
    public int lightCommercialFitness(int x, int y, int z)
    {
        int lightCommercialFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isLCommercial())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++lightCommercialFitness;
                }
            }
            lightCommercialFitness += adjacentToBlockOfType(x, y, z, BlockType.LIGHTCOMMERCIAL);            
        }
        return lightCommercialFitness;
    }
    
    public int denseCommercialFitness(int x, int y, int z)
    {
        int denseCommercialFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isDCommercial())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++denseCommercialFitness;
                }
            }
            denseCommercialFitness += adjacentToBlockOfType(x, y, z, BlockType.DENSECOMMERCIAL);
            
        }
        return denseCommercialFitness;
    }
    
    public int farmlandFitness(int x, int y, int z)
    {
        int farmlandFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isFarmland())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++farmlandFitness;
                }
            }
            
            farmlandFitness += adjacentToBlockOfType(x, y, z, BlockType.FARMLAND);
            
        }
        return farmlandFitness;
    }
    
    public int industryFitness(int x, int y, int z)
    {
        int industryFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isIndustry())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++industryFitness;
                }
            }
            industryFitness += adjacentToBlockOfType(x, y, z, BlockType.INDUSTRY);
        }
        return industryFitness;
    }
    
    public int hospitalFitness(int x, int y, int z)
    {
        int hospitalFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isHospital())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++hospitalFitness;
                }
            }
            int value;
            if((value = adjacentToBlockOfType(x, y, z, BlockType.HOSPTIAL)) == 0)
            {
                hospitalFitness -= 2;
            }
            else
            {
                hospitalFitness += value;
            } 
            
            //deductions
            hospitalFitness -= nextToBlockOfType(x, y, z, BlockType.INDUSTRY);
            hospitalFitness -= nextToBlockOfType(x, y, z, BlockType.FARMLAND);
            
        }
        return hospitalFitness;
    }
    
    public int policeFitness(int x, int y, int z)
    {
        int policeFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isPolice())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++policeFitness;
                }
            }
            int value;
            if((value = adjacentToBlockOfType(x, y, z, BlockType.POLICE)) == 0)
            {
                policeFitness -= 2;
            }
            else
            {
                policeFitness += value;
            }              
        }
        return policeFitness;
    }
    
    public int fireFitness(int x, int y, int z)
    {
        int fireFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isFire())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++fireFitness;
                }
            }
            int value = 0;
            if((value = adjacentToBlockOfType(x, y, z, BlockType.FIRE)) == 0)
            {
                fireFitness -= 2;
            }
            else
            {
                fireFitness += value;
            }          
        }
        return fireFitness;
    }
    
    public int educationFitness(int x, int y, int z)
    {
        int educationFitness = 1; // one for being a road block
        if(this.gene[x][y][z].isEducation())
        {
            if(this.gene[x][y][z].isLowestLevel())
            {
                if(withinTwoBlocksOfRoad(x,y,z))
                {
                    ++educationFitness;
                }
            }
            int value = 0;
            if((value = adjacentToBlockOfType(x, y, z, BlockType.EDUCATION)) == 0)
            {
                educationFitness -= 2;
            }
            else
            {
                educationFitness += value;
            }
            
            //deductions
            educationFitness -= nextToBlockOfType(x, y, z, BlockType.INDUSTRY);
            educationFitness -= nextToBlockOfType(x, y, z, BlockType.FARMLAND);
            
        }
        return educationFitness;
    }
    
    public int roadFitness(int x, int y, int z)
    {
        int roadFitness = 0; // one for being a road block
        if(this.gene[x][y][z].isRoad())
        {
            roadFitness += adjacentToBlockOfType(x, y, z, BlockType.ROAD);
            
            //if road is diagonal and has no connection
            if((x + 1 < xLength) && (z + 1 < zLength))
            {
                if(this.gene[x + 1][y][z + 1].isRoad())
                {
                    if(!this.gene[x + 1][y][z].isRoad() || !this.gene[x][y][z + 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            if((z + 1 < zLength) && (x != 0))
            {
                if(this.gene[x - 1][y][z + 1].isRoad())
                {
                    if(!this.gene[x - 1][y][z].isRoad() || !this.gene[x][y][z + 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            if((z != 0) && (x + 1 < xLength))
            {
                if(this.gene[x + 1][y][z - 1].isRoad())
                {
                    if(!this.gene[x + 1][y][z].isRoad() || !this.gene[x][y][z - 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            if((z != 0) && (x != 0))
            {    
                if(this.gene[x - 1][y][z - 1].isRoad())
                {
                    if(!this.gene[x - 1][y][z].isRoad() || !this.gene[x][y][z - 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            
            //if road is diagonal and has two connections to that block
            if((x + 1 < xLength) && (z + 1 < zLength))
            {
                if(this.gene[x + 1][y][z + 1].isRoad())
                {
                    if(this.gene[x + 1][y][z].isRoad() && this.gene[x][y][z + 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            if((z + 1 < zLength) && (x != 0))
            {
                if(this.gene[x - 1][y][z + 1].isRoad())
                {
                    if(this.gene[x - 1][y][z].isRoad() && this.gene[x][y][z + 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            if((z != 0) && (x + 1 < xLength))
            {
                if(this.gene[x + 1][y][z - 1].isRoad())
                {
                    if(this.gene[x + 1][y][z].isRoad() && this.gene[x][y][z - 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            if((z != 0) && (x != 0))
            {    
                if(this.gene[x - 1][y][z - 1].isRoad())
                {
                    if(this.gene[x - 1][y][z].isRoad() && this.gene[x][y][z - 1].isRoad())
                    {
                        roadFitness -= 2;
                    }
                }
            }
            
            //surrounded
            if((z != 0) && (x != 0) && (x + 1 < xLength) && (z + 1 < zLength))
            {
                if(this.gene[x + 1][y][z + 1].isRoad() 
                        && this.gene[x - 1][y][z - 1].isRoad()
                        && this.gene[x + 1][y][z - 1].isRoad()
                        && this.gene[x - 1][y][z + 1].isRoad()
                        && this.gene[x][y][z + 1].isRoad() 
                        && this.gene[x][y][z - 1].isRoad()
                        && this.gene[x + 1][y][z].isRoad()
                        && this.gene[x - 1][y][z].isRoad())
                {
                    roadFitness -= 8;
                }
            }
        }
        return roadFitness;
    }
}



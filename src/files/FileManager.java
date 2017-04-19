/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import algorithm.Individual;
import algorithm.Population;
import cityevolver.Block;
import cityevolver.BlockType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexgray
 */
public class FileManager
{
    
    private static FileManager instance = null;
    
    public static FileManager getInstance() 
    {
       if(instance == null) 
       {
          instance = new FileManager();
       }
       return instance;
    }
    
    private final String resourceLocation = System.getProperty("user.dir") + "/resource/";
    
    
    protected FileManager()
    {
        new File(resourceLocation).mkdirs();
    }
    
    public void test()
    {
        System.out.println("files.FileManager.test() " + System.getProperty("user.dir"));
    }
    
    public String[] getMapsFromDisk()
    {
        File folder = new File(resourceLocation);
        File[] rawListOfFiles = folder.listFiles();
        ArrayList<String> listOfFiles = new ArrayList<>();
        
        for (File rawFile : rawListOfFiles)
        {
            String extension = "";
            int i = rawFile.getName().lastIndexOf('.');
            if (i > 0) {
                extension = rawFile.getName().substring(i+1);
            }
            
            if(extension.equals("ceo"))
            {
                listOfFiles.add(rawFile.getName());
            }
        }
        
        return listOfFiles.toArray(new String[0]);
    }
    
    public Population loadCES(String location)
    {
        return null;
    }
    
    public Individual loadSHP(String location)
    {
        return null;
    }
    
    public Individual loadCEO(String name)
    {
        String fileName = resourceLocation + name;
        File file = new File(fileName);
        Individual newIndividual = null;
        try
        {
            Scanner fileIn = new Scanner(file);
            String [] header = fileIn.nextLine().split(",");
            
            int x = Integer.valueOf(header[0]);
            int y = Integer.valueOf(header[1]);
            int z = Integer.valueOf(header[2]);
            Block[][][] blocks = new Block[x][y][z];
            String [] body = null;
            
            while(fileIn.hasNextLine())
            {
                String line = fileIn.nextLine();
                body = line.split(",");
                
                x = Integer.valueOf(body[0]);
                y = Integer.valueOf(body[1]);
                z = Integer.valueOf(body[2]);
                
                blocks[x][y][z] = new Block(Integer.valueOf(body[0]),
                                          Integer.valueOf(body[1]),
                                          Integer.valueOf(body[2]), 
                                          BlockType.values()[Integer.valueOf(body[3])]);
            }
            x = Integer.valueOf(header[0]);
            y = Integer.valueOf(header[1]);
            z = Integer.valueOf(header[2]);
            return new Individual(x, y, z, blocks);
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean saveCEO(Individual in, String name)
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(resourceLocation + name ), "utf-8"))) {
            //header
            writer.write(in.getXLength() + "," 
                    + in.getYLength() + "," 
                    + in.getZLength() + "\n");
            
            //body
            for (int x = 0; x < in.getXLength(); x++)
            {
                for (int y = 0; y < in.getYLength(); y++)
                {
                    for (int z = 0; z < in.getZLength(); z++)
                    {
                        BlockType type = in.getGene(x,y,z).getType();
                        //if it's air, don't save it
                        if(type == BlockType.AIR)
                        {
                            continue;
                        }
                        writer.write(x + "," 
                                    + y + "," 
                                    + z + "," 
                                    + (byte)type.ordinal() + 
                                    "\n");
                    }
                }
            }
            
        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean saveSHP(Individual in, String name)
    {
        return false;
    }
    
    public boolean saveCES(Population searchSession, String name)
    {
        return false;
    }
    
    public boolean removeMapFromDisk(String name)
    {
        return false;
    }
    
    public boolean removeSessionFromDisk(String name)
    {
        return false;
    }
    
    public ArrayList<String> getMapNamesFromDisk()
    {
        return null;
    }
    
    public ArrayList<String> getSessionNamesFromDisk()
    {
        return null;
    }

    public boolean doesFileExist(String name)
    {
        String fileName = resourceLocation + name;
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import algorithm.Individual;
import algorithm.Population;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
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
    
    public Individual loadCES(String location)
    {
        return null;
    }
    
    public Individual loadSHP(String location)
    {
        return null;
    }
    
    public Population loadCEO(String location)
    {
        return null;
    }
    
    public boolean saveCEO(Individual in, String name)
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(resourceLocation + name ), "utf-8"))) {
            //header
            writer.write(in.getXLength() + ",");
            writer.write(in.getYLength() + ",");
            writer.write(in.getZLength() + ",");
            writer.write("\n");           
            
            
            //body
            for (int x = 0; x < in.getXLength(); x++)
            {
                for (int y = 0; y < in.getYLength(); y++)
                {
                    for (int z = 0; z < in.getZLength(); z++)
                    {
                        writer.write(x + ",");
                        writer.write(y + ",");
                        writer.write(z + ",");
                        writer.write((byte)in.getGene(x,y,z).getType().ordinal());
                        
                        writer.write("\n");    
                    }
                }
            }
            
        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
    
    
}

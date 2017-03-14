/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityevolver;

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
    
    protected FileManager() 
    {
    }
}

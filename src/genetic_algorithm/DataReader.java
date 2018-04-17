package genetic_algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class DataReader {
    String fileName;
    String line;
    ArrayList <String>lines = new ArrayList<String>();
    
    HashMap <String, ArrayList<Integer>>data = new HashMap<String, ArrayList<Integer>>();
    public DataReader(String FileName)
    {
        this.fileName=FileName;
    }
    
    public ArrayList readRunsFile(String separator) {
        ArrayList<HashMap<String, Object>> runs = new  ArrayList<HashMap<String, Object>>();
        try {
            //storeValues.clear();//just in case this is the second call of the ReadFile Method./
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String[] options = null;
            StringTokenizer columnsToken = null;
            int lineNumber = 0, tokenNumber = 0;
            int columnCount = 0;
            
            while( (line = br.readLine()) != null)
            {                               
                lines.add(line);
                //break comma separated line using ","
                options = line.split(separator);
                HashMap<String, Object> run = new HashMap<String, Object>();
                
                for(int i = 0; i< options.length; i++){                                     
                    Object[] pairs = options[i].split(" : ");
                    String key = (String) pairs[0];
                    key = key.trim();
                    //Log.debugMsg(key + "-" + (String)pairs[1]);
                    run.put(key, pairs[1].toString().trim());
                }                
                runs.add(run);
                //reset token number
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return runs;
    }
    
    public boolean readFile( String separator)
    {
        try {
            //storeValues.clear();//just in case this is the second call of the ReadFile Method./
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String[] d = null;
            StringTokenizer columnsToken = null;
            int lineNumber = 0, tokenNumber = 0;
            int columnCount = 0;
            
            while( (line = br.readLine()) != null)
            {                
                
                lines.add(line);
                //break comma separated line using ","
                d = line.split(separator);
                ArrayList<Integer> lineData = new ArrayList<Integer>();
                int counter = 0;      
                String id = d[0];
                for(int i = 1; i< d.length; i++){                                     
                    lineData.add(Integer.parseInt(d[i]));
                    counter++;
                }
                data.put(id, lineData);
                //reset token number
                
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Could not find " + fileName + " in run directory.\nEnding.");
            System.exit(1);
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
    //mutators and accesors
    public void setFileName(String newFileName)
    {
        this.fileName=newFileName;
    }
    public String getFileName()
    {
        return fileName;
    }
    public HashMap <String, ArrayList<Integer>> getData()
    {
        return this.data;
    }
    public void displayArrayList()
    {
       
    }
}

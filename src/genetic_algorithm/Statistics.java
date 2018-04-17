package genetic_algorithm;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.HashMap;

public class Statistics {
    HashMap<Double, Long> maxScoreGenerationCount;
    private int numberOfDataPoints;
    private double[] maxGHs;
    private double[] averageGH;
    private double[] standardDeviation;
    private int generationCount = 0;
    private static String fileName;
   
    private double maxGH;
    private static double maxGHoverall;
    private static Chromosome bestChromosome;
    
    
//    private Run run;
    private int dataPointFrequency;
    
    /**
     * Constructor
     * @param dataPointFreq how often we take data points
     */
    public Statistics(int dataPointFreq) {
        this.maxScoreGenerationCount = new HashMap<Double, Long>();
//        this.run = run;
        this.dataPointFrequency = dataPointFreq;
        numberOfDataPoints = Defines.runGenerations / dataPointFreq + 1;  //add one to account for generation 0
        maxGHs = new double[this.numberOfDataPoints];
        averageGH = new double[this.numberOfDataPoints];
        standardDeviation = new double[this.numberOfDataPoints];
        this.createFileName();
    }
    
    /**
     * This function adds a max score to a hashmap with a count of 1 or increases the number generations that max score has been present
     * @param score max score from a generation
     */
    
    public void addMaxScoreCount(double score) {
        Long scoreCount = 0l;
        if(this.maxScoreGenerationCount.containsKey(score)) {
            scoreCount = this.maxScoreGenerationCount.get(score);            
        }
        this.maxScoreGenerationCount.put(score, scoreCount + 1);
    }
    
    /**
     * This function adds data from a generation to the stats 
     * @param generation Current generation
     */
    
    public void addGeneration(Generation generation) {
        this.addMaxScore(generation);
        this.addAverageGH(generation.getAverageGH());
        generationCount++;
    }
    
    /**
     * This function takes the max score of the current generation and stores it. Also checks if current 
     * generation max score is better that overall max score.
     * @param generation Current generation
     */
    
    private void addMaxScore(Generation generation) {
        
        this.maxGHs[this.generationCount] = generation.getMaxGH();
        if(this.maxGH < generation.getMaxGH()) {
           this.maxGH = generation.getMaxGH(); 
           if(this.maxGH > Statistics.maxGHoverall) {
               Statistics.maxGHoverall = this.maxGH;
               Statistics.bestChromosome = generation.getBestChromo();
           }
          
           
        }
                
    }
    
    /**
     * This funciton adds the average GH of a generation to an array of averages
     * @param averageGH
     */
    
    private void addAverageGH(double averageGH) {
        this.averageGH[this.generationCount] = averageGH;        
    }
    
    /**
     * This function creates the output that is printed to a csv
     * @return String output
     */
    
    public String createOutput() {
        String output = "";        
        output = "number of generations, population size, crossover probability, mutation probability, parent algorithm, crossover mode, tournament size " + Statistics.newline();

        output += Defines.runGenerations + ", " + Defines.popSize + ", " + Defines.probCrossover + ", " + Defines.probMutate + "," + Defines.parentAlgo + "," + Defines.cpMode + "," + Defines.tournamentSize +  Statistics.newline();
        
        output += "Max GH" + Statistics.newline();
        output += this.maxGH + Statistics.newline();
        
        output += "GH Over Generation" + Statistics.newline();
        output += "Generations,";
        for(int i = 0; i < this.generationCount; i++) {
            output += i * this.dataPointFrequency + ",";            
        }
        output += Statistics.newline();
        output += "Max GH,";
        for(int i = 0; i < this.generationCount; i++) {
            output += this.maxGHs[i] + ",";          
        }
        output += Statistics.newline();
        output += "Avg GH,";
        for(int i = 0; i < this.generationCount; i++) {
            output += this.averageGH[i] + ",";           
        }
        output += Statistics.newline();
        output += Statistics.newline();
        
        output += "Max GH Generation Frequency";
        output += Statistics.newline();
        output += "Max GH,";
        for (double key : this.maxScoreGenerationCount.keySet()) {
           output += key + ",";
        }
        output += Statistics.newline();
        output += "Generation Count,";
        for (Long value : this.maxScoreGenerationCount.values()) {
            output += value + ",";
         }
         output += Statistics.newline();
        
       return output;
        
    }
    
    /**
     * This function creates the output for the current run and sends it off to be printed to file
     * 
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void output() throws FileNotFoundException, UnsupportedEncodingException {
        String output = this.createOutput();
        //System.out.println(this.output);
        this.outputToFile(output);
       
    }
    
    /**
     * This function writes output to file
     * @param output
     */
    
    private static void outputToFile(String output) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(Statistics.getFileName(), true)));
            writer.print(output);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * This static function creates the output for best overall data then writes to files
     */
    
    public static void outputOverallBest() {
        String output = "";
        output += "Best Overall GH" + Statistics.newline();
        output += Statistics.maxGHoverall + Statistics.newline();
        output += "Best Chromosome Sequence" + Statistics.newline();
        String outputGroupMembers = "Group Members,";
        String outputGroupGH = "Group GH,";
        String outputMaxDistace = "Max Euclidean Distance,";
        Group[] groups = Statistics.bestChromosome.getGroups();
        for(int i = 0; i < groups.length; i++) {
           Gene[] genes = groups[i].getGenes();
           for(int j = 0; j < genes.length; j++) {
               outputGroupMembers += genes[j].getStudentId() + ";";
           }
           outputGroupMembers += ",";           
           outputGroupGH += groups[i].getGH() + ",";
           outputMaxDistace += groups[i].getMaxDistance() + ",";
        }
        output += outputGroupMembers + Statistics.newline();
        output += outputGroupGH + Statistics.newline();
        output += outputMaxDistace + Statistics.newline();
        Statistics.outputToFile(output);
    }
    
    /**
     * Creates file name for the current runs
     */
    
    private static void createFileName() {
        if(Statistics.fileName == null || Statistics.fileName.isEmpty()) {
            fileName = "output_" + Timer.getTime() + ".csv";
        }
    }
    
    /**
     * Returns the file name for current output
     * @return
     */
    
    private static String getFileName() {
        if(Statistics.fileName == null) {
            Statistics.createFileName();            
        }
        return Statistics.fileName;
    }
    
    /**
     * Creates the newline character.
     * @return
     */
    
    private static String newline() {
        return "\n";
    }
    
    
}

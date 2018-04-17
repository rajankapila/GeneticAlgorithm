package genetic_algorithm;

import java.util.HashMap;

public class Run {
    private  String fileName;
    private  int numberOfElites;    
    private  int populationSize; 
    private  int maxGenerations; 
    private int dataPointFrequency;
    private  int numberOfParents; // for crossover operation
    private  int numberOfCrossoverPoints;
    private  double probabilityCrossover;
    private  double probabilityMutate;
    private  int paTournamentSize;    
    private  int parentAlgo;
    private  int cpMode;
    private double penalty;
    
    public Run(HashMap<String, Object> run) {
        this.setFileName((String)run.get("fileName"));
        this.setNumberOfElites(Integer.parseInt(run.get("numberOfElites").toString()));
        this.setPopulationSize(Integer.parseInt(run.get("populationSize").toString()));        
        this.setMaxGenerations(Integer.parseInt(run.get("maxGenerations").toString()));
        this.setDataPointFrequency(Integer.parseInt(run.get("dataPointFrequency").toString()));
        this.setNumberOfParents(Integer.parseInt(run.get("numberOfParents").toString()));
        this.setNumberOfCrossoverPoints(Integer.parseInt(run.get("numberOfCrossoverPoints").toString()));
        this.setProbabilityCrossover(Double.parseDouble(run.get("probabilityCrossover").toString()));
        this.setProbabilityMutate(Double.parseDouble(run.get("probabilityMutate").toString()));   
        this.setPaTournamentSize(Integer.parseInt(run.get("paTournamentSize").toString()));
        this.setParentAlgo(Integer.parseInt(run.get("parentAlgo").toString()));
        this.setCpMode(Integer.parseInt(run.get("cpMode").toString()));
        this.setPenalty(Double.parseDouble(run.get("penalty").toString()));  
        
    }
    
  

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumberOfElites() {
        return numberOfElites;
    }

    public void setNumberOfElites(int numberOfElites) {
        this.numberOfElites = numberOfElites;
    }

    public int getPopulationSize() {
       
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;       
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }
    

    public int getDataPointFrequency() {
        return dataPointFrequency;
    }

    public void setDataPointFrequency(int dataPointFrequency) {
        this.dataPointFrequency = dataPointFrequency;
    }

    public int getNumberOfParents() {
        return numberOfParents;
    }

    public void setNumberOfParents(int numberOfParents) {
        this.numberOfParents = numberOfParents;
    }

    public int getNumberOfCrossoverPoints() {
        return numberOfCrossoverPoints;
    }

    public void setNumberOfCrossoverPoints(int numberOfCrossoverPoints) {
        this.numberOfCrossoverPoints = numberOfCrossoverPoints;
    }

    public double getProbabilityCrossover() {
        return probabilityCrossover;
    }

    public void setProbabilityCrossover(double probabilityCrossover) {
        this.probabilityCrossover = probabilityCrossover;
    }

    public double getProbabilityMutate() {
        return probabilityMutate;
    }

    public void setProbabilityMutate(double probabilityMutate) {
        this.probabilityMutate = probabilityMutate;
    }

    public int getPaTournamentSize() {
        return paTournamentSize;
    }

    public void setPaTournamentSize(int paTournamentSize) {
        this.paTournamentSize = paTournamentSize;
    }

    public  int getParentAlgo() {
        return parentAlgo;
    }

    public  void setParentAlgo(int parentAlgo) {
        this.parentAlgo = parentAlgo;
    }

    public  int getCpMode() {
        return cpMode;
    }

    public  void setCpMode(int cpMode) {
        this.cpMode = cpMode;
    }

    /**
     * @return the penalty
     */
    public double getPenalty() {
        return penalty;
    }

    /**
     * @param penalty the penalty to set
     */
    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }
    
}

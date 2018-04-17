package genetic_algorithm;

import java.util.ArrayList;



public class Generation {
    private Population population;
    private boolean isValidFound = false;
    private double totalGH;
    private double maxGH;
    private double averageGH;
    private String output;
    private Chromosome bestChromo;
    private boolean isBestValid;
//    private Run run;
    
    
    public Generation (Population population) {
        this.isBestValid = false;
        this.bestChromo = null;
        this.population = population;
//        this.run = run;
        this.output = "";
        double localGH;
        this.totalGH = 0.0;
        this.maxGH = 0.0;
        
        this.population.sort();
        ArrayList<Chromosome> chromosomes = this.population.getChromosomes();      
        for(int i = 0; i < chromosomes.size(); i++) {
            localGH = chromosomes.get(i).getTotalGH();
            if (localGH > this.maxGH) {
                this.maxGH = localGH;
                this.bestChromo = chromosomes.get(i);
                this.isBestValid = this.bestChromo.isValid();
            }
            this.totalGH += localGH;
            
            this.output += String.format("%.2f",localGH);
            this.output += "-" + chromosomes.get(i).getNumValidGroup();
            if(chromosomes.get(i).isValid()) {
                this.output += "-VALID";
                this.isValidFound = true;              
            }
            if(i < chromosomes.size() - 1) {
                this.output += ", ";
            }            
        }
        this.averageGH = this.totalGH / this.population.getChromosomes().size();
    }
    
   public void print() {
       
       
       Log.debugMsg(this.output);
       Log.debugMsg(String.format("Population avg GH: %.2f. (%d out of %d chromosomes valid)",
               (this.averageGH), this.population.getValidChromosomes(), Defines.popSize));
//       if (this.isBestValid) {
//           System.out.println("Best valid chromosome of its generation:");
//           this.bestChromo.print();
//       }
   }
   
   public boolean isValidFound() {
       return this.isValidFound;
   }

   public double getMaxGH() {
       return maxGH;
   }

   public double getAverageGH() {
       return averageGH;
   }
   
   public Chromosome getBestChromo() {
       return this.bestChromo;
   }
   
   
    
}

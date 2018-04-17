package genetic_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Population {

    private ArrayList<Chromosome> chromosomes;

    private ArrayList<Chromosome> parents;
    private ArrayList<Chromosome> offspring;
//    private HashMap<double, Chromosome> map; // [TODO: temporary? track GH with chromosome for selection of elite, parents]
    private GenePool pool;
    private Integer validChromosomes = 0;
    private boolean isSorted = false;
//    private Run run;

    private double bestScore; // highest GH found in population for valid chromo
    private Chromosome bestChromo; // best valid chromo 

    /**
     * Constructor: create a population; initialize the student gene pool and
     * the number of parents.
     *
     * @param pool Student gene pool
     * @param Defines.crossoverParentCt Number of parents selected for
     * reproduction.
     */
//    public Population(GenePool pool, Run run) {
//        this.pool = pool;
////        this.run = run;
//        this.chromosomes = new ArrayList<Chromosome>();
//    }
    /**
     * Constructor: create a population; initialize the student gene pool and
     * the number of parents.
     *
     * @param pool Student gene pool
     * @param Defines.crossoverParentCt Number of parents selected for
     * reproduction.
     */
    public Population(GenePool pool) {
        this.bestScore = 0.0;
        this.pool = pool;
        this.chromosomes = new ArrayList<Chromosome>();
    }

    /**
     * Add a chromosome to the population.
     *
     * @param c Chromosome to be added to the population.
     */
    public void addChromosome(Chromosome c) {
        this.chromosomes.add(c);
    }

    /**
     * Add an array of chromosomes to the population.
     *
     * @param chromoArray Chromosome array to be added to this population.
     */
    public void addChromosomes(ArrayList<Chromosome> chromoArray) {
        if (chromoArray != null) {
            for (Chromosome chromo : chromoArray) {
                this.chromosomes.add(chromo);
            }
        }
    }

    /**
     * We need to know which are the highest scoring chromosomes for help in
     * selecting parents. [TODO: finish this]
     */
    public void evaluate() {
        for (Chromosome chromo : chromosomes) {
            double chromoGH = chromo.getTotalGH();
            if (chromo.isValid()) {
                this.validChromosomes++;
                if (chromoGH > this.getBestScore()) {
                    this.bestScore = chromoGH;
                    this.setBestChromo(chromo);
                }
            }
            //Log.debugMsg(chromo.getTotalGH().toString());
//            this.map.put(chromoGH, chromo);
        }
    }

    /**
     * Elitism automatically promotes some of the best of this population into
     * the next generation. This method identifies the elite chromosomes that
     * get automatically promoted.
     */
    public ArrayList<Chromosome> getEliteChromosomes() {
        // SortedMap instead of HashMap? Duplicate key values? 
        ArrayList<Chromosome> elites = new ArrayList<Chromosome>();
//      ArrayList<Chromosome> sortedList = this.getChromosomesSorted();
        boolean firstRun = true;

        //this.sort();
        Collections.sort(this.chromosomes);

        while (elites.size() < Defines.eliteCt) {
            for (Chromosome chromosome : this.chromosomes) {
                if (elites.size() < Defines.eliteCt) {
                    if (firstRun && chromosome.isValid()) {
                        elites.add(chromosome);
                    } else {
                        elites.add(chromosome);
                    }
                }
            }
            firstRun = false;
        }
        return elites;
    }

    /**
     * Sort the chromosomes by GH
     *
     *
     */
    public ArrayList<Chromosome> getChromosomesSorted() {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        Chromosome temp;   //holding variable
        ArrayList<Chromosome> sortedChromosomes = this.chromosomes;
        while (flag) {
            flag = false;    //set flag to false awaiting a possible swap
            for (j = 0; j < sortedChromosomes.size() - 1; j++) {
                if (sortedChromosomes.get(j).getTotalGH() < sortedChromosomes.get(j + 1).getTotalGH()) // change to > for ascending sort
                {
                    temp = sortedChromosomes.get(j);                //swap elements
                    sortedChromosomes.set(j, sortedChromosomes.get(j + 1));
                    sortedChromosomes.set(j + 1, temp);
                    flag = true;            //shows a swap occurred 
                }
            }
        }
        return sortedChromosomes;
    }

    /**
     * Indicates whether this population has its full complement of chromosomes.
     *
     * @return True if this population contains its full complement of
     * chromosomes; false otherwise.
     */
    public boolean isFull() {
        return this.chromosomes.size() >= Defines.popSize;
    }

    /**
     * Nominate parents for reproduction. Several approaches are possible. We'll
     * start with a purely random selection of two parents.
     */
    public void selectParents() {
        // Create a new parent list for this reproductive cycle. Let the garbage
        // handler dispose of the old list, if there was one.
        switch (Defines.parentAlgo) {
            case Defines.PA_RANDOM:
                this.parents = this.selectParentsRandom();
                break;

            case Defines.PA_ROULETTE:
                this.parents = this.selectParentsRoulette();
                break;

            case Defines.PA_TOURNAMENT:
                this.parents = this.selectParentsTournament();
                break;
        }
//        this.howGoodAreParents();
    }

    /**
     * Nominate parents for reproduction. Several approaches are possible. We'll
     * start with a purely random selection of two parents.
     */
    public void selectParents1() {
        // Create a new parent list for this reproductive cycle. Let the garbage
        // handler dispose of the old list, if there was one.
        switch (Defines.parentAlgo) {
            case Defines.PA_RANDOM:
                this.parents = this.selectParentsRandom();
                break;

            case Defines.PA_ROULETTE:
                this.parents = this.selectParentsRoulette();
                break;

            case Defines.PA_TOURNAMENT:
                this.parents = this.selectParentsTournament();
                break;
        }
//        this.howGoodAreParents();
    }

    /**
     * for testing - displays selection results.
     */
    private void howGoodAreParents() {
        String msg = "From chromo with GH scores:  ";
        for (int i = 0; i < this.chromosomes.size(); i++) {
            msg += this.chromosomes.get(i).getTotalGH() + "-" + this.chromosomes.get(i).getNumValidGroup() + " ";
        }

        msg += "\n\tChose parents (";
        switch (Defines.parentAlgo) {
            case Defines.PA_RANDOM:
                msg += "random";
                break;

            case Defines.PA_ROULETTE:
                msg += "roulette";
                break;

            case Defines.PA_TOURNAMENT:
                msg += "tournament";
                msg += String.format(" (size %d)", Defines.tournamentSize);
                break;
        }
        msg += ") ";
        for (int i = 0; i < Defines.crossoverParentCt; i++) {
            msg += this.parents.get(i).getTotalGH() + " ";
        }
        Log.debugMsg(msg);
    }

    /**
     * Tournament parent selection - should outperform Roulette. Increasing
     * tournament size increases selection pressure. Increased selection
     * pressure causes faster convergence. Must balance convergence with
     * exploration.
     *
     * @return Parents.
     */
    private ArrayList<Chromosome> selectParentsTournament() {
        ArrayList<Chromosome> parents = new ArrayList<Chromosome>(Defines.crossoverParentCt);
        ArrayList<Chromosome> matingPool = new ArrayList<Chromosome>(Defines.crossoverParentCt);

        // Run tournaments to select parents - for each parent
        for (int parentIdx = 0; parentIdx < Defines.crossoverParentCt; parentIdx++) {

            // Run tournaments - get random contestants (run.getPaTournamentSize())
            for (int tournIdx = 0; tournIdx < Defines.tournamentSize; tournIdx++) {
                int contestantID = Defines.randNum(0, this.chromosomes.size() - 1);
                matingPool.add(this.chromosomes.get(contestantID));
            }
            Collections.sort(matingPool);
            parents.add(matingPool.get(0));
        }

        return parents;
    }

    /**
     * Roulette selection of parents.
     *
     * @return Parents.
     */
    private ArrayList<Chromosome> selectParentsRoulette() {
        ArrayList<Chromosome> parents = new ArrayList<Chromosome>(Defines.crossoverParentCt);
        double sumGH = 0.0; // sums GH for all chromosomes in this pop
        Random randgen = new Random(); // random number generator
        for (Chromosome chromo : this.chromosomes) {
            sumGH += chromo.getTotalGH();
        }
        for (int i = 0; i < Defines.crossoverParentCt; i++) {
            double parentRandomizer = randgen.nextDouble() * sumGH; // get random #
            double aggGH = 0.0; // aggregate the GH until we reach our random #
            int chromoIdx = 0; // identifies the chromosome in the pop
            Chromosome parentCandidate;
            do {
                parentCandidate = this.chromosomes.get(chromoIdx++);
                aggGH += parentCandidate.getTotalGH();
            } while (aggGH < parentRandomizer);
            parents.add(parentCandidate);
        }
        return parents;
    }

    /**
     * Random selection of parents from this chromosome.
     *
     * @return Parents.
     */
    private ArrayList<Chromosome> selectParentsRandom() {
        ArrayList<Chromosome> parents = new ArrayList<Chromosome>(Defines.crossoverParentCt);

        for (int i = 0; i < Defines.crossoverParentCt; i++) {
            // Generate random index into chromosomes in range [0..size-1]
            int randomParent = Defines.randNum(0, chromosomes.size() - 1);
            // Remember the new parent
            parents.add(chromosomes.get(randomParent));
        }
        return parents;
    }

    /**
     * Crossover executive - selects and initiates crossover operation.
     */
    public void crossover() {

        // Perform crossover with probability Defines.PROB_CROSSOVER
        if (Defines.probCrossover > Math.random()) {
            this.crossoverOX();
        } else {
            // randomly select one of the parents to copy without crossover
            int idx = Defines.randNum(0, this.parents.size() - 1);
            Chromosome newChild = this.parents.get(idx);
            this.offspring = new ArrayList<Chromosome>();
            this.offspring.add(newChild);
        }
    }

    /**
     * Performs order crossover (OX) operation on parents. As discussed by
     * Potvin. Generates one offspring from two parents.
     */
    public void crossoverOX() {

        // [TODO: consider usefulness of a crossover point that coincides with
        // group boundary. If both crossovers are group boundaries, the crossover does nothing.]
        ArrayList<Integer> crossPoints = getCrossoverPoints(Defines.cpMode);

        Collections.sort(crossPoints);

        ChromosomeGenerator chromoGen = new ChromosomeGenerator(this.pool);

        this.offspring = new ArrayList<Chromosome>(Arrays.asList(chromoGen.generateOffspringOX(this.parents, crossPoints)));

    }

    /**
     * Crossover point executive
     *
     * @return
     */
    private ArrayList<Integer> getCrossoverPoints(int mode) {

        ArrayList<Integer> cPoints = new ArrayList<Integer>();

        int cPoint1 = this.getCrossoverPoint(mode);
        int cPoint2;
        do {
            cPoint2 = this.getCrossoverPoint(mode);
        } while (cPoint2 == cPoint1);

        for (int i = 0; i < Defines.crossoverPoints; i++) {
            cPoints.add(this.getCrossoverPoint(mode));
        }

        return cPoints;
    }

    /**
     * Get a crossover point
     *
     * @param mode Pure random, or not on group boundary
     * @return A new crossover point
     */
    private int getCrossoverPoint(int mode) {
        int cPoint = 0;

        switch (mode) {
            case Defines.CP_PURE_RANDOM:
                cPoint = Defines.randNum(0, Defines.chromosomeSize - 1);
                break;

            case Defines.CP_NO_BOUNDARY:
                do {
                    cPoint = Defines.randNum(0, Defines.chromosomeSize - 1);
                } while (cPoint % Defines.GROUP_SIZE == 0 || cPoint % Defines.GROUP_SIZE == 3);
                break;
        }

        return cPoint;
    }

    /**
     * Performs order crossover (OX) operation on parents. As discussed by
     * Potvin.
     */
    public void origCrossover() {

        ArrayList<Integer> crossPoints;

        // Perform crossover with probability Defines.PROB_CROSSOVER
        if (Defines.probCrossover > Math.random()) {
            // Choose random crossover points within the chromosome
            // [TODO: consider usefulness of a crossover point that coincides with
            // group boundary. If both crossovers are group boundaries, the crossover does nothing.]
            crossPoints = new ArrayList<Integer>();
            for (int i = 0; i < Defines.crossoverPoints; i++) {
                crossPoints.add(Defines.randNum(0, Defines.chromosomeSize - 1));
            }
        } else {
            // Parents are used without crossover - no crossover points; 
            crossPoints = null;
        }

        ChromosomeGenerator chromoGen = new ChromosomeGenerator(this.pool);
        this.offspring = new ArrayList<Chromosome>(Arrays.asList(chromoGen.generateOffspring(this.parents, crossPoints)));
    }

    /**
     * Perform mutation operation on offspring. We define the mutation operation
     * for this application as the exchange of two genes within the chromosome.
     * Mutation is considered for each of the offspring.
     */
    public void mutate() {
        if (this.offspring != null) {
            for (int i = 0; i < this.offspring.size(); i++) {

                if (Defines.probMutate > Math.random()) {
                    // OK, choose two genes to switch
                    int nGene1 = Defines.randNum(0, Defines.chromosomeSize - 1);
                    int nGene2 = nGene1;
                    // Make sure gene2 is not the same gene as gene1
                    while (nGene2 == nGene1) {
                        nGene2 = Defines.randNum(0, Defines.chromosomeSize - 1);
                    }

                    // Switch two genes
                    String temp = this.offspring.get(i).getGene(nGene1);

                    this.offspring.get(i).setGene(nGene1, this.offspring.get(i).getGene(nGene2));
                    this.offspring.get(i).setGene(nGene2, temp);
                }
                // Regenerate the chromosome
                ChromosomeGenerator chromoGen = new ChromosomeGenerator();
                chromoGen.update(this.offspring.get(i));
            }

        }

    }

    /**
     * The children leave the nest.
     *
     * @return ArrayList<Chromosome> of new children.
     */
    public ArrayList<Chromosome> getOffspring() {
        return this.offspring;
    }

    public ArrayList<Chromosome> getChromosomes() {
        return this.chromosomes;
    }

    public ArrayList<Chromosome> getParents() {
        return this.parents;
    }

    /**
     * @return the bestScore
     */
    public double getBestScore() {
        return bestScore;
    }

    /**
     * @return the bestChromo
     */
    public Chromosome getBestChromo() {
        return bestChromo;
    }

    public Integer getValidChromosomes() {
        return validChromosomes;
    }

    public double getMaxGH() {
        this.sort();
        return this.chromosomes.get(0).getTotalGH();
    }

    public void sort() {
        if (!this.isSorted) {
            Collections.sort(this.chromosomes);
            this.isSorted = true;
        }
    }

    /**
     * @param bestChromo the bestChromo to set
     */
    public void setBestChromo(Chromosome bestChromo) {
//        this.bestChromo = bestChromo;
        ChromosomeGenerator chromoGen = new ChromosomeGenerator(this.pool);
        this.bestChromo = chromoGen.clone(bestChromo);
    }

}

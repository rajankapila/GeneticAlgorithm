package genetic_algorithm;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class main {

    private static ArrayList<Population> generations = new ArrayList<Population>();
    private static ArrayList<Run> runs = new ArrayList<Run>();

    public static void main(String[] args) throws Exception {

        String inputFile = "input.txt";       
        String runFile = "runs.txt";
        main app = new main();
        app.loadRuns(runFile);
        app.execute(inputFile);
    }

    public void execute(String inputFile) {
//        testParmSets(); System.exit(1);

        final int MODE_EXPLORE = 0;
        final int MODE_DIRECTED = 1;
        int mode = MODE_DIRECTED;

        // Collect all student genes into pool - do this only once. Must use 
        // same data for meaningful comparison? 
        GenePool pool = loadStudentData(inputFile);
//        app.testChromosomesValidity();

        int runNum = 1;
        int trials = 10;
        switch (mode) {
            case MODE_DIRECTED:

                if (runs != null) {
                    for (Run run : runs) {
                        setRunParms(run);
                        System.out.println("--------------------------------------------------");
                        System.out.format("Run #%d starts at %s\n", runNum++, DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()));
                        reportSettings();
                        System.out.println("\nRunning " + trials + " trials with these settings.");
                        for (int i = 0; i < trials; i++) {
                            System.out.println("Trial #" + (i+1));
                            run(pool);
                        }
                    }
                }
                break;

            case MODE_EXPLORE:

                ParmSet parms = new ParmSet();
                while (!parms.isDone()) {
                    setRunParms(parms);
                    System.out.println("--------------------------------------------------");
                    System.out.format("Run #%d starts at %s\n", runNum++, DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()));
                    reportSettings();
                    run(pool);
                }

                break;

        }
        Statistics.outputOverallBest();

        System.out.format("Finished at %s.\n", DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()));

    }

    /**
     * Sets operational parameters - read from file
     *
     * @param run Input file data with run parameters
     */
    public void setRunParms(Run run) {
        Defines.probCrossover = run.getProbabilityCrossover();
        Defines.probMutate = run.getProbabilityMutate();
        Defines.popSize = run.getPopulationSize();
        Defines.parentAlgo = run.getParentAlgo();
        Defines.tournamentSize = run.getPaTournamentSize();
        Defines.runGenerations = run.getMaxGenerations();
        Defines.cpMode = run.getCpMode();
        Defines.eliteCt = run.getNumberOfElites();
        Defines.dataPointFrequency = run.getDataPointFrequency();
        Defines.crossoverPoints = run.getNumberOfCrossoverPoints();
        Defines.chromoInvPenalty = run.getPenalty();
        Defines.useInvPenalty = true;
        Defines.crossoverParentCt = 2; // works with our crossover operator
    }

    /**
     * Sets operational parameters - from a ParmSet object
     *
     * @param Parmset parms a parameter generator
     */
    private void setRunParms(ParmSet parms) {
        parms.getNextParmSet();
        Defines.probCrossover = parms.getPCross();
        Defines.probMutate = parms.getPMutate();
        Defines.popSize = parms.getPopSize();
        Defines.parentAlgo = parms.getSelectionModels();
        Defines.tournamentSize = parms.getTournamentSize();
        Defines.runGenerations = parms.getGenerations();
        Defines.cpMode = parms.getCPointModels();
        Defines.eliteCt = 2; // not implemented in ParmSet
        Defines.dataPointFrequency = 1000; // not implemented in ParmSet - report data each dpf/1000 generations
        Defines.crossoverPoints = 2; // not implemented in ParmSet - works with CO operator
        Defines.chromoInvPenalty = parms.getPenalty();
        Defines.useInvPenalty = parms.getPenaltyModel();
        Defines.crossoverParentCt = 2; // works with our crossover operator

        System.out.println(parms.toString());
    }

    /**
     * testing iteration through parmsets
     */
    public void testParmSets() {
        ParmSet parms = new ParmSet();
        while (!parms.isDone()) {
            System.out.println(parms.toString());
            parms.getNextParmSet();
        }
    }

    /**
     * Sets operational parameters
     *
     * @param i
     */
    private void loadParmSet6() {
        Defines.runGenerations = 10000;
        Defines.useInvPenalty = true;
        Defines.chromoInvPenalty = 0.1;
        Defines.parentAlgo = Defines.PA_RANDOM;
        Defines.tournamentSize = 0;
        Defines.probCrossover = 0.600000;
        Defines.probMutate = 0.010000;
        Defines.popSize = 50;
        Defines.cpMode = Defines.CP_NO_BOUNDARY;
        System.out.format("Pop=%d Pc=%f Pm=%f Gens=%d XPtMod=%s SelMod=%s PenMod=%s\n",
                Defines.popSize,
                Defines.probCrossover,
                Defines.probMutate,
                Defines.runGenerations,
                Defines.cpMode == Defines.CP_NO_BOUNDARY ? "nobds" : "rand",
                Defines.parentAlgo == Defines.PA_RANDOM ? "rand" : (Defines.parentAlgo == Defines.PA_ROULETTE ? "roul" : "tour(" + Defines.tournamentSize + ")"),
                Defines.useInvPenalty ? "Pen(" + Defines.chromoInvPenalty + ")" : "NoPen"
        );
    }

    /**
     * This method is for testing; checks to see how often randomly updated
     * chromosomes are valid.
     */
    public void testChromosomesValidity() {
        // Collect all student genes into pool
        GenePool pool = loadStudentData("input.txt");

        ChromosomeGenerator chromosomeGenerator = new ChromosomeGenerator(pool);

        // update chromosomes
        int chromoValid = 0;
        int chromoInvalid = 0;
        int nTests = 10000;
//        for (int i = 0; i < nTests; i++) {
//            //generates a new chromosome randomly from seed data
//            Chromosome chromosome = chromosomeGenerator.update("random");
//            if (chromosome.isValid()) {
//                chromoValid++;
//            } else {
//                chromoInvalid++;
//            }
//        }        
//        System.out.format("Over %d trials, valid chromosomes are randomly generated at a rate of %f%%.\n", nTests, (chromoValid / nTests * 100.0));
//        System.out.format("%d successes, %d failures in %d trials.\n", chromoValid, chromoInvalid, nTests);
        int totalTrials = 0;
        int chromoReqd = 1;
        Log.debugMsg("Starting random chromosome generation, lets see how long this takes");
        Timer.start();
        while (chromoValid < chromoReqd) {
            totalTrials++;

            //generates a new chromosome randomly from seed data
            Chromosome chromosome = chromosomeGenerator.generate("random");
            if (chromosome.isValid()) {
                chromoValid++;
            } else {
                chromoInvalid++;
            }
            if (chromoInvalid % 10000 == 0) {
                Log.debugMsg(chromoInvalid + " invalid chromosomes to date.");
            }
        }
        System.out.format("To generate %d valid chromosomes, %d trials were required.\n", chromoReqd, totalTrials);
        System.out.format("Successful chromosome generation rate: %f\n", ((double) chromoValid / (double) totalTrials));
        Timer.end();
    }

    /**
     * This is the program executive.
     *
     * @param run
     * @param pool
     */
    public void run(GenePool pool) {

        double bestScore = 0.0;
        Chromosome bestChromo = null;

        Statistics statistics = new Statistics(Defines.dataPointFrequency);

        ChromosomeGenerator chromosomeGenerator = new ChromosomeGenerator(pool);
        Population population = new Population(pool);

        // update initial population
        for (int i = 0; i < Defines.popSize; i++) {
            //generates a new chromosome randomly from seed data
            Chromosome chromosome = chromosomeGenerator.generate("random");
            population.addChromosome(chromosome);
        }

        Generation generation = new Generation(population);

        boolean done = false;
        int generationCount = 1;

        population.evaluate(); // tell the population to identify best chromosomes.
        statistics.addGeneration(generation);
        statistics.addMaxScoreCount(generation.getMaxGH()); // are we tracking best chromo?
//        generation.print();

        // Iterate through building a new population for each generation:
        // 1) Promote elite chromosomes from old pop to new pop
        // 2) Build new chromosomes for new pop by repeating until new pop is full:
        //    2.1) Identify parent(s)
        //    2.2) Crossover parents
        //    2.3) Mutate parents
        //    2.4) Add offspring to new pop
        // 3) Evaluate the new population
        // 4) Evaluate progress across generations - converging? improving?
        while (!done) {

            Population newPop = new Population(pool);

            newPop.addChromosomes(population.getEliteChromosomes());

            while (!newPop.isFull()) {
                // Choose parent(s)
                population.selectParents();

                // Generate offspring
                population.crossover();
                population.mutate();

                // Add offspring to new population
                ArrayList<Chromosome> offspring = population.getOffspring();
//                this.matchChildAndParent(offspring, population);
                newPop.addChromosomes(offspring);
            } // done building new population

            // We have our new population
            population = newPop;

            // How good is the new pop?
            population.evaluate();

            // track best results so far - redundant with Generations?
            if (population.getBestScore() > bestScore) {
                bestScore = population.getBestScore();
                bestChromo = population.getBestChromo();
            }

            generation = new Generation(population);
            statistics.addMaxScoreCount(generation.getMaxGH());

            if (generationCount % Defines.dataPointFrequency == 0) {
//                Log.debugMsg("Generation=" + generationCount);
//                generation.print();
                statistics.addGeneration(generation);
            }

            generationCount++;
            done = generationCount >= Defines.runGenerations;

        }
        try {
            statistics.output();
        } catch (FileNotFoundException e) {
            Log.debugMsg("Output? File Not Found");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.format("Best valid chromo encountered scored: %f\n", bestScore);
        if (bestChromo != null) {
            if (bestChromo.isValid()) {
                bestChromo.printGroups();
            }
        }
        System.out.println("==================================================\n");

    }

    private void matchChildAndParent(ArrayList<Chromosome> offspring, Population population) {
        int counter = 0;
        if (offspring != null) {
            ArrayList<Chromosome> parents = population.getParents();
            for (int i = 0; i < parents.size(); i++) {
                for (int j = 0; j < offspring.size(); j++) {
                    if (parents.get(i).equivTo(offspring.get(j))) {
                        counter++;
                    }
                }
            }
        }
        System.out.format("%d matches between offspring (of %d) matched some parent (pop=%d).\n",
                counter, offspring.size(), Defines.popSize);
    }

    public void chromosomeValidityReport(Chromosome chromosome) {
        Log.debugMsg("Chromosome is " + (chromosome.isValid() ? "" : " not ") + "valid.");
    }

    /**
     * Grabs student data from input file and stores it in the pool. Each
     * student is a gene.
     */
    public GenePool loadStudentData(String fileName) {
        GenePool pool = new GenePool();
        DataReader reader = new DataReader(fileName); // TODO: use args to set this
        reader.readFile(",");
        HashMap<String, ArrayList<Integer>> data = reader.getData();
        Iterator it = data.entrySet().iterator();

        int geneCount = 0;

        while (it.hasNext()) {

            Map.Entry studentGeneData = (Map.Entry) it.next();

            //add genes to the pool based on the id and array of scores
            // studentID => studentInfoArray
            pool.addGene(new Gene((String) studentGeneData.getKey(), (ArrayList<Integer>) studentGeneData.getValue()));

            geneCount++;

        }
        Defines.chromosomeSize = geneCount;
        Defines.totalGroups = Defines.chromosomeSize / Defines.GROUP_SIZE;
        return pool;
    }

    public void loadRuns(String fileName) throws Exception {
        DataReader reader = new DataReader(fileName); // TODO: use args to set this
        ArrayList<HashMap<String, Object>> runsList = reader.readRunsFile(",");

        if (runsList != null && runsList.size() > 0) {
            for (int i = 0; i < runsList.size(); i++) {
                Run run = new Run(runsList.get(i));
                runs.add(run);
            }
        } else {
            throw new Exception("no runs file");
        }
    }

    public void report(Run run) {

        System.out.format("Population:\t%d\n", run.getPopulationSize());
        System.out.format("Prob crossover:\t%f\n", run.getProbabilityCrossover());
        System.out.format("Prob mutation:\t%f\n", run.getProbabilityMutate());
        System.out.format("Generations:\t%d\n", run.getMaxGenerations());
        System.out.format("Chromo size:\t%d\n", Defines.chromosomeSize);
        System.out.format("Crossover point selection:\t%s\n", run.getCpMode() == Defines.CP_NO_BOUNDARY ? "No group boundary points" : "Unconstrained random");
        System.out.format("Number of crossover points:\t%d\n", run.getNumberOfCrossoverPoints());
        System.out.format("Parent selection:\t%s\n", run.getParentAlgo() == Defines.PA_RANDOM ? "Random"
                : (run.getParentAlgo() == Defines.PA_ROULETTE ? "" + "Roulette" : "Tournament" + " (" + Defines.paTournamentSize + ")"));

    }

    public void reportSettings() {
        System.out.println("Run settings: ");
        System.out.format("Population:\t%d\n", Defines.popSize);
        System.out.format("Prob crossover:\t%f\n", Defines.probCrossover);
        System.out.format("Prob mutation:\t%f\n", Defines.probMutate);
        System.out.format("Generations:\t%d\n", Defines.runGenerations);
        System.out.format("Chromo size:\t%d\n", Defines.chromosomeSize);
        System.out.format("Crossover point selection:\t%s\n", Defines.cpMode == Defines.CP_NO_BOUNDARY ? "No group boundary points" : "Unconstrained random");
        System.out.format("Number of crossover points:\t%d\n", Defines.crossoverPoints);
        System.out.format("Parent selection:\t%s\n", Defines.parentAlgo == Defines.PA_RANDOM ? "Random"
                : (Defines.parentAlgo == Defines.PA_ROULETTE ? "" + "Roulette" : "Tournament" + " (" + Defines.tournamentSize + ")"));
        System.out.format("Penalize invalid chromo:\t%s\n", Defines.useInvPenalty ? "Yes (" + Defines.chromoInvPenalty + ")" : "No");

    }
}

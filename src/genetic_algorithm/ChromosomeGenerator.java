package genetic_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChromosomeGenerator {

    private GenePool pool;

    public ChromosomeGenerator(GenePool pool) {
        this.pool = pool;
    }

    public ChromosomeGenerator() {

    }

    /**
     * Drives the chromosome generation process.
     *
     * @param type What type of generator should we activate?
     * @return A new Chromosome.
     */
    public Chromosome generate(String type) {
        Chromosome chromosome = null;

        if (type.equals("random")) {
            chromosome = this.generateRandom();
        }

        return chromosome;
    }

    /**
     * Use this constructor after modifying an existing chromosome's genes. This
     * will generate internal constructs (groups, valid switch).
     *
     * @param chromo A (genetically?) modified, pre-existing chromosome.
     * @return The updated chromosome.
     */
    public Chromosome update(Chromosome chromo) {
        chromo.makeGroups();
        return chromo;
    }
    
    public Chromosome clone(Chromosome chromo) {
        Chromosome newChromosome = new Chromosome(chromo);
//        this.update(newChromosome);
        
        // all this is done in the copy constructor
//        newChromosome.setNumValidGroup(chromo.getNumValidGroup());
//        newChromosome.setGeneIds(chromo.getGeneIds());
//        newChromosome.setIsValid(chromo.isIsValid());
//        newChromosome.setChromosomeSize(chromo.getChromosomeSize());
//        newChromosome.setChromoGH(chromo.getChromoGH());
//        newChromosome.setPool(chromo.getPool());
        
        return newChromosome;        
    }

    /**
     * Generate a random chromosome - i.e., randomly organize the student genes
     * into a new chromosome.
     *
     * @return A new random chromosome.
     */
    private Chromosome generateRandom() {
        Chromosome newChromosome = new Chromosome(this.pool);
//        ArrayList<String> geneIds = new ArrayList<String>(Arrays.asList(this.pool.genesArray));
        ArrayList<String> geneIds = new ArrayList<String>(this.pool.genesArray);

        String geneList = ""; // just a debugging tracker

        // Create a new chromosome, one gene at a time. Randomly select a gene
        // from the geneIds collection and add it to the chromosome. Keep going
        // until all genes have been moved from geneIds to chromosome.
        while (geneIds.size() > 0) {

            // Select a gene from geneIds by generating a random number in 
            // [0..size-1].
            int randomGenePos = (Defines.randNum(0, geneIds.size() - 1));

            // Move randomly selected gene from geneIds to new chromosome
            String newGene = geneIds.remove(randomGenePos);
            newChromosome.addGene(newGene);

            // Just remember for debugging
            geneList += String.format("%d [%s], ", randomGenePos, newGene);
        }

        newChromosome.makeGroups();

        //System.out.print(geneList);
        return newChromosome;
    }

    /**
     * Generate a random chromosome - i.e., randomly organize the student genes
     * into a new chromosome.
     *
     * @return A new random chromosome.
     */
    private Chromosome generateRandom2() {
        Chromosome newChromosome = new Chromosome(this.pool);
        Set<String> geneIds = new HashSet<String>(this.pool.genesArray);

        boolean goodSelect;
//        String l = "";
        for (int i = 0; i < Defines.chromosomeSize; i++) {
            goodSelect = false;

            while (!goodSelect) {
                Integer random = (int) Math.ceil(Math.random() * (Defines.chromosomeSize));
                if (geneIds.contains(random.toString())) {
//                    l += random.toString() + ",";
                    newChromosome.addGene(random.toString());
                    goodSelect = true;
                    geneIds.remove(random.toString());
                }
            }

        }
        //System.out.print(l);
        return newChromosome;
    }

    /**
     * Crossover operation invokes this to build new chromosomes. Using
     * crossover points, extract splices from parent chromosomes and exchange
     * with the other parent, creating two new offspring. [TODO: revisit
     * hardcoded 2 offspring]
     *
     * @param parents Parent chromosomes used in reproduction
     * @param crossPoints Crossover points that delineate the splices for
     * exchange between parents in the reproduction.
     * @return An array of offspring chromosomes.
     */
    public Chromosome[] generateOffspring(ArrayList<Chromosome> parents, ArrayList<Integer> crossPoints) {
        Chromosome[] offspring = new Chromosome[2];
        // Initialize offspring Chromosomes
        for (int i = 0; i < offspring.length; i++) {
            offspring[i] = new Chromosome(this.pool);
        }

        // No point repeatedly invokeing get method
        Chromosome parent0 = parents.get(0);
        Chromosome parent1 = parents.get(1);

        // startPoint and endPoint are the crossover points. They mark the start 
        // and end points in the list of geneIds where exchange/crossover happens.
        int startPoint;
        int endPoint;

        // Maximum two crossover points in this implementation, used to define
        // endpoints for the chromosome section to be "crossed over".
        // Note: includes flexibility for one or two crossover points.
        if (crossPoints != null) {
            if (crossPoints.size() > 0) {
                startPoint = crossPoints.get(0);
                if (crossPoints.size() > 1) {
                    endPoint = crossPoints.get(1);
                } else {
                    endPoint = Defines.chromosomeSize;
                }

                // First, rather than complicate things with a wraparound splice, we'll 
                // just switch start and end points if startPoint comes after endPoint. 
                // This works because the parents end up the same, but switched: 
                // parent1 ends up as what parent2 would have been, and vice versa.
                if (startPoint > endPoint) {
                    int temp = startPoint;
                    startPoint = endPoint;
                    endPoint = temp;
                }

            } else {
                Log.debugMsg("Chromosome.getSplice error: crossover point array is empty.");
                return null; // [TODO: Might want to exit here.]
            }
        } else {
            // crosspoints is null; we're not doing crossover, just replicating 
            // parents. We'll force this with artificial values for startPoint
            // and endPoint.
            startPoint = Defines.chromosomeSize + 1;
            endPoint = -1;
        }

        // Build offspring using one parent and splice from other parent, one 
        // gene at a time. [TODO: revisit hardcoding]        
        for (int geneIdx = 0; geneIdx < Defines.chromosomeSize; geneIdx++) {
            if (geneIdx < startPoint || geneIdx > endPoint) {
                offspring[0].addGene(parent0.getGene(geneIdx));
                offspring[1].addGene(parent1.getGene(geneIdx));
            } else {
                offspring[0].addGene(parent1.getGene(geneIdx));
                offspring[1].addGene(parent0.getGene(geneIdx));
            }
        }

        return offspring;
    }

    /**
     * Crossover operation OX invokes this to build a new chromosome. Using
     * crossover points, extract splices from parent chromosomes and exchange
     * with the other parent, then fix, creating one new offspring.
     *
     * Approach: 1) splice defined by crosspoints is copied from parent 1 to
     * offspring. 2) parent 2 fills the rest of the offspring
     *
     * @param parents Parent chromosomes used in reproduction
     * @param crossPoints Crossover points that delineate the splices for
     * exchange between parents in the reproduction.
     * @return An array of offspring chromosomes.
     */
    public Chromosome[] generateOffspringOX(ArrayList<Chromosome> parents, ArrayList<Integer> crossPoints) {
        
//        Log.debugMsg(String.format("Crossover points are %d and %d out of %d genes in chromo. Mode is %s",
//                crossPoints.get(0), crossPoints.get(1), Defines.chromosomeSize, (Defines.cpMode == Defines.CP_NO_BOUNDARY?"no boundary":"random")));
        
        Chromosome child = new Chromosome(this.pool);
        Chromosome parent0 = parents.get(0);
        Chromosome parent1 = parents.get(1);

        int startPoint = crossPoints.get(0).intValue();
        int endPoint = crossPoints.get(1).intValue();

        // copy subsection between crossPoints from first parent to child
        for (int i = startPoint; i <= endPoint; i++) {
            child.setGene(i, parent0.getGene(i));
        }

        int parentIdx; // Points to gene in second parent
        if (endPoint < Defines.chromosomeSize - 1) {
            parentIdx = endPoint + 1;
        } else {
            parentIdx = 0;
        }

        // fill child's end from second parent, without duplication
        for (int childIdx = endPoint + 1; childIdx < Defines.chromosomeSize; childIdx++) {
            while (child.containsGene(parent1.getGene(parentIdx))) {
                parentIdx = pointerIncrement(parentIdx, Defines.chromosomeSize);
            }
            child.setGene(childIdx, parent1.getGene(parentIdx));
            parentIdx = pointerIncrement(parentIdx, Defines.chromosomeSize);
        }

        // fill child's start from second parent, without duplication
        for (int childIdx = 0; childIdx < startPoint; childIdx++) {
            while (child.containsGene(parent1.getGene(parentIdx))) {
                parentIdx = pointerIncrement(parentIdx, Defines.chromosomeSize);
            }
            child.setGene(childIdx, parent1.getGene(parentIdx));
            parentIdx = pointerIncrement(parentIdx, Defines.chromosomeSize);
        }
        
        child.makeGroups(); // finishes setup

        // For consistency with other methods, we return the single offspring in an array.
        return new Chromosome[]{child};

    }

    /**
     * Increments an index (or pointer), not beyond maximum, wrapping to 0.
     *
     * @param idx A index into an array of values
     * @param max Maximum value for this index.
     * @return new value for index.
     */
    private int pointerIncrement(int idx, int max) {
        // assumes we're indexing a 0-based array.
        if (idx >= max - 1) {
            idx = 0;
        } else {
            idx++;
        }
        return idx;
    }

    private ArrayList<Integer> sortCrossPoints(ArrayList<Integer> crossPoints) {
        return null;

    }

}

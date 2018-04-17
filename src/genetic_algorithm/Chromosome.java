package genetic_algorithm;

import java.util.ArrayList;

public class Chromosome implements Comparable<Chromosome> {

    private String[] geneIds; // relies on knowing input data :(
    private GenePool pool; //[TODO: does chromosome need the pool?]
    private boolean isValid;
    private int chromosomeSize; // [TODO: is this needed?]
    private Group[] groups;
    private int numValidGroup;
    private double chromoGH;


    public Chromosome(GenePool pool) {
        this.numValidGroup = 0;
        this.geneIds = new String[Defines.chromosomeSize];
        this.isValid = false;
        this.chromosomeSize = 0;
        this.chromoGH = 0.0;
        this.pool = pool;
    }

    /**
     * Copy constructor.
     * @param chromo Copy this chromosome.
     */
    public Chromosome(Chromosome chromo) {
        this.numValidGroup = chromo.numValidGroup;
        this.geneIds = new String[chromo.chromosomeSize];
        System.arraycopy(chromo.geneIds, 0, this.geneIds, 0, chromo.chromosomeSize);
        this.groups = new Group[chromo.getGroups().length];
        System.arraycopy(chromo.groups, 0, this.groups, 0, chromo.groups.length);
        this.isValid = chromo.isValid;
        this.chromosomeSize = chromo.chromosomeSize;
        this.chromoGH = chromo.chromoGH;
        this.pool = chromo.pool;
    }

    public void addGene(String id) {
        this.geneIds[this.chromosomeSize] = id;
        this.chromosomeSize++;
    }

    public boolean isValidSize() {
        return (this.chromosomeSize == Defines.chromosomeSize);
    }

    public boolean isValid() {
//        this.isValid = (this.isValidSize() && this.calcValid());
        return this.isIsValid();
    }

    /**
     * Checks to see whether all groups are valid according to requirements.
     * Requires: - exactly 4 members. - GH > 0.5 - Euclidean distance > 2 for at
     * least one pair in group.
     *
     * @return True if all groups are valid; false otherwise.
     */
    private boolean calcValid() {
        boolean groupCheck = true;
        /*if (!this.isValidSize()) {
            Log.debugMsg("Not Valid Size");
            return false;
        }*/
        /*ArrayList<String> geneList = new ArrayList(Arrays.asList(this.geneIds));
        for(Gene gene : this.pool.getGenes()) {
            if(!geneList.contains(gene.getStudentId())) {
                Log.debugMsg("invalid genes missing");
                return false;
            }
        }*/
        // Examine each group
        this.setNumValidGroup(0);
        for (Group group : this.groups) {
            if (!group.isValid()) {
                groupCheck = false;
            } else {
                this.setNumValidGroup(this.getNumValidGroup() + 1);
            }
        }
        if(!groupCheck) {           
            return false;
        }
        
        return true;
    }

    /**
     * Checks to see if a gene is contained in this chromosome.
     * @param gene A gene to look for in this chromosome.
     * @return True if this gene is contained in this chromosome; false otherwise.
     */
    public boolean containsGene(String gene) {
        for (int i = 0; i < this.getGeneIds().length; i++) {
            if (gene.equals(this.getGene(i))) {
                return true;
            }
        }
        return false;
    }
    
//    public boolean checkValid2() {
//        // First, layer groups on  top of chromosome. First four student genes
//        // are group 1; next 4 are group 2, ....
//        for (int i = 0; i < Defines.totalGroups; i++) {
//            Group g = new Group();
//            for (int j = 0; j < Defines.GROUP_SIZE; j++) {
//                g.addMember(this.pool.getGene(this.geneIds[((i * Defines.GROUP_SIZE) + j)]));
//            }
////            System.out.println("GH = " + g.getGH());
//            if (!g.isValid()) {
//                return false;
//            }
//        }
//        return true;
//    }

    /**
     * Getter - returns groups[]
     *
     * @return Array of groups, where each group is an array of student genes.
     */
    public Group[] getGroups() {
        return this.groups;
    }

    /**
     * Divides the chromosome into groups.
     *
     * @return Array of groups, where each group is an array of student genes.
     */
    public void makeGroups() {
        // How many groups will we have?
        int nGroups = this.getPool().getPoolSize() / Defines.GROUP_SIZE; // TODO: revaluate reliance on this math working out.

        this.setGroups(new Group[nGroups]);

        int nextGenePos = 0;

        for (int groupIdx = 0; groupIdx < nGroups; groupIdx++) {
            groups[groupIdx] = new Group();
            for (int geneIdx = 0; geneIdx < Defines.GROUP_SIZE; geneIdx++) {
                groups[groupIdx].addMember(this.getPool().getGene(this.getGeneIds()[nextGenePos++]));                
            }
            // Now that group membership is complete, trigger the calculation
            // of the various group measures. 
            groups[groupIdx].calcMetrics();
           
        }
        // set up other fields
        this.setChromosomeSize(this.getGeneIds().length);
        this.calcTotalGH();
        this.setIsValid(this.calcValid());
    }

    /**
     * The chromosome's total GH - i.e., the sum of the GH for each member
     * group, is the metric for optimization. This routine calculates chromosome
     * GH and sets the global chromoGH.
     *
     * @return Chromosome's GH.
     */
    private void calcTotalGH() {
        double sumGH = 0;
        if (this.groups == null) {
            this.makeGroups();
        }
        for (Group group : this.getGroups()) {
            if(group.isValid()) {
                sumGH += group.getGH();
            }
        }
        this.setChromoGH(sumGH);
    }
    private void calcTotalGHwithPenalty() {
        double sumGH = 0;
        if (this.groups == null) {
            this.makeGroups();
        }
        for (Group group : this.getGroups()) {
            sumGH += group.getGH();
        }
        if (Defines.useInvPenalty) {
            if (this.isIsValid()) {
                this.setChromoGH(sumGH);
            } else {
                this.setChromoGH(sumGH * Defines.chromoInvPenalty);
            }
        } else {
            this.setChromoGH(sumGH);
        }
    }
    public double getTotalGH() {
        if (this.getChromoGH() == 0.0) {
            this.calcTotalGH();
        }
        return this.getChromoGH();
    }

    /**
     * Used in crossover operation, getSplice returns the piece of a chromosome
     * that will be copied to another parent.
     *
     * @param crossPoints Array of 1 or more crossover points.
     * @return A subset of the geneIds from this chromosome.
     */
    public ArrayList<String> getSplice(ArrayList<Integer> crossPoints) {
        int startPoint;
        int endPoint;

        // Maximum two crossover points in this implementation, used to define
        // endpoints for the chromosome section to be "crossed over".
        if (crossPoints.size() > 0) {
            startPoint = crossPoints.get(0);
            if (crossPoints.size() > 1) {
                endPoint = crossPoints.get(1);
            } else {
                endPoint = this.getGeneIds().length;
            }
        } else {
            Log.debugMsg("Chromosome.getSplice error: crossover point array is empty.");
            return null;
        }

        // First, rather than complicate things with a wraparound splice, we'll 
        // just switch start and end points when start comes after end. This works
        // because the parents end up the same, but switched - parent1 is what parent2
        // would have been, and vice versa.
        if (startPoint > endPoint) {
            int temp = startPoint;
            startPoint = endPoint;
            endPoint = temp;
        }

        // Extract a copy of the crossover splice - i.e., subsection of this chromosome.
        ArrayList<String> splice = new ArrayList<String>();
        for (int i = startPoint; i <= endPoint; i++) {
            splice.add(this.getGeneIds()[i]);
        }

        return splice;
    }

    /**
     * Copy one gene from this chromosome
     *
     * @param num The gene position we seek
     * @return String The gene Id.
     */
    public String getGene(int num) {
        String gene = "";
        try  {
            gene = this.getGeneIds()[num];
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.debugMsg("EXCEPTION: ArrayIndexOutOfBoundsException in Chromosome.getGene");
            String msg = String.format("\tgeneIds.length= %d\n", this.getGeneIds().length);
            msg += String.format("\tDefines.CHROMOSOME_SIZE = %d\n", Defines.chromosomeSize);
            msg += String.format("\trequesting gene #%d\n", num);
            msg += String.format("\tChromosome size = %d\n", this.chromosomeSize);
            Log.debugMsg(msg);
            ex.printStackTrace();
            System.exit(-1);
        }
        return gene;
    }
    
    /**
     * Mutation will need to change single genes within the chromosome.
     * @param genePos The position of the gene to change.
     * @param geneId The new value for the gene at position genePos.
     */
    public void setGene(int genePos, String geneId) {
        try  {
            this.geneIds[genePos] = geneId;
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.debugMsg("EXCEPTION: ArrayIndexOutOfBoundsException in Chromosome.setGene");
            String msg = String.format("\tgeneIds.length= %d\n", this.getGeneIds().length);
            msg += String.format("\tDefines.CHROMOSOME_SIZE = %d\n", Defines.chromosomeSize);
            msg += String.format("\tAttemtpt to set gene #%d to %s\n", genePos, geneId);
            msg += String.format("\tChromosome size = %d\n", this.chromosomeSize);
            ex.printStackTrace();
            Log.debugMsg(msg);
            System.exit(-1);
        }
    }

    /**
     * Output for debugging.
     */
    public void print() {
        for (int i = 0; i < this.getGeneIds().length; i++) {
            //System.out.println(this.geneIds[i]);
            this.getPool().getGene(this.getGeneIds()[i]).print();
        }
    }
    
    public void printGroups() {
        for (int i = 0; i < this.groups.length; i++) {
            System.out.println("Group " + i + ":\t" + this.groups[i].toString());
        }
    }
    
    public void printGeneSequence() {
        String output = "";
        for (int i = 0; i < this.getGeneIds().length; i++) {
            output += i  + "-" + this.getGeneIds()[i] + ", ";
        }
        Log.debugMsg(output);
    }
    

    public int getNumValidGroup() {
        return numValidGroup;
    }

    public int getChromosomeSize() {
        return chromosomeSize;
    }

    @Override
    public int compareTo(Chromosome chromo) {
        if (this.getChromoGH() < chromo.getTotalGH()) {
            return 1;
        } else if (this.getChromoGH() > chromo.getTotalGH()) {
            return -1;
        } else {
            return 0;
        }
    }
    
    /**
     * Checks to see if two chromosomes match gene for gene.
     * @param chromo Compare this chromosome to the current chromosome.
     * @return Returns true if chromosomes match at each gene; false otherwise.
     */
    public boolean equivTo(Chromosome chromo) {
        if (this.getChromosomeSize() != chromo.getChromosomeSize()) {
            return false;
        }
        
        if (this.getTotalGH() != chromo.getTotalGH()) {
            return false;
        }
        
        for (int i = 0; i < this.getChromosomeSize(); i++) {
            if (!this.getGene(i).equals(chromo.getGene(i))) {
                return false;
            }
        }        
        return true;        
    }

    /**
     * @param geneIds the geneIds to set
     */
//    public void setGeneIds(String[] geneIds) {
//        this.setGeneIds(geneIds);
//    }

    /**
     * @param pool the pool to set
     */
    public void setPool(GenePool pool) {
        this.pool = pool;
    }

    /**
     * @param geneIds the geneIds to set
     */
    public void setGeneIds(String[] geneIds) {
        System.arraycopy(geneIds, 0, this.geneIds, 0, geneIds.length);
    }

    /**
     * @param isValid the isValid to set
     */
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * @param chromosomeSize the chromosomeSize to set
     */
    public void setChromosomeSize(int chromosomeSize) {
        this.chromosomeSize = chromosomeSize;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(Group[] groups) {
        this.groups = groups;
    }

    /**
     * @param numValidGroup the numValidGroup to set
     */
    public void setNumValidGroup(int numValidGroup) {
        this.numValidGroup = numValidGroup;
    }

    /**
     * @param chromoGH the chromoGH to set
     */
    public void setChromoGH(double chromoGH) {
        this.chromoGH = chromoGH;
    }

    /**
     * @return the geneIds
     */
    public String[] getGeneIds() {
        return geneIds;
    }

    /**
     * @return the pool
     */
    public GenePool getPool() {
        return pool;
    }

    /**
     * @return the isValid
     */
    public boolean isIsValid() {
        return isValid;
    }

    /**
     * @return the chromoGH
     */
    public double getChromoGH() {
        return chromoGH;
    }
    
    
    
}

package genetic_algorithm;

public class Group {

    private Gene[] genes = new Gene[Defines.GROUP_SIZE];
    private Integer groupSize = 0;
    private Integer groupMaxScore;
    private Integer groupMinScore;
    private double groupAD;
    private double groupGH;
    private Integer maxScoreIndex;
    private Integer minScoreIndex;
    private double maxDistance;
    private boolean groupValid;

    /**
     * Constructor.
     */
    public Group() {

    }

    /**
     * Add a student gene to this group.
     *
     * @param gene New student gene for this group.
     * @return
     */
    public boolean addMember(Gene gene) {
//        System.out.println(this.groupSize + " - " + g.getStudentId());
        this.genes[this.groupSize] = gene;
        this.groupSize++;
        return true;
    }

    public Gene getGene(int i) {
        if (i >= 0 && i < this.genes.length) {
            return this.genes[i];
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        String groupString = "";
        for (int i = 0; i < Defines.GROUP_SIZE; i++) {
            groupString += this.getGene(i).getStudentId();
            if ((i + 1)< Defines.GROUP_SIZE) {
                groupString += "\t";
            }
        }
        return groupString;
        
    }

    /**
     * Invoke this to set up the max, min, AD, GH for this group.
     */
    public void calcMetrics() {
        this.groupMaxScore = this.calcGroupMaxScore();
        this.groupMinScore = this.calcGroupMinScore();
        this.groupAD = this.calcAD();
        this.groupGH = this.calcGH();
        this.groupValid = this.calcValid();
    }

    /**
     * Calculate this group's maximum scoring student gene value.
     *
     * @return Highest attribute score from this group's member student genes.
     */
    private Integer calcGroupMaxScore() {
        Integer maxScore = 0;
        for (int i = 0; i < this.genes.length; i++) {
            Gene gene = genes[i];
            if (maxScore < gene.getAttrScore()) {
                maxScore = gene.getAttrScore();
                this.maxScoreIndex = i; // hidden side effect
            }
        }
        return maxScore;
    }

    public Integer getGroupMaxScore() {
        return this.groupMaxScore;
    }

    /**
     * Calculate this group's minimum scoring student gene value.
     *
     * @return Lowest attribute score from this group's member student genes.
     */
    private Integer calcGroupMinScore() {
        int minScore = this.genes[0].getAttrScore();
        this.minScoreIndex = 0; // hidden side effect
        for (int i = 1; i < this.genes.length; i++) {
            Gene gene = this.genes[i];
            if (minScore > gene.getAttrScore()) {
                minScore = gene.getAttrScore();
                this.minScoreIndex = i; // hidden side effect
            }
        }
        return minScore;
    }

    /**
     * Return the minimum student gene attribute score.
     *
     * @return the minimum student gene attribute score.
     */
    public Integer getGroupMinScore() {
        return this.groupMinScore;
    }

    /**
     * Calculate the group AD ("average distance", or midpoint between
     * outliers).
     *
     * @return Group AD
     */
    private double calcAD() {
        double AD = ((double) this.getGroupMaxScore() + (double) this.getGroupMinScore()) / 2.0;
        return AD;
    }

    /**
     * Return the group AD ("average distance", or midpoint between outliers).
     *
     * @return Group AD
     */
    public double getAD() {
        return this.groupAD;
    }

    /**
     * Calculate this group's goodness of heterogeneity (GH).
     *
     * GH = (maxScore - minScore) / dividend (1 + sum( |AD - each other score| )
     * divisor
     *
     * @return Group GH.
     */
    private double calcGH() {
        double dividend = (double) this.getGroupMaxScore() - (double) this.getGroupMinScore();
        double divisor = 1.0; // initialize to 1 as it's added anyway - see formula

        for (int i = 0; i < this.groupSize; i++) {
            // exclude max and min
            if (i != this.maxScoreIndex && i != this.minScoreIndex) {
                // accumulate 
                divisor += Math.abs(this.getAD() - this.genes[i].getAttrScore());
            }
        }
        return dividend / divisor;
    }

    /**
     * Returns the group GH
     *
     * @return group GH
     */
    public double getGH() {
        return this.groupGH;
    }

    /**
     * Checks whether two group members meet the minimum Euclidean distance
     * requirement.
     *
     * @return True if the distance requirement is satisfied in this group;
     * false otherwise.
     */
    public boolean checkMinDistanceRequirement() {
        boolean minDistanceMet = false;
        double distance;
        for (int i = 0; i < this.genes.length - 1; i++) {
            for (int j = i + 1; j < this.genes.length; j++) {
                distance = this.genes[i].getDistance(this.genes[j]);
                if (distance > Defines.MIN_DISTANCE) {
                    minDistanceMet = true;
                }
                if(distance > this.maxDistance) {
                    this.maxDistance = distance;
                }
            }
        }
        return minDistanceMet;
    }

    public void flushGroup() {
        this.genes = new Gene[Defines.GROUP_SIZE];
    }

    /**
     * Performs the different checks to determine whether this is a valid group.
     *
     * @return true if group is valid; false otherwise.
     */
    private boolean calcValid() {
        String groupIds = "";
        for (int i = 0; i < this.groupSize; i++) {
            groupIds += this.genes[i].getStudentId() + " "; // [TODO: is this used?]
        }
        if (this.groupSize != Defines.GROUP_SIZE) {
//            Log.debugMsg("Group.calcValid [" + groupIds + "] fails on groupSize = " + this.groupSize);
            return false;
        }
        if (this.getGH() <= Defines.MIN_GH) {
//            Log.debugMsg("Group.calcValid [" + groupIds + "] fails on GH = " + this.groupGH);
            return false;
        }
        if (!this.checkMinDistanceRequirement()) {
//            Log.debugMsg("Group.calcValid [" + groupIds + "] fails on min distance");
            return false;
        }

        return true;
    }

    /**
     * Reports whether this is a valid group.
     *
     * @return True if this group passes validity tests; false otherwise.
     */
    public boolean isValid() {
        return this.groupValid;
    }
    /**
     * Get Group genes
     * 
     * @return Gene[]
     */
    
    public Gene[] getGenes() {
        return this.genes;
    }
    
    /**
     * Get the max distance of this group
     * @return maximum distance of the group
     */
    
    public double getMaxDistance() {
        return this.maxDistance;
    }

}

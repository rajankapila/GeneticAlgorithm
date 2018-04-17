package genetic_algorithm;

import java.util.ArrayList;

public class ParmSet {

//Penalize invalid chromo: Yes (0.5)
    private int[] popSize;
    private double[] pCross;
    private double[] pMutate;
    private int[] generations;
    private int[] cPointModels;
    private int[] selectionModels;
    private int[] tournamentSize;
    private boolean[] penaltyModel;
    private double[] penalty;

    private int idxPop;
    private int idxPCross;
    private int idxPMutate;
    private int idxGens;
    private int idxXPts;
    private int idxSelect;
    private int idxPenMod;
    private int idxPenalty;
    private int idxTourSize;

    private final boolean DONE = false;
    private boolean swDone = false;
    private boolean firstCall;

    public ParmSet() {
        firstCall = true;

        popSize = new int[]{5, 10, 15, 20};
        pCross = new double[]{0.6, 0.7, 0.8, 0.9};
        pMutate = new double[]{0.01, 0.05, 0.1};
        generations = new int[]{10000, 50000, 100000};
        cPointModels = new int[]{Defines.CP_NO_BOUNDARY, Defines.CP_PURE_RANDOM};
        selectionModels = new int[]{Defines.PA_TOURNAMENT, Defines.PA_ROULETTE, Defines.PA_RANDOM};
        tournamentSize = new int[]{2, 3, 4, 5};
        penaltyModel = new boolean[]{false, true};
        penalty = new double[]{0.1, 0.25, 0.5, 0.7, 1.0};

        idxPop = 0;
        idxPCross = 0;
        idxPMutate = 0;
        idxGens = 0;
        idxXPts = 0;
        idxSelect = 0;
        idxPenMod = 0;
        idxPenalty = 0;
        idxTourSize = 0;
    }

    @Override
    public String toString() {
        if (!this.swDone) {
            try {
                return String.format("Pop=%d Pc=%.2f Pm=%.2f Gens=%d XPtMod=%s SelMod=%s PenMod=%s\n",
                        popSize[idxPop],
                        pCross[idxPCross],
                        pMutate[idxPMutate],
                        generations[idxGens],
                        cPointModels[idxXPts] == Defines.CP_NO_BOUNDARY ? "nobds" : "rand",
                        selectionModels[idxSelect] == Defines.PA_RANDOM ? "rand"
                        : (selectionModels[idxSelect] == Defines.PA_ROULETTE ? "roul" : "tour(" + tournamentSize[idxTourSize] + ")"),
                        penaltyModel[idxPenMod] ? "Pen(" + penalty[idxPenalty] + ")" : "NoPen"
                );
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.format("Crash.\nIndexes:Pop %d\tCO %d\tMut %d\tGen %d\tPts %d\tSel %d (TSize %d)\tPen %d (Amt %d)\n",
                        idxPop,
                        idxPCross,
                        idxPMutate,
                        idxGens,
                        idxXPts,
                        idxSelect,
                        idxTourSize,
                        idxPenMod,
                        idxPenalty);
                System.out.format("Sizes:\tPop %d\tCO %d\tMut %d\tGen %d\tPts %d\tSel %d (TSize %d)\tPen %d (Amt %d)\n",
                        this.popSize.length,
                        this.pCross.length,
                        this.pMutate.length,
                        this.generations.length,
                        this.cPointModels.length,
                        this.selectionModels.length,
                        this.tournamentSize.length,
                        this.penaltyModel.length,
                        this.penalty.length);
                System.exit(1);
                return "bang";

            }
        } else {
            return "All done.";
        }
    }

    public boolean getNextParmSet() {
        if (firstCall) {
            firstCall = false;
            // initialize?
            return !DONE;
        }
        // prioritize - put more important earlier.
        if (iterateSelection()) {
            if (++idxPop >= popSize.length) {
                idxPop = 0;
                if (++idxGens >= generations.length) {
                    idxGens = 0;
                    if (++idxPCross >= pCross.length) {
                        idxPCross = 0;
                        if (++idxPMutate >= pMutate.length) {
                            idxPMutate = 0;
                            if (++idxXPts >= cPointModels.length) {
                                idxXPts = 0;
//                            if (++idxSelect >= selectionModels.length) {
//                                idxSelect = 0;
                                if (iteratePenalty()) {
                                    swDone = true;
                                    return DONE;
                                }
                            }
                        }
                    }
                }
            }
        }
        return !DONE;
    }

    private boolean iteratePenalty() {
        // Is penalty mode on?
        if (this.penaltyModel[this.idxPenMod]) {
            // Penalty mode: are we at end of penalty values?
            if (++this.idxPenalty >= this.penalty.length) {
                // At last penalty value; do we have another penalty mode?
                if (++this.idxPenMod >= this.penaltyModel.length) {
                    // At the end; reset and move to next parm
                    this.idxPenMod = 0;
                    this.idxPenalty = 0;
                    return true;
                } else {
                    // go to next penalty mode
                }

            } else {
                // to go next penalty value
            }

        } else {
            // Not penalty mode; go to next mode
            if (++this.idxPenMod >= this.penaltyModel.length) {
                // At the end; reset and move to next parm
                this.idxPenMod = 0;
                this.idxPenalty = 0;
                return true;
            } else {
                // go to next penalty mode
            }

        }
        return false;
    }

    private boolean iterateSelection() {

        // Special case: are we looking at tournament selection?
        if (this.selectionModels[idxSelect] == Defines.PA_TOURNAMENT) {

            // Looking at tournament selection; iterate through tournament sizes
            if (++this.idxTourSize >= this.tournamentSize.length) {
                
                this.idxTourSize = 0;

                // We are at the last tournament size; next selection model
                if (++this.idxSelect >= this.selectionModels.length) {
                    // At the last selection model; reset and move to next parm
                    this.idxSelect = 0;
                    return true;
                } else {
                    // Go to next selection model
                }

            } else {
                // tournament selection: go to next tournament size
            }
        } else {
            // Not tournament selection; are we at the end of selection models?
            if (++this.idxSelect >= this.selectionModels.length) {
                // We are at the end of selection models - reset and move to next parm
                this.idxSelect = 0;
                this.idxTourSize = 0;
                return true;

            } else {
                // Go to next selection model
            }
        }

        return false;
    }

    /**
     * @return the popSize
     */
    public int getPopSize() {
        return popSize[idxPop];
    }

    /**
     * @return the pCross
     */
    public double getPCross() {
        return pCross[idxPCross];
    }

    /**
     * @return the pMutate
     */
    public double getPMutate() {
        return pMutate[idxPMutate];
    }

    /**
     * @return the generations
     */
    public int getGenerations() {
        return generations[idxGens];
    }

    /**
     * @return the cPointModels
     */
    public int getCPointModels() {
        return cPointModels[idxXPts];
    }

    /**
     * @return the selectionModels
     */
    public int getSelectionModels() {
        return selectionModels[idxSelect];
    }

    /**
     * @return the penaltyModel
     */
    public boolean getPenaltyModel() {
        return penaltyModel[idxPenMod];
    }

    /**
     * @return the tournamentSize
     */
    public int getTournamentSize() {
        return tournamentSize[idxTourSize];
    }

    /**
     * @return the penalty
     */
    public double getPenalty() {
        return penalty[idxPenalty];
    }

    /**
     * @return the swDone
     */
    public boolean isDone() {
        return swDone;
    }

}

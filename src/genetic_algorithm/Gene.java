package genetic_algorithm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Each student is implemented as a gene in our chromosome.
 */
public class Gene {

    private ArrayList<Integer> studentAttrs;
    private String studentId;
    private Integer score;
//    private Integer maxScore;
//    private Integer minScore;
    private HashMap<String, Double> distances = new HashMap<String, Double>();

    /**
     * Constructor - creates a new student gene from student ID and attributes.
     *
     * @param id
     * @param attrs
     */
    public Gene(String id, ArrayList<Integer> attrs) {
        this.setStudentId(id);
        this.setStudentAttrs(attrs);
        this.setScore(this.calcAttrScore());
    }

    /**
     * Returns this student's attribute arraylist.
     *
     * @return This student's attribute scores.
     */
    public ArrayList<Integer> getStudentAttrs() {
        return this.studentAttrs;
    }

    /**
     * Sets this student's attribute scores.
     *
     * @param attrs
     */
    public void setStudentAttrs(ArrayList<Integer> attrs) {
        this.studentAttrs = attrs;
    }

    /**
     * Returns this student's ID.
     *
     * @return Student ID.
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Sets this student's ID.
     *
     * @param studentId A student ID to use for this student.
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Returns this student's attribute score. This is calculated as the sum of
     * the attribute values.
     *
     * @return This student's attribute score.
     */
    public Integer getAttrScore() {
        if (this.score == null) {
            this.score = 0;
            if (this.studentAttrs != null) {
                for (int i = 0; i < this.studentAttrs.size(); i++) {
                    this.score += this.studentAttrs.get(i);
                }
            }
        }
        return this.score;
    }

    /**
     * Calculates this student's attribute score by summing the attribute
     * values.
     *
     * @return This student's attribute score.
     */
    private Integer calcAttrScore() {
        int attrScore = 0;
        
        if (this.studentAttrs != null) {
            for (int i = 0; i < this.studentAttrs.size(); i++) {
                attrScore += this.studentAttrs.get(i);
            }
        }
        return attrScore;
    }
    
    /**
     * Set this student's attribute score. 
     * @param score Set this student's attribute score to this value.
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    //trying this at first, may blow up.
    /**
     * Returns the Euclidean distance between this student gene and the 
     * parameter student gene. As student genes get regrouped across many
     * iterations over the program run, these two students might be in the same
     * group again. So we'll save the distance to avoid recalculating it later.
     * @param g2 Another student gene whose distance from this we seek.
     * @return The distance between this student gene and the parameter gene.
     */
    public double getDistance(Gene g2) {
        Double distance;

        // Have we seen this other student gene before?
        if (!this.distances.containsKey(g2.getStudentId())) {
            // New groupmate; calculate and remember distance
            Double sum = 0.0;

            for (int i = 0; i < this.studentAttrs.size(); i++) {
                sum += Math.pow((this.studentAttrs.get(i) - g2.getStudentAttrs().get(i)), 2);

            }
            distance = Math.sqrt(Math.abs(sum));
            distances.put(g2.getStudentId(), distance);
        } else {
            // We've seen this groupmate before. Look up distance.
            distance = this.distances.get(g2.getStudentId());
        }
        return distance;
    }

    public void print() {
        String scores = "";
        for (int i = 0; i < this.studentAttrs.size(); i++) {
            scores += this.studentAttrs.get(i);
            if (i < this.studentAttrs.size() - 1) {
                scores += ",";
            }
        }
        System.out.println(this.getStudentId() + " - " + scores + " - " + this.getAttrScore());
    }

}

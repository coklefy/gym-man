package gymman.fitness;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class of Metric. It is the method of execution of
 * an exercise. It can be in two types :
 *        1- with timer
 *        2- with repetitions
 */
public abstract class Metric {
    /** minimum sets for an exercise. **/
    private static final int MIN_SETS = 1;
    /** maximum sets for an exercise. **/
    private static final int MAX_SETS = 50;

    /** metric sets. */
    @Getter @Setter private int sets;
    /** metric type. */
    @Getter @Setter private MetricType metricType;

    /**
     * Constructor.
     * @param nrSets : number of sets of the metric
     *
     * @throw IllegalArgumentException
     */
    public Metric(final int nrSets) {
        if (nrSets < MIN_SETS || nrSets > MAX_SETS) {
             throw new IllegalArgumentException("Illegal sets or repetitions value");
        }
        this.sets = nrSets;
    }

    @Override
    public final String toString() {
        return this.metricType.toString();
    }

}

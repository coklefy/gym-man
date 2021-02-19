package gymman.fitness;

import lombok.Getter;
import lombok.Setter;

/**
 * Type of the execution metric of exercises.
 * It consists in one exercise that can be executed
 * in sets and repetitions.
 *
 */
public class MetricWithRepetitions extends Metric {

    /** maximum repetitions allowed for an exercise. **/
    private static final int MAX_REPETITIONS = 50;
    /** minimum repetitions allowed for an exercise. **/
    private static final int MIN_REPETITIONS = 1;

    /** repetitions: how many repetitions a set will have. */
    @Getter @Setter private int repetitions;

    /**
     * Constructor to create the Metric with repetitions.
     * Setting the number of sets and the repetitions for
     * each set.
     * @param nrSets : number of sets
     * @param nrRepetitions : number of repetitions
     */
    public MetricWithRepetitions(final int nrSets, final int nrRepetitions) {
        super(nrSets);

        if (nrRepetitions < MIN_REPETITIONS || nrRepetitions > MAX_REPETITIONS) {
            throw new IllegalArgumentException("Illegal repetitions value");
        }
        this.repetitions = nrRepetitions;
        this.setMetricType(MetricType.METRIC_WITH_REPETITIONS);
    }

}

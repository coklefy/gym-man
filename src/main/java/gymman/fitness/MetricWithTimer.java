package gymman.fitness;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * Type of the execution metric of exercises.
 * It consists in one exercise that can be executed
 * in sets for an amount of time.
 */
public class MetricWithTimer extends Metric {


    /** maximum seconds allowed for a set. **/
    private static final int MAX_SEC_ALLOWED = 300;
    /** minimum seconds allowed for a set. **/
    private static final int MIN_SEC_ALLOWED = 3;

    /** time of the set execution. */
    @Getter @Setter private int seconds;

    /**
     * Constructor to create the Metric with timer.
     * Setting the number of sets and the timer for
     * each set.
     * @param nrSets : number of sets
     * @param seconds : timer of each set
     */
    public MetricWithTimer(final int nrSets, final int seconds) {
        super(nrSets);

        if (seconds < MIN_SEC_ALLOWED || seconds > MAX_SEC_ALLOWED) {
			throw new IllegalArgumentException("Illegal seconds value");
		}
        this.seconds = seconds;
        this.setMetricType(MetricType.METRIC_WITH_TIMER);

    }

}

package gymman.fitness;

/**
 *
 * Enumeration of all Metric Types.
 *
 */
public enum MetricType {
	/** Metric type with timer. */
    METRIC_WITH_TIMER("with timer"),
    /** Metric type with set-rep */
    METRIC_WITH_REPETITIONS("with repetitions");

    /** Type denomination of metric. */
    private final String type;

    MetricType(final String type) {
        this.type = type;
    }

    @Override
    public final String toString() {
        return this.type;
    }
}

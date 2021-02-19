package gymman.fitness;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Class to test the two types of the metric execution.
 * Metric with timer and metric with repetitions.
 * Controlling non compatible parameters and setting the fields.
 */
public class ExecutionMetricTest {
    /** timerMetric field. Execution method with timer metric */
    private final Metric timerMetric = new MetricWithTimer(10, 10);
    /** repetitionMetric field. Execution method with timer repetitions */
    private final Metric repetitionMetric = new MetricWithRepetitions(10, 10);

    /**
     * Test timer with metric.
     * Control metric type and changing the sets and the seconds
     */
	@Test
	public void timerMetricTest() {

	    assertEquals("Metric type is with timer", timerMetric.getMetricType(), MetricType.METRIC_WITH_TIMER);
        assertNotEquals("Metric type is not with repetitions", timerMetric.getMetricType(), MetricType.METRIC_WITH_REPETITIONS);

        assertEquals("Metric sets are 10", timerMetric.getSets(), 10);
        assertNotEquals("Metric sets are not 11", timerMetric.getSets(), 11);

        timerMetric.setSets(15);
        assertNotEquals("Metric sets are not 10", timerMetric.getSets(), 10);
        assertEquals("Metric sets are  15", timerMetric.getSets(), 15);


        assertEquals("Metric seconds are 10", ((MetricWithTimer) timerMetric).getSeconds(), 10);
        assertNotEquals("Metric seconds are not 11", ((MetricWithTimer) timerMetric).getSeconds(), 11);


        ((MetricWithTimer) timerMetric).setSeconds(11);
        assertNotEquals("Metric seconds are not 10", ((MetricWithTimer) timerMetric).getSeconds(), 10);
        assertEquals("Metric seconds are 11", ((MetricWithTimer) timerMetric).getSeconds(), 11);
	}


    /**
     * Test timer with repetitions.
     * Control metric type and changing the sets and the repetitions
     */
	@Test
	public void repetitionsMetricTest() {

        assertEquals("Metric type is with repetitions", repetitionMetric.getMetricType(), MetricType.METRIC_WITH_REPETITIONS);
        assertNotEquals("Metric type is not with timer", repetitionMetric.getMetricType(), MetricType.METRIC_WITH_TIMER);

        assertEquals("Metric sets are 10", repetitionMetric.getSets(), 10);
        assertNotEquals("Metric sets are not 11", repetitionMetric.getSets(), 11);

        repetitionMetric.setSets(15);
        assertNotEquals("Metric repetitions are not 10", repetitionMetric.getSets(), 10);
        assertEquals("Metric repetitions are 10", repetitionMetric.getSets(), 15);


        assertEquals("Metric repetitions are 10", ((MetricWithRepetitions) repetitionMetric).getRepetitions(), 10);
        assertNotEquals("Metric repetitions are not 11", ((MetricWithRepetitions) repetitionMetric).getRepetitions(), 11);


        ((MetricWithRepetitions) repetitionMetric).setRepetitions(11);
        assertNotEquals("Metric repetitions are not 10", ((MetricWithRepetitions) repetitionMetric).getRepetitions(), 10);
        assertEquals("Metric repetitions are 11", ((MetricWithRepetitions) repetitionMetric).getRepetitions(), 11);
	}


    /**
     * Test timer  metric with illegal parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void illegalTimerMetricParametersTest() {
        /** timer metric with not compatible parameters */
        final Metric timerMetric = new MetricWithTimer(0, 0);
    }

    /**
     * Test repetitions metric with illegal parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void illegalRepetitionsMetricParametersTest() {
        /** repetitions metric with not compatible parameters */
        final Metric repetitionsMetric = new MetricWithRepetitions(0, 0);
    }
}

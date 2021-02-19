package gymman.fitness;

/**
 *
 * All the goals that can be added in one
 * training program.
 *
 */
public enum Goal {

    GAIN_STRENGTH("Gain strength"),
    INCREASE_MUSCLE_SIZE("Increase muscle size"),
    DEFENITION("Definition"),
    SLIMMING("Slimming"),
    BUILD_MUSCLE("Build muscle");

    /** goal denomination. */
    private final String goal;

    Goal(final String goal) {
        this.goal = goal;
    }

    @Override
    public String toString() {
        return this.goal;
    }

}

package gymman.customers;

/**
 * The Class InvalidValueException.
 */
public class InvalidValueException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 346125632221120511L;

    /**
     * Instantiates a new invalid value exception.
     *
     * @param entity the entity
     */
    public InvalidValueException(final String entity) {
        super(String.format("Inserito valore non valido nel campo '%s'", entity));
    }

}

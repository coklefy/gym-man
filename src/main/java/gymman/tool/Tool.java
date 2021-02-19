package gymman.tool;

import gymman.common.BaseEntity;
import gymman.customers.InvalidValueException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * The Class Tool make a new tool for gym.
 */
public final class Tool extends BaseEntity {


    @Getter @Setter private String name;

    @Getter @Setter private String desc;

    @Getter @Setter private String numseriale;

    @Getter @Setter private Integer maintenance;

    /**
     * Instantiates a new Tool.
     */
    private Tool() {
        super();
    }

    /**
     * Of.
     *
     * @param id the id
     * @param name the name
     * @param desc the desc
     * @param numseriale the numseriale
     * @param maintenance the maintenance
     * @return the tool
     * @throws InvalidValueException the invalid value exception
     */

    @Builder
    public static Tool of(
            final String id,
            final String name,
            final String desc,
            final String numseriale,
            final Integer maintenance
            ) throws InvalidValueException {
        final Tool tool = new Tool();
        if (id!= null) {
            tool.id = id;
        }
        if(!checkValue(maintenance)) {
            throw new InvalidValueException("maintenance");
        }
        if(!isNameValueValid(name)) {
            throw new InvalidValueException("nome");
        }
        if(isEmptyValue(name)) {
            throw new InvalidValueException("nome");
        }
        if(isEmptyValue(desc)) {
            throw new InvalidValueException("desc");
        }
        if(isEmptyValue(numseriale)) {
            throw new InvalidValueException("numseriale");
        }
        if(isEmptyValue(String.valueOf(maintenance))) {
            throw new InvalidValueException("maintenance");
        }

        tool.name = name;
        tool.desc = desc;
        tool.numseriale = numseriale;
        tool.maintenance = maintenance;
        return tool;
    }

    /**
     * Checks if value is Empty.
     *
     * @param tmp the tmp
     * @return true, if is empty value
     */
    private static boolean isEmptyValue(final String tmp) {
        return tmp.trim().isEmpty();
    }

    /**
     * Checks if is name value valid.
     *
     * @param name the name
     * @return true, if is name value valid
     */
    private static boolean isNameValueValid(final String name) {
        return name.matches("[a-zA-Z]*");
    }

    /**
     * Check value months valid.
     *
     * @param months the months
     * @return true, if successful
     */
    public static boolean checkValue(final int months) {
        return months <= 12 && months >= 0 ;
    }

}

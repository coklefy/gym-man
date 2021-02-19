package gymman.fitness;

import static gymman.fitness.Category.Type.ENDURANCE;
import static gymman.fitness.Category.Type.STRENGTH;
import static gymman.fitness.Category.Type.BALANCE;

import java.util.ArrayList;
import java.util.List;

/**
 * The categories of the exercises. Any category has a type.
 */
public enum Category {

    CARDIO(ENDURANCE, "Cardio"),
	CORE(ENDURANCE, "Core"),
	FULL_BODY(STRENGTH, "Full body"),
	FUNCTIONAL_TRAINING(STRENGTH, "Functional training"),
	OWN_BODY_WEIGHT(STRENGTH, "Own body weight"),
	STRETCHING(BALANCE, "Stretching"),
	TONING(BALANCE, "Toning");

    /** Type of the category. **/
	private final Type type;
	/** Denomination of the category. **/
	private final String actualName;

	Category(final Type type, final String actualName) {
		this.type = type;
		this.actualName = actualName;
	}

	/**
	 * Method to return the type of a category.
	 * @return {@link Type} of the category
	 */
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.actualName;
	}

	/**
	 * The types of any category that will be created.
	 */
	public enum Type {

		ENDURANCE("Endurance"),
		STRENGTH("Strength"),
		BALANCE("Balance"),
		FLEXIBILITY("Flwxibility");

	    /** Type denomination. **/
	    private final String type;

        Type(final String typeName) {
            this.type = typeName;
        }

        /**
         * Method to return all the categories of a particular type.
         * @return List of categories
         */
        public List<Category> getCategories() {
            final ArrayList<Category> list = new ArrayList<>();
            for (final Category c : Category.values()) {
                if (c.getType() == this) {
                    list.add(c);
                }
            }
            return list;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
}


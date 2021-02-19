package gymman.fitness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gymman.fitness.Category.Type;

/**
 * Test the {@link Category} Enumeration functionalities.
 */
public class CategoryTest {

    /**
     *  Test the type of a particular category.
     */
    @Test
    public void categoryFitsTypeTest() {
        final Category cardio = Category.CARDIO;
        final Type endurance = Type.ENDURANCE;
        final Type strength = Type.STRENGTH;

        assertEquals("Cardio category of Endurance Type", cardio.getType(), endurance);
        assertNotEquals("Cardio category is not of Strength Type", cardio.getType(), strength);
    }

    /**
     *  Test the different types of different categories.
     */
    @Test
    public void typeCategoriesTest() {
        final Category cardio = Category.CARDIO;
        final Category core = Category.CORE;
        final Category fullBody = Category.FULL_BODY;
        final Type endurance = Type.ENDURANCE;

        final List<Category> myCategories = new ArrayList<>();

        myCategories.add(cardio);
        myCategories.add(core);
        assertEquals("Categories of Endurance Type", myCategories, endurance.getCategories());

        myCategories.add(fullBody);
        assertNotEquals("Not all the Categories are of Endurance Type", myCategories, endurance.getCategories());

    }

}

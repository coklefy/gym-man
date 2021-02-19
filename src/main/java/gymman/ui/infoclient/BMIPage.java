package gymman.ui.infoclient;

import gymman.ui.AbstractPage;

/**
 * The Class BMIPage.
 */
public class BMIPage extends AbstractPage {

    /**
     * Instantiates a new BMI page.
     */
    public BMIPage() {
        super(BMIPage.class.getResource("BMI.fxml"));
    }

    @Override
    public String getId() {
        return "page_bmi";
    }


    @Override
    public String getTitle() {
        return "BMI Cliente";
    }


    @Override
    public boolean hasMenuEntry() {
        return false;
    }
}

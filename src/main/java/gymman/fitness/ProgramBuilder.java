package gymman.fitness;

import java.util.Date;
import gymman.customers.Customer;
import gymman.employees.Employee;

/**
 *
 * Builder interface of the  TrainingProgramBuilder.
 * Offers the methods to create a new TrainingProgram
 */

public interface ProgramBuilder {

    /**
     * Method to set the customer of the training program.
     *
     * @param customer : the person who will use this training program
     * @return the builder
     */
     ProgramBuilder customer(Customer customer);


    /**
     * Method to set the training program start date.
     *
     * @param dateFrom : program start day
     * @return the builder
     */
     ProgramBuilder periodValidationFrom(Date dateFrom);


    /**
     * Method to set the training program expire date.
     *
     * @param dateTo : program expire date
     * @return the builder
     */
     ProgramBuilder periodValidationTo(Date dateTo);


    /**
     * Method to set the customer goal.
     *
     * @param exGoal : customer goal to be added to training program
     * @return the builder
     */
     ProgramBuilder goal(Goal exGoal);


     /**
      * Method to set the customer tutor.
      *
      * @param tutor : employee that follows the customer during the workout
      * @return the builder
      */
     ProgramBuilder tutor(Employee tutor);

     /**
	  * Method to build the new Training Program. Check if the necessary fields have
	  * been compiled.
	  *
	  * @return the new builded TrainingProgram
	  */
	 TrainingProgram buildTrainingProgramPlan();

}

package gymman.fitness;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gymman.common.BaseEntity;
import gymman.customers.Customer;
import gymman.employees.Employee;
import gymman.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * Class to create a new TrainingProgram extending  BaseEntity class.
 * Offering the possibility to add and remove exercises from the training program.
 *
 */
public final class TrainingProgram extends BaseEntity implements EditableTrainingProgram {
    /** Minimum validation days to consider a program as valid. */
    private static final int MIN_VALID_DAYS = 1;
    /** Key of training program is equal as customer's user name. **/
    @Getter @Setter private String key;
    /** Training program headed to this customer. **/
    @Getter @Setter private Customer customer;
    /** Program start date. **/
    @Getter @Setter private Date validFrom;
    /** Program expire date. **/
    @Getter @Setter private Date validTo;
    /** Goal of training programs. **/
    @Getter @Setter private Goal goal;
    /** Tutor to follow the customer workout **/
    @Getter @Setter private Employee tutor;
    /** Set of all the exercises of training program. Firstly is empty **/
    @Getter @Setter private Set<Exercise> exercises;

    /**
     * Class to implement the  ProgramBuilder interface.
     * Check if all the informations to create a new TrainingProgram
     * have been full-filled.
     *
     */
    public static  class TrainingProgramBuilder implements ProgramBuilder {
        /** Key of training program is equal as customer's user name. **/
        private String key;
        /** Training program headed to this customer. **/
        private Customer customer;
        /** Program start date. **/
        private Date validFrom;
        /** Program expire date. **/
        private Date validTo;
        /** Goal of training programs. **/
        private Goal programGoal;
        /** Tutor to follow the customer workout. **/
        private Employee tutor;


        @Override
		public final TrainingProgramBuilder customer(final Customer customer) {
            this.customer = customer;
            this.key = customer.getUsername();
            return this;
        }

        @Override
        public final TrainingProgramBuilder periodValidationFrom(final Date dateFrom) {
            this.validFrom = dateFrom;
            return this;
        }

        @Override
        public final TrainingProgramBuilder periodValidationTo(final Date dateTo) {
            if (DateUtils.daysBetweenDates(this.validFrom, dateTo) < MIN_VALID_DAYS) {
                throw new IllegalStateException("Validation Date not correct");
            } else {
                this.validTo = dateTo;
            }
            return this;
        }

        @Override
        public final TrainingProgramBuilder goal(final Goal exGoal) {
            this.programGoal = exGoal;
            return this;
        }

        @Override
        public final TrainingProgramBuilder tutor(final Employee tutor) {
            this.tutor = tutor;
            return this;
        }


        @Override
        public final TrainingProgram buildTrainingProgramPlan() {
            final TrainingProgram trainingProgram = new TrainingProgram();

            trainingProgram.key = this.key;
            trainingProgram.customer = this.customer;
            trainingProgram.validFrom = this.validFrom;
            trainingProgram.validTo = this.validTo;
            trainingProgram.goal = this.programGoal;
            trainingProgram.tutor = this.tutor;

            if (trainingProgram.getCustomer() == null) {
                throw new IllegalStateException("Training Program not assing to any customer");
            } else {
                if (trainingProgram.getValidFrom() == null) {
                    throw new IllegalStateException("Validation Date From not specified");
                } else {
                    if (trainingProgram.getValidTo() == null) {
                        throw new IllegalStateException("Validation Date To not specified");
                    }  if (trainingProgram.getTutor() == null) {
                    	throw new IllegalStateException("Tutor not specified");
                    } else {
                        return trainingProgram;
                    }
                }
            }
        }

    }


    private TrainingProgram() {
        super();
        exercises = new HashSet<>();
    }

    /**
     * Method to set the validation expire date  of the {@link TrainingProgram}.
     *
     * @param validTo {@link Date} : expire date
     * @throws IllegalStateException validation period is not correct.
     *
     */
    public void setValidTo(final Date validTo) {
        if (this.validFrom == null) {
            throw new IllegalStateException("Validation From Date not corrct");
        } else {
            if (DateUtils.daysBetweenDates(this.getValidFrom(), validTo) < MIN_VALID_DAYS) {
                throw new IllegalStateException("Validation To Date not corrct");
            } else {
                this.validTo = validTo;
            }
        }
    }


    @Override
    public void addNewExercise(final Exercise newExercise) {
        if (containsExercise(newExercise.getName())) {
            throw new IllegalStateException("Exercise already added");
        } else {
            this.exercises.add(newExercise);
        }
    }


    @Override
    public void removeExercise(final String name) {
        this.exercises.removeIf((ex) -> ex.getName().equals(name));
    }

    private List<String> getExercisesNames() {
        final List<String> exNames  = new ArrayList<>();
        this.exercises.forEach(ex -> exNames.add(ex.getName()));
        return exNames;
    }

    private boolean containsExercise(final String name) {
        return this.getExercisesNames().stream().filter(ex -> ex.equals(name)).findAny().isPresent();
    }

}

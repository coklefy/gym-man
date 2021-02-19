package gymman.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * Date utility class to offer operations with dates.
 *
 */

//final, because it's not supposed to be subclassed
public final class DateUtils {

    private DateUtils() {
        // private constructor to avoid unnecessary instantiation of the class
    }

    private static long getDateDiff(final Date date1, final Date date2, final TimeUnit timeUnit) {
        final long diffInMillies = date2.getTime() - date1.getTime();
         return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
    *
    * Method to get the difference between two local dates.
    *
    * @param date1 the oldest date
    * @param date2 the newest date
    * @return the difference value, in the provided unit
    *
    */
   public static long daysBetweenLocalDates(final LocalDate date1, final LocalDate date2) {
       final Date fromDate = DateUtils.convertToDateViaInstant(date1);
       final Date toDate =  DateUtils.convertToDateViaInstant(date2);

       return getDateDiff(fromDate, toDate, TimeUnit.DAYS);
   }


    /**
     *
     * Method to convert LocalDate to Date.
     *
     * @param dateToConvert {@link LocalDate} : local date to be converted
     * @return date
     */
    public static Date convertToDateViaInstant(final LocalDate dateToConvert) {
        return Date.from(
                dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     *
     * Method to convert Date to LocalDate.
     *
     * @param dateToConvert : date to be converted
     * @return localDate
     *
     */
    public static LocalDate convertToLocalDateViaInstant(final Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    /**
     *
     * Method to get the difference between two dates.
     *
     * @param date1 the oldest date
     * @param date2 the newest date
     * @return the difference value, in the provided unit
     *
     */
    public static long daysBetweenDates(final Date date1, final Date date2) {
        return DateUtils.getDateDiff(date1, date2, TimeUnit.DAYS);
    }

    /**
     *
     * Method to convert a date in a string format.
     *
     * @param date : date to be converted
     * @return date in the string format
     *
     */
    public static String parseDateToString(final Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    /**
     *
     * Method to convert a string in a date format.
     *
     * @param date : date in the string format
     * @return Date
     *
     * @throws ParseException
     *
     */
    public static Date parseDateFromString(final String date) throws ParseException {
         return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    /**
     * Method to return a Local date from another LocalDate,
     * adding a number of days to the current day.
     * @param date : current LocalDate
     * @param daysToAdd : number of the days to be added to current date
     * @return LocalDate : the new date
     */
    public static LocalDate addDays(final LocalDate date, final long daysToAdd) {
        return date.plusDays(daysToAdd);
    }


}

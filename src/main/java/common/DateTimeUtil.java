package common;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * This class provides utility for creating dates and
 * times.
 */
public abstract class DateTimeUtil {

    /**
     * @return The current date.
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}

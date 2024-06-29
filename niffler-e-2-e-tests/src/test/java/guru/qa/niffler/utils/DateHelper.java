package guru.qa.niffler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.", e);
        }
    }

    public static String formatDate(Date date, String dateFormat) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
        return outputFormat.format(date);
    }
}

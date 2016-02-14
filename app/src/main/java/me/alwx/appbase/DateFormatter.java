package me.alwx.appbase;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class DateFormatter {

    private DateFormatter() {
        throw new AssertionError();
    }

    public static String formatDate(Format format, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        return new SimpleDateFormat(format.getFormatString(), Locale.getDefault())
                .format(calendar.getTime());
    }

    public enum Format {
        FULL("dd.MM.yyyy HH:mm");

        private String mFormatString;

        Format(String formatString) {
            mFormatString = formatString;
        }

        public String getFormatString() {
            return mFormatString;
        }
    }
}

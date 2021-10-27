package database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class LocalDateTimeConverter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime toDate(String dateString) {
        return dateString == null ? null : LocalDateTime.parse(dateString);
    }

    @TypeConverter
    public static String toDateString(LocalDateTime date) {
        return date == null ? null : date.toString();
    }
}

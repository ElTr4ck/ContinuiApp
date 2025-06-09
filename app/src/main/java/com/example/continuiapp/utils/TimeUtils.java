package com.example.continuiapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String getRelativeTime(Date date) {
        long diff = System.currentTimeMillis() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "Hace un momento";
        } else if (minutes < 60) {
            return "Hace " + minutes + (minutes == 1 ? " minuto" : " minutos");
        } else if (hours < 24) {
            return "Hace " + hours + (hours == 1 ? " hora" : " horas");
        } else if (days < 7) {
            return "Hace " + days + (days == 1 ? " día" : " días");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(date);
        }
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
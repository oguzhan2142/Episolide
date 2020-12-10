package com.oguzhan.episolide.utils;

import java.util.Locale;

public abstract class Utils
{
    public static String ExtractYear(String movieDbDate)
    {
        return movieDbDate.split("-")[0];
    }


    public static String ConvertDateAsFormatted(String movieDbDate)
    {
        try
        {
            String template = "%s.%s.%s";
            String[] parts = movieDbDate.split("-");

            return String.format(Locale.US, template, parts[2], parts[1], parts[0]);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return movieDbDate;
        }
    }
}

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


    public static String ConvertBudgedAsFormatted(String budged)
    {


        StringBuilder builder = new StringBuilder();


        for (int i = budged.length() - 1, commaCounter = 1; i >= 0; i--, commaCounter++)
        {
            char ch = budged.charAt(i);
            builder.append(ch);

            if (commaCounter == 0) continue;

            if (commaCounter % 3 == 0)
            {
                builder.append(",");
            }
        }

        return builder.reverse().toString() + "$";
    }
}

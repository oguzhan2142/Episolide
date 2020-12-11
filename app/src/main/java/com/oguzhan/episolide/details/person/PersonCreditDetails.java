package com.oguzhan.episolide.details.person;

import com.oguzhan.episolide.utils.Utils;

import java.util.List;

public class PersonCreditDetails implements Comparable<PersonCreditDetails>

{

    public String name;
    public String roleName;
    public String job;
    public String firstAirDate;


    public static PersonCreditDetails CastInstance(String name, String roleName, String firstAirDate)
    {
        PersonCreditDetails instance = new PersonCreditDetails();
        instance.name = name;
        instance.roleName = roleName;
        instance.firstAirDate = firstAirDate;
        return instance;
    }

    public static PersonCreditDetails CrewInstance(String name, String job, String firstAirDate)
    {
        PersonCreditDetails instance = new PersonCreditDetails();
        instance.name = name;
        instance.job = job;
        instance.firstAirDate = firstAirDate;
        return instance;
    }


    @Override
    public int compareTo(PersonCreditDetails o)
    {

        if (o.firstAirDate.equals(""))
            return -1;
        if (firstAirDate.equals(""))
            return 1;

        int second = 0, first = 0;
        try
        {
            second = Integer.parseInt(Utils.ExtractYear(o.firstAirDate));
            first = Integer.parseInt(Utils.ExtractYear(this.firstAirDate));
        } catch (NumberFormatException e)
        {
            e.printStackTrace();

        }
        if (first > second)
            return -1;
        else if (second > first)
            return 1;

        return 0;
    }
}

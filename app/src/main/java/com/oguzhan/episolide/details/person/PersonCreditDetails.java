package com.oguzhan.episolide.details.person;

import com.oguzhan.episolide.utils.Utils;

import java.util.List;

public class PersonCreditDetails implements Comparable<PersonCreditDetails>

{

    public String name;
    public String roleName;
    public String firstAirDate;


    public PersonCreditDetails()
    {
    }

    public PersonCreditDetails(String name)
    {
        this.name = name;
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

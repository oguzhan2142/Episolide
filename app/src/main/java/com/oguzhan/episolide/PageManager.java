package com.oguzhan.episolide;

import java.util.Locale;

public class PageManager
{

    private int currentPage;
    private int maxPage;


    public PageManager(int currentPage, int maxPage)
    {
        this.currentPage = currentPage;
        this.maxPage = maxPage;
    }

    public void nextPage()
    {
        if (!canNext())
            currentPage++;

    }


    public void previousPage()
    {
        if (!canPrevious())
            currentPage--;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "%d/%d", currentPage, maxPage);
    }

    private boolean canPrevious()
    {
        return currentPage <= 1;
    }


    private boolean canNext()
    {
        return currentPage >= maxPage;
    }


    public int getCurrentPage()
    {
        return currentPage;
    }
}

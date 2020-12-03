package com.oguzhan.episolide.ui.SearchNav.results;

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
        if (!isPassedMaxPage())
            currentPage++;

    }


    public void previousPage()
    {
        if (!isPassedMinPage())
            currentPage--;
    }

    private boolean isPassedMinPage()
    {
        return currentPage < 1;
    }


    private boolean isPassedMaxPage()
    {
        return currentPage > maxPage;
    }


    public int getCurrentPage()
    {
        return currentPage;
    }
}

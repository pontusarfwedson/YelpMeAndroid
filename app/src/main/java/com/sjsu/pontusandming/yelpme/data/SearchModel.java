package com.sjsu.pontusandming.yelpme.data;

/**
 * Created by PontusArfwedson on 2016-10-03.
 */
public class SearchModel {
    private String total;

    private Results[] results;

    private String total_pages;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    public String getTotal_pages ()
    {
        return total_pages;
    }

    public void setTotal_pages (String total_pages)
    {
        this.total_pages = total_pages;
    }
}

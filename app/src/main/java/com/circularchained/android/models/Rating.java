package com.circularchained.android.models;

import java.io.Serializable;

public class Rating implements Serializable{

    private Esg esg;
    private int noOfItems;

    public Rating() {
    }

    public Rating(Esg esg, int noOfItems) {
        this.esg = esg;
        this.noOfItems = noOfItems;
    }

    public Esg getEsg() {
        return esg;
    }

    public void setEsg(Esg esg) {
        this.esg = esg;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        this.noOfItems = noOfItems;
    }
}

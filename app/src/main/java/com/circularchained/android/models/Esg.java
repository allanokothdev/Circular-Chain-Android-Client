package com.circularchained.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Esg implements Serializable{

    private float natureScore;
    private float climateScore;
    private float labourScore;
    private float communityScore;
    private float wasteScore;

    public Esg() {
    }

    public Esg(float natureScore, float climateScore, float labourScore, float communityScore, float wasteScore) {
        this.natureScore = natureScore;
        this.climateScore = climateScore;
        this.labourScore = labourScore;
        this.communityScore = communityScore;
        this.wasteScore = wasteScore;
    }

    public float getNatureScore() {
        return natureScore;
    }

    public void setNatureScore(float natureScore) {
        this.natureScore = natureScore;
    }

    public float getClimateScore() {
        return climateScore;
    }

    public void setClimateScore(float climateScore) {
        this.climateScore = climateScore;
    }

    public float getLabourScore() {
        return labourScore;
    }

    public void setLabourScore(float labourScore) {
        this.labourScore = labourScore;
    }

    public float getCommunityScore() {
        return communityScore;
    }

    public void setCommunityScore(float communityScore) {
        this.communityScore = communityScore;
    }

    public float getWasteScore() {
        return wasteScore;
    }

    public void setWasteScore(float wasteScore) {
        this.wasteScore = wasteScore;
    }
}

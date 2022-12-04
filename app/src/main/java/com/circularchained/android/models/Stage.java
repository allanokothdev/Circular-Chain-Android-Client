package com.circularchained.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Stage implements Serializable{

    private String id;
    private String batchId;
    private String title;
    private String summary;
    private long date;
    private int step;
    private String location;
    private float nature;
    private float climate;
    private float labour;
    private float community;
    private float waste;
    private ArrayList<String> tags;

    public Stage() {
    }

    public Stage(String id, String batchId, String title, String summary, long date, int step, String location, float nature, float climate, float labour, float community, float waste, ArrayList<String> tags) {
        this.id = id;
        this.batchId = batchId;
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.step = step;
        this.location = location;
        this.nature = nature;
        this.climate = climate;
        this.labour = labour;
        this.community = community;
        this.waste = waste;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getNature() {
        return nature;
    }

    public void setNature(float nature) {
        this.nature = nature;
    }

    public float getClimate() {
        return climate;
    }

    public void setClimate(float climate) {
        this.climate = climate;
    }

    public float getLabour() {
        return labour;
    }

    public void setLabour(float labour) {
        this.labour = labour;
    }

    public float getCommunity() {
        return community;
    }

    public void setCommunity(float community) {
        this.community = community;
    }

    public float getWaste() {
        return waste;
    }

    public void setWaste(float waste) {
        this.waste = waste;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Stage stage = (Stage) obj;
        return id.matches(stage.getId());
    }
}

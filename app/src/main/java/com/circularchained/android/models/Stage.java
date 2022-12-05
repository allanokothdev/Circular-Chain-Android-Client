package com.circularchained.android.models;

import java.io.Serializable;

public class Stage implements Serializable{

    private String stageId;
    private String title;
    private String summary;
    private String publisher;
    private long timestamp;
    private String location;
    private Esg esgScore;
    private String batchId;

    public Stage() {
    }

    public Stage(String stageId, String title, String summary, String publisher, long timestamp, String location, Esg esgScore, String batchId) {
        this.stageId = stageId;
        this.title = title;
        this.summary = summary;
        this.publisher = publisher;
        this.timestamp = timestamp;
        this.location = location;
        this.esgScore = esgScore;
        this.batchId = batchId;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Esg getEsgScore() {
        return esgScore;
    }

    public void setEsgScore(Esg esgScore) {
        this.esgScore = esgScore;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Stage stage = (Stage) obj;
        assert stage != null;
        return stageId==stage.getStageId();
    }
}

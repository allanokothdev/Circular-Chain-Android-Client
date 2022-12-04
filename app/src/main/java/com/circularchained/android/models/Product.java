package com.circularchained.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {

    private String id;
    private String title;
    private String pic;
    private String summary;
    private String category;
    private int batchId;
    private float rating;
    private ArrayList<String> tags;

    public Product() {
    }

    public Product(String id, String title, String pic, String summary, String category, int batchId, float rating, ArrayList<String> tags) {
        this.id = id;
        this.title = title;
        this.pic = pic;
        this.summary = summary;
        this.category = category;
        this.batchId = batchId;
        this.rating = rating;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Product product = (Product) obj;
        return id.matches(product.getId());
    }
}

package com.circularchained.android.models;

import java.io.Serializable;

public class User implements Serializable {


    private String id; //user id
    private String pic; //user picture
    private String name; //user name

    public User() {
    }

    public User(String id, String pic, String name) {
        this.id = id;
        this.pic = pic;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        User user = (User) obj;
        return id.matches(user.getId());
    }
}

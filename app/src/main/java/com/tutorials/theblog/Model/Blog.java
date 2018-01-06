package com.tutorials.theblog.Model;

/**
 * Created by HP on 05-11-2017.
 */

public class Blog {

    public String title;
    public String image;
    public String desc;
    public String timestamp;
    public String userid;

    public Blog()
    {

    }

    public Blog(String title, String image, String desc, String timestamp, String userid) {
        this.title = title;
        this.image = image;
        this.desc = desc;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}


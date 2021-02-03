package com.example.smartapp.Post;

public class postmodel {
    public String date,descryption,fullname,time;
    public String postimage;
    public postmodel() {
    }

    public postmodel(String postimage, String descryption, String fullname, String time, String date) {
        this.date = date;
        this.descryption = descryption;
        this.fullname = fullname;
        this.time = time;
        this.postimage = postimage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescryption() {
        return descryption;
    }

    public void setDescryption(String descryption) {
        this.descryption = descryption;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }
}

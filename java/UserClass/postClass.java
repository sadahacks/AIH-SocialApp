package com.example.smartapp.UserClass;

import com.example.smartapp.Post.DiscussionActivity;

import java.util.List;

public class postClass {
    private String postId;
    private String postImage;
    private String description;
    private String publisher;

    public postClass(String postId, String postImage, String description, String publisher) {
        this.postId = postId;
        this.postImage = postImage;
        this.description = description;
        this.publisher = publisher;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public postClass(DiscussionActivity discussionActivity, List<postClass> mPost) {
    }
}
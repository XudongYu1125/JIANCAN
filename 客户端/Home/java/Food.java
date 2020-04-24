package com.example.user.jiancan;

import android.media.Image;

import java.util.Set;

public class Food {

    private int id;
    private int vegetablesId;
    private int userId;

    private String title;
    private String content;
    private String foodName;
    private int fabulous;
    private int download;
    private String uploadDate;

    private Set<Image> images;
    private String video;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getVegetablesId() {
        return vegetablesId;
    }
    public void setVegetablesId(int vegetablesId) {
        this.vegetablesId = vegetablesId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public int getFabulous() {
        return fabulous;
    }
    public void setFabulous(int fabulous) {
        this.fabulous = fabulous;
    }
    public int getDownload() {
        return download;
    }
    public void setDownload(int download) {
        this.download = download;
    }
    public String getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
    public Set<Image> getImages() {
        return images;
    }
    public void setImages(Set<Image> images) {
        this.images = images;
    }
    public String getVideo() {
        return video;
    }
    public void setVideo(String video) {
        this.video = video;
    }


}

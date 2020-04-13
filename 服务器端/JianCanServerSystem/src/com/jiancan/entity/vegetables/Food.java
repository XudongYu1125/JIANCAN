package com.jiancan.entity.vegetables;

import javax.persistence.Entity;

@Entity
public class Food {
	private int id;
	private int vegetablesId;
	private int userId;
	private String name;
	private int fabulous;
	private int download;
	private String imageUrl;
	private String videosUrl;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getVideosUrl() {
		return videosUrl;
	}
	public void setVideosUrl(String videosUrl) {
		this.videosUrl = videosUrl;
	}
	
}

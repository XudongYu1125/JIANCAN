package com.jiancan.entity.vegetables;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "food")

public class Food {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int vegetablesId;
	private int userId;
	
	private int type;
	private String title;
	private String content;
	private String foodName;
	private int fabulous;
	private int download;
	private String uploadDate;
	
	@OneToMany(cascade = CascadeType.ALL, targetEntity = Image.class)
	@JoinColumn(name = "foodId")
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	@Override
	public String toString() {
		return "Food [id=" + id + ", vegetablesId=" + vegetablesId + ", userId=" + userId + ", title=" + title
				+ ", content=" + content + ", foodName=" + foodName + ", fabulous=" + fabulous + ", download="
				+ download + ", uploadDate=" + uploadDate + ", images=" + images + ", video=" + video + "]";
	}
	
	
	
	
}

package com.example.user.jiancan.personal.entity;

public class Image {

	private int id;
	private String imageName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		return "Image{" +
				"id=" + id +
				", imageName='" + imageName + '\'' +
				'}';
	}
}

package com.jiancan.entity.vegetables;

import javax.persistence.Entity;

@Entity
public class Vegetables {
	private int id;
	private String vegetable;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVegetable() {
		return vegetable;
	}
	public void setVegetable(String vegetable) {
		this.vegetable = vegetable;
	}
	
}

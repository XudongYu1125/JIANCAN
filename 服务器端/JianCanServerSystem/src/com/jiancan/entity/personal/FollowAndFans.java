package com.jiancan.entity.personal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "followandfans")
public class FollowAndFans {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int followId;
	private int fansId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFollowId() {
		return followId;
	}
	public void setFollowId(int followId) {
		this.followId = followId;
	}
	public int getFansId() {
		return fansId;
	}
	public void setFansId(int fansId) {
		this.fansId = fansId;
	}
	
}

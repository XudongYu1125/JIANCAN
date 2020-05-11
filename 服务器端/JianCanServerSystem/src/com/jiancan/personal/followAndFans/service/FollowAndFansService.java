package com.jiancan.personal.followAndFans.service;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiancan.personal.followAndFans.dao.FollowAndFansDao;

@Service
public class FollowAndFansService {
	@Resource
	private FollowAndFansDao followAndFansDao;
	public String addFollow(int userId,int followId) {
		return followAndFansDao.insertFollow(userId, followId)+"";
	}
	public String removeFollow(int userId,int followId) {
		return followAndFansDao.deleteFollow(userId, followId)+"";
	}
	public String findFanss(int userId){
		return new GsonBuilder().serializeNulls().create().toJson(followAndFansDao.selectFanss(userId));
	}
	public String findFollows(int userId){
		return new GsonBuilder().serializeNulls().create().toJson(followAndFansDao.selectFollows(userId));
	}
}

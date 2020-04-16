package com.jiancan.personal.followAndFans.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiancan.entity.personal.TUser;
import com.jiancan.personal.followAndFans.dao.FollowAndFansDao;

@Service
public class FollowAndFansService {
	@Resource
	private FollowAndFansDao followAndFansDao;
	public int addFollow(int userId,int followId) {
		return this.followAndFansDao.insertFollow(userId, followId);
	}
	public int removeFollow(int userId,int followId) {
		return this.followAndFansDao.deleteFollow(userId, followId);
	}
	public List<TUser> findFanss(int userId){
		return this.followAndFansDao.selectFanss(userId);
	}
	public List<TUser> findFollows(int userId){
		return this.followAndFansDao.selectFollows(userId);
	}
}

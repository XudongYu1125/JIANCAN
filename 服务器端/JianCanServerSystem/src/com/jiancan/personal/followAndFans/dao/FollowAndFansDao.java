package com.jiancan.personal.followAndFans.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.jiancan.entity.personal.TUser;
import com.jiancan.entity.personal.User;

@Repository
public class FollowAndFansDao {
	@Resource
	private SessionFactory sessionFactory;
	private Session session = this.sessionFactory.getCurrentSession();
	private Transaction txTransaction = session.beginTransaction();
	public List<TUser> selectTUsers(List userIdList){
		List<TUser> tUsers = new ArrayList<TUser>();
		String sql="from User where id in ?";
		Query query = this.session.createQuery(sql);
		query.setParameter(0, userIdList);
		for(User user:(List<User>)query.list()) {
			TUser tUser = new TUser();
			BeanUtils.copyProperties(user,tUser);
			tUsers.add(tUser);
		}
		return tUsers;
	}
	public List<TUser> selectFollows(int userId) {
		String sql="select followId from followandfans where fansId=?";
		Query query = this.session.createQuery(sql);
		query.setParameter(0, userId);
		return selectTUsers(query.list());
	}
	public List<TUser> selectFanss(int userId) {
		String sql="select fansId from followandfans where followId=?";
		Query query = this.session.createQuery(sql);
		query.setParameter(0, userId);
		return selectTUsers(query.list());
	}
	public int insertFollow(int userId,int followId) {
		String sql="insert into followandfans(followId,fansId) value(?,?)";
		Query query = this.session.createQuery(sql);
		query.setParameter(0, followId);
		query.setParameter(1, userId);
		int i = query.executeUpdate();
		txTransaction.commit();
		return i;
	}
	public int deleteFollow(int userId,int followId) {
		String sql="delete from followandfans where fansId=? and followId=?";
		Query query = this.session.createQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, followId);
		int i = query.executeUpdate();
		txTransaction.commit();
		return i;
	}
}

package com.jiancan.personal.collectAndRecord.dao;

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
import com.jiancan.entity.vegetables.Food;

@Repository
public class CollectAndRecordDao {
	@Resource
	private SessionFactory sessionFactory;
	private Session session = this.sessionFactory.getCurrentSession();
	private Transaction txTransaction = session.beginTransaction();
	//userId和foodId添加记录
	public int insert(int userId,int foodId,int type) {
		String str="insert into collectandrecord(userId,foodId,type) value(?,?,?)";
		Query query = session.createQuery(str);
		query.setParameter(0, userId);
		query.setParameter(1, foodId);
		query.setParameter(2, type);
		int i = query.executeUpdate();
		txTransaction.commit();
		return i;
		
	}
	//userId和foodId删除记录
	public int delete(int userId,int foodId,int type) {
		String str="delete from collectandrecord where userId=? and foodId=? and type=?";
		Query query = session.createQuery(str);
		query.setParameter(0, userId);
		query.setParameter(1, foodId);
		query.setParameter(2, type);
		int i = query.executeUpdate();
		txTransaction.commit();
		return i;
	}
	//根据foodIdList查询记录foodList
	public List<Food> selectRecordsByList(List foodIdList) {
		String sql="from food where id in ?";
		Query query = this.session.createQuery(sql);
		query.setParameter(0, foodIdList);
		return (List<Food>)query.list();	
	}
	//根据foodIdList查询记录foodList
	public List<Food> selectRecordsByUserId(int userId,int type) {
		String sql="select foodId from collectandrecord where uerId=? and type=?";
		Query query = this.session.createQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, type);
		return selectRecordsByList(query.list());
	}
}

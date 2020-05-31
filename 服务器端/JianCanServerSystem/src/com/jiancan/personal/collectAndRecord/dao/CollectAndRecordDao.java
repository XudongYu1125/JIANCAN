package com.jiancan.personal.collectAndRecord.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.jiancan.entity.personal.CollectAndRecord;
import com.jiancan.entity.vegetables.Food;

@Repository
public class CollectAndRecordDao {
	@Resource
	private SessionFactory sessionFactory;
	//userId和foodId添加记录
	public int insert(int userId,int foodId,int type) {
		CollectAndRecord collectAndRecord = new CollectAndRecord();
		collectAndRecord.setUserId(userId);
		collectAndRecord.setFoodId(foodId);
		collectAndRecord.setType(type);
		Session session = sessionFactory.getCurrentSession();
		Transaction txTransaction = session.beginTransaction();
		session.save(collectAndRecord);
		txTransaction.commit();
		return 1;
		
	}
	//userId和foodId删除记录
	public int delete(int userId,int foodId,int type) {
		Session session = sessionFactory.getCurrentSession();
		Transaction txTransaction = session.beginTransaction();
		String str="delete from CollectAndRecord where userId=? and foodId=? and type=?";
		Query query = session.createQuery(str);
		query.setParameter(0, userId);
		query.setParameter(1, foodId);
		query.setParameter(2, type);
		int i = query.executeUpdate();
		txTransaction.commit();
		return i;
	}
	//userId和foodIdList批量删除记录
	public int deleteSome(int userId,List<Integer> foodIdList,int type) {
		Session session = sessionFactory.getCurrentSession();
		Transaction txTransaction = session.beginTransaction();
		String str="delete from CollectAndRecord where userId=? and foodId=? and type=?";
		int i=0;
		for(int foodId:foodIdList) {
			Query query = session.createQuery(str);
			query.setParameter(0, userId);
			query.setParameter(1, foodId);
			query.setParameter(2, type);
			i += query.executeUpdate();
		}
		txTransaction.commit();
		return i;
	}
	//根据foodIdList查询记录foodList
	public List<Food> selectRecordsByList(List<Integer> foodIdList) {
		Session session = sessionFactory.getCurrentSession();
		List<Food> foods = new ArrayList<Food>();
		String sql="from Food where id = ?";
		for(int i:foodIdList) {
			Query query = session.createQuery(sql);
			query.setParameter(0, i);
			Food food = (Food)query.list().get(0);
			foods.add(food);
		}
		return foods;	
	}
	//根据foodIdList查询记录foodList
	public List<Food> selectRecordsByUserId(int userId,int type) {
		Session session = sessionFactory.getCurrentSession();
		String sql="select foodId from CollectAndRecord where userId=? and type=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, type);
		return selectRecordsByList(query.list());
	}
}

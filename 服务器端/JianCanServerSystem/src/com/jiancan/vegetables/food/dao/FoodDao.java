package com.jiancan.vegetables.food.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.jiancan.entity.vegetables.Food;

@Repository
public class FoodDao {

	@Resource
	private SessionFactory sessionFactory;
	
	public Food saveFood(Food food) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			session.save(food);
			tx.commit();
			
			return food;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean deleteFood(int id) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			Food food = new Food();
			food.setId(id);
			
			session.delete(food);
			tx.commit();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean editFood(Food food) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
				
			session.update(food);
			tx.commit();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public List<Food> selectFoodByUser(int userId){
		
		try {
			
			Session session = this.sessionFactory.openSession();
	
			String hql = "from Food f where f.userId = ?";
			
			Query query = session.createQuery(hql);
			query.setParameter(0, userId);
		
			List<Food> list = query.list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Food> selectFoodByVegetables(int vegetablesId) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
	
			String hql = "from Food f where food.vegetablesId = ?";
			
			Query query = session.createQuery(hql);
			query.setParameter(0, vegetablesId);
		
			List<Food> list = query.list();
		
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	
}

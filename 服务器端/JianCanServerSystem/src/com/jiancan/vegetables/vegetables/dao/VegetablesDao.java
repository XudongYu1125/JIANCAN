package com.jiancan.vegetables.vegetables.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.jiancan.entity.vegetables.Vegetables;

@Repository
public class VegetablesDao {

	@Resource
	private SessionFactory sessionFactory;
	
	public Vegetables saveVegetables(Vegetables vegetables) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			session.save(vegetables);
			tx.commit();
			
			return vegetables;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean deleteVegetables(int id) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			Vegetables vegetables = new Vegetables();
			vegetables.setId(id);
			
			session.delete(vegetables);
			tx.commit();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean editVegetables(Vegetables vegetables) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
				
			session.update(vegetables);
			tx.commit();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public List<Vegetables> selectAll() {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			
			String hql = "from Vegetables";
			
			Query query = session.createQuery(hql);
		
			List<Vegetables> list = query.list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}

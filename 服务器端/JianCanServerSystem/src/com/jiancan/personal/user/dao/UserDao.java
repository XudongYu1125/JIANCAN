package com.jiancan.personal.user.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.jiancan.entity.personal.User;

@Repository
public class UserDao {
	@Resource
	private SessionFactory sessionFactory;
	public List<User> selectAllUsers(){
		Session session = sessionFactory.getCurrentSession();
		String sql = "from User";
		Query query = session.createQuery(sql);
		return (List<User>)query.list();
	}
	public int insertUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		Transaction txTransaction = session.beginTransaction();
		String sql= "insert into User(sex,nickname,phone,password,imageurl) values(?,?,?,?,?)";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, user.getSex());
		query.setParameter(1, user.getNickname());
		query.setParameter(2, user.getPhone());
		query.setParameter(3, user.getPassword());
		query.setParameter(4, user.getImageUrl());
		int i =query.executeUpdate();
		txTransaction.commit();
		return i;
	}
	public int deleteUser(int id) {
		Session session = sessionFactory.getCurrentSession();
		Transaction txTransaction = session.beginTransaction();
		String sql= "delete from User where id = ?";
		Query query = session.createQuery(sql);
		query.setParameter(0, id);
		int i =query.executeUpdate();
		txTransaction.commit();
		return i;
	}
	public int updateUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		Transaction txTransaction = session.beginTransaction();
		String sql= "update User set sex=?,nickname=?,phone=?,password=?,imageurl=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, user.getSex());
		query.setParameter(1, user.getNickname());
		query.setParameter(2, user.getPhone());
		query.setParameter(3, user.getPassword());
		query.setParameter(4, user.getId()+".jpg");
		int i =query.executeUpdate();
		txTransaction.commit();
		return i;
	}
	public User selectUserById(int id) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from User where id=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, id);
		List<User>users = (List<User>)query.list();
		if(users!=null) {
			return users.get(0);
		}
		return null;
	}
	public User selectUserByNickname(String nickname) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from User where nickname=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, nickname);
		List<User>users = (List<User>)query.list();
		if(users!=null) {
			return users.get(0);
		}
		return null;
	}
	public User selectUserByPhone(String phone) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from User where phone=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, phone);
		List<User>users = (List<User>)query.list();
		if(users!=null) {
			return users.get(0);
		}
		return null;
	}
}

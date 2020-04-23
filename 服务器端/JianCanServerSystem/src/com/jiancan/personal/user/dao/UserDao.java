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
	private Session session = this.sessionFactory.getCurrentSession();
	private Transaction txTransaction = session.beginTransaction();
	public List<User> selectAllUsers(){
		String sql = "from User";
		Query query = session.createQuery(sql);
		return (List<User>)query.list();
	}
	public int insertUser(User user) {
		String sql= "insert into User (sex,nickname,phone,password,imageurl) value(?,?,?,?,?)";
		Query query = session.createQuery(sql);
		query.setParameter(0, user.getSex());
		query.setParameter(1, user.getNickname());
		query.setParameter(2, user.getPhone());
		query.setParameter(3, user.getPassword());
		query.setParameter(4, user.getImageUrl());
		return query.executeUpdate();
	}
	public int deleteUser(int id) {
		String sql= "delete from User where id = ?";
		Query query = session.createQuery(sql);
		query.setParameter(0, id);
		return query.executeUpdate();
	}
	public int updateUser(User user) {
		String sql= "update User set sex=?,nickname=?,phone=?,password=?,imageurl=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, user.getSex());
		query.setParameter(1, user.getNickname());
		query.setParameter(2, user.getPhone());
		query.setParameter(3, user.getPassword());
		query.setParameter(4, user.getImageUrl());
		return query.executeUpdate();
	}
	public User selectUserById(int id) {
		String sql = "from User where id=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, id);
		List<User>users = (List<User>)query.list();
		if(users==null) {
			return users.get(0);
		}
		return null;
	}
	public User selectUserByNickname(String nickname) {
		String sql = "from User where nickname=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, nickname);
		List<User>users = (List<User>)query.list();
		if(users==null) {
			return users.get(0);
		}
		return null;
	}
	public User selectUserByPhone(String phone) {
		String sql = "from User where phone=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, phone);
		List<User>users = (List<User>)query.list();
		if(users==null) {
			return users.get(0);
		}
		return null;
	}
}

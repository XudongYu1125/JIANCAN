package com.jiancan.vegetables.comment.dao;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.jiancan.entity.vegetables.Comment;

import java.util.List;

@Repository
public class CommentDao {

	@Resource
	private SessionFactory sessionFactory;
	
	public Comment saveComment(Comment comment) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			session.save(comment);
			tx.commit();
			
			return comment;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean deleteComment(int id) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			Comment comment = new Comment();
			comment.setId(id);
			
			session.delete(comment);
			tx.commit();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean editComment(Comment comment) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
				
			session.update(comment);
			tx.commit();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public List<Comment> selectCommentByUser(int userId){
		
		try {
			
			Session session = this.sessionFactory.openSession();
	
			String hql = "from Comment c where c.userId = ?";
			
			Query query = session.createQuery(hql);
			query.setParameter(0, userId);
		
			List<Comment> list = query.list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Comment> selectCommentByFood(int foodId) {
		
		try {
			
			Session session = this.sessionFactory.openSession();
	
			String hql = "from Comment c where c.foodId = ?";
			
			Query query = session.createQuery(hql);
			query.setParameter(0, foodId);
		
			List<Comment> list = query.list();
		
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}

package com.jiancan.vegetables.comment.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiancan.entity.vegetables.Comment;
import com.jiancan.vegetables.comment.dao.CommentDao;

@Service
public class CommentService {

	@Resource
	private CommentDao dao;
	
	public Comment addComment(Comment comment) {
		
		return dao.saveComment(comment);
		
	}
	
	public boolean deleteComment(int id) {
		
		return dao.deleteComment(id);
		
	}
	
	public boolean updateComment(Comment comment) {
		
		return dao.editComment(comment);
		
	}
	
	public Comment findCommentById(int id) {
		
		return dao.selectCommentById(id);
		
	}
	
	public List<Comment> findCommentByUser(int userId) {
		
		return dao.selectCommentByUser(userId);
		
	}
	
	public List<Comment> findCommentByFood(int foodId) {
		
		return dao.selectCommentByFood(foodId);
		
	}
	
	public boolean praiseComment(int id) {
		
		Comment comment = findCommentById(id);
		
		comment.setFabulous(comment.getFabulous() + 1);
		
		return dao.editComment(comment);
		
	}
	
	public boolean abolishComment(int id) {
		
		Comment comment = findCommentById(id);
		
		comment.setFabulous(comment.getFabulous() - 1);
		
		return dao.editComment(comment);
		
	}
	
}

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
	
	public List<Comment> findCommentByUser(int userId) {
		
		return dao.selectCommentByUser(userId);
		
	}
	
	public List<Comment> findCommentByFood(int foodId) {
		
		return dao.selectCommentByFood(foodId);
		
	}
	
	
}

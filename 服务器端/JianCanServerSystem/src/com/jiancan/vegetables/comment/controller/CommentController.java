package com.jiancan.vegetables.comment.controller;

import java.io.InputStream;
import java.io.OutputStream;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.jiancan.entity.vegetables.Comment;
import com.jiancan.vegetables.comment.service.CommentService;

@Controller
@RequestMapping("/comment")
public class CommentController {

	@Resource
	private CommentService service;
	
	@RequestMapping("/add")
	public void addComment(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Add Comment");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
					
			InputStream inputStream = request.getInputStream();
			OutputStream outputStream = response.getOutputStream();
			
			byte[] buffer = new byte[2048];
	        int len;
	        StringBuffer stringBuffer = new StringBuffer();
	        while ((len = inputStream.read(buffer)) != -1) {
	            stringBuffer.append(new String(buffer, 0, len));
	        }
			
				
			Gson gson = new Gson();
			Comment comment = gson.fromJson(stringBuffer.toString(), Comment.class);
			
			JSONObject res = new JSONObject();
			
			res.put("isSuccess", service.addComment(comment));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
	}
	
	@RequestMapping("/delete/{Id}")
	public void deleteComment(@PathVariable int Id, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Delete Comment");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.deleteComment(Id));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/update")
	public void updateComment(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Update Comment");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			InputStream inputStream = request.getInputStream();
			OutputStream outputStream = response.getOutputStream();
			
			byte[] buffer = new byte[2048];
	        int len;
	        StringBuffer stringBuffer = new StringBuffer();
	        while ((len = inputStream.read(buffer)) != -1) {
	            stringBuffer.append(new String(buffer, 0, len));
	        }
	        
	        Gson gson = new Gson();
	        Comment comment = gson.fromJson(stringBuffer.toString(), Comment.class);
	        
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.updateComment(comment));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/getByUser/{userId}")
	public void getCommentByUser(@PathVariable int userId, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("GetByUser Comment");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("list", service.findCommentByUser(userId));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/getByFood/{foodId}")
	public void getCommentByFood(@PathVariable int foodId, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("GetByFood Comment");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("list", service.findCommentByFood(foodId));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/praiseComment/{Id}")
	public void praiseComment(@PathVariable int Id, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Praise Comment");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.praiseComment(Id));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}   
		
	}
	
	@RequestMapping("/abolishComment/{Id}")
	public void abolishComment(@PathVariable int Id, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Abolish Comment");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.abolishComment(Id));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}   
		
	}
	
}

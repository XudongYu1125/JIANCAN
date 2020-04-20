package com.jiancan.vegetables.food.controller;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.jiancan.entity.vegetables.Food;
import com.jiancan.vegetables.food.service.FoodService;

@Controller
@RequestMapping("/food")
public class FoodController {

	@Resource
	private FoodService service;
	
	@RequestMapping("/add")
	public void addFood(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Add Food");
		
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
			Food food = gson.fromJson(stringBuffer.toString(), Food.class);
			
			JSONObject res = new JSONObject();
			
			res.put("isSuccess", service.addFood(food));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
	}
	
	@RequestMapping("/delete/{Id}")
	public void deleteFood(@PathVariable int Id, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Delete Food");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.deleteFood(Id));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/update")
	public void updateFood(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Update Food");
		
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
	        Food food = gson.fromJson(stringBuffer.toString(), Food.class);
	        
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.updateFood(food));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/getByUser/{userId}")
	public void getFoodByUser(@PathVariable int userId, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("GetByUser Food");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("list", service.findFoodByUser(userId));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/getByVegetables/{vegetablesId}")
	public void getFoodByVegetables(@PathVariable int vegetablesId, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("GetByVegetables Food");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("list", service.findFoodByVegetables(vegetablesId));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/uploadVideo")
	public void uploadVideo(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("UploadVideo Food");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			
			InputStream inputStream = request.getPart("food").getInputStream();
			OutputStream outputStream = response.getOutputStream();
						
			byte[] buffer = new byte[2048];
	        int len;
	        StringBuffer stringBuffer = new StringBuffer();
	        while ((len = inputStream.read(buffer)) != -1) {
	            stringBuffer.append(new String(buffer, 0, len));
	        }
			
				
			Gson gson = new Gson();
			Food food = gson.fromJson(stringBuffer.toString(), Food.class);
			

		    String rootPath = request.getServletContext().getRealPath("/");
		    
			
			Part video = request.getPart("video");
	        video.write(rootPath + "/upload/" + food.getVideosUrl());
	        
	        Part image = request.getPart("image");
	        image.write(rootPath + "/upload/" + food.getImageUrl());
	        
	       
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.addFood(food));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
}

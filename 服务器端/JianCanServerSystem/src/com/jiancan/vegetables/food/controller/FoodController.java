package com.jiancan.vegetables.food.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.google.gson.Gson;
import com.jiancan.entity.personal.User;
import com.jiancan.entity.vegetables.Food;
import com.jiancan.entity.vegetables.Image;
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
			OutputStream outputStream = response.getOutputStream();
			
		    String rootPath = request.getServletContext().getRealPath(File.separator);		   
	        	    
		    MultipartHttpServletRequest multipartRequest =null;
		    
		    if (request instanceof MultipartHttpServletRequest) {
		    	
		    	multipartRequest = (MultipartHttpServletRequest)(request);
							
				Gson gson = new Gson();
				//获取food user
				Food food = gson.fromJson(multipartRequest.getParameter("foods"), Food.class);
				User user = gson.fromJson(multipartRequest.getParameter("user"), User.class);
				String phone = user.getPhone();
				
				String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date().getTime());
				food.setUploadDate(time);
				
			    List<MultipartFile> images =multipartRequest.getFiles("images");
			    
			    if(images != null) {
			    	
			    	Set<Image> set = new HashSet<Image>();
			    	
			    	String filepath = rootPath + File.separator + "upload" + File.separator + "images";
			    	
			    	File dest = new File(filepath);
			    	
					if (!dest.exists()) {
						dest.mkdirs();
					}
			    	
			    	for(MultipartFile image : images) {
			    		
			    		String fileName = phone+image.getOriginalFilename();
			  
			    		File file = new File(filepath + File.separator + fileName+".png");
			    		
			    		new FileOutputStream(file).write(image.getBytes());
			    		
			    		Image i = new Image();
			    		
			    		i.setImageName(fileName);
			    		
			    		set.add(i);
			    		
			    	}
			    	
			    	food.setImages(set);
			    	
			    }
			    
			    
			    

		        MultipartFile video = ((MultipartHttpServletRequest) request).getFile("video");
		        
		        if(video != null) {
		        	
		        	String filepath = rootPath + File.separator + "upload" + File.separator + "videos";
			    	
			    	File dest = new File(filepath);
			    	
					if (!dest.exists()) {
						dest.mkdirs();
					}
					
					String fileName = phone + "_" + food.getUserId() + "_" + video.getOriginalFilename();
					
					File file = new File(filepath + File.separator + fileName);
		    		
		    		new FileOutputStream(file).write(video.getBytes());
		        	
		    		food.setVideo(fileName);
		    		
		        }
			    
		 
		        
		       
		        JSONObject res = new JSONObject();
		        
		        res.put("isSuccess", service.addFood(food));
				
				outputStream.write(res.toString().getBytes("UTF-8"));
				System.out.println("res:" + res.toString());
		    }
		    
		    
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	
	
	
	
}

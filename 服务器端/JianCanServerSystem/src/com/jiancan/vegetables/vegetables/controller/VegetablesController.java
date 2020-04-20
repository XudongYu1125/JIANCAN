package com.jiancan.vegetables.vegetables.controller;

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
import com.jiancan.entity.vegetables.Vegetables;
import com.jiancan.vegetables.vegetables.service.VegetablesService;

@Controller
@RequestMapping("/vegetables")
public class VegetablesController {
	
	@Resource
	private VegetablesService service;
	
	@RequestMapping("/add")
	public void addVegetables(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Add Vegetables");
		
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
			Vegetables vegetables = gson.fromJson(stringBuffer.toString(), Vegetables.class);
			
			JSONObject res = new JSONObject();
			
			res.put("isSuccess", service.addVegetables(vegetables));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
	}
	
	@RequestMapping("/delete/{Id}")
	public void deleteVegetables(@PathVariable int Id, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Delete Vegetables");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.deleteVegetables(Id));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/update")
	public void updateVegetables(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Update Vegetables");
		
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
	        Vegetables vegetables = gson.fromJson(stringBuffer.toString(), Vegetables.class);
	        
	        JSONObject res = new JSONObject();
	        
	        res.put("isSuccess", service.updateVegetables(vegetables));
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
	@RequestMapping("/getAll")
	public void getAllVegetables(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("GetAll Vegetables");
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			OutputStream outputStream = response.getOutputStream();
			
	        JSONObject res = new JSONObject();
	        
	        res.put("list", service.listVegetables());
			
			outputStream.write(res.toString().getBytes("UTF-8"));
			System.out.println("res:" + res.toString());
	        
	        
		}catch (Exception e) {
			e.printStackTrace();

		}    
		
	}
	
}

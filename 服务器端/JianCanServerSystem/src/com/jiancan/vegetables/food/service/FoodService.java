package com.jiancan.vegetables.food.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiancan.entity.vegetables.Food;
import com.jiancan.vegetables.food.dao.FoodDao;

@Service
public class FoodService {
	
	@Resource
	private FoodDao dao;
	
	public Food addFood(Food food) {
		
		return dao.saveFood(food);
		
	}
	
	public boolean deleteFood(int id) {
		
		return dao.deleteFood(id);
		
	}
	
	public boolean updateFood(Food food) {
		
		return dao.editFood(food);
		
	}
	
	public List<Food> listFood(){
		
		return dao.selectAll();
	}
	
	public Food findFoodById(int id) {
		
		return dao.selectFoodById(id);
		
	}
	
	public List<Food> findFoodByUser(int userId) {
		
		return dao.selectFoodByUser(userId);
		
	}
	
	public List<Food> findFoodByVegetables(int vegetablesId) {
		
		return dao.selectFoodByVegetables(vegetablesId);
		
	}
	
	public boolean praiseFood(int id) {
		
		Food food = findFoodById(id);
		
		food.setFabulous(food.getFabulous() + 1);
		
		return dao.editFood(food);
		
	}
	
	public boolean abolishFood(int id) {
		
		Food food = findFoodById(id);
		
		food.setFabulous(food.getFabulous() - 1);
		
		return dao.editFood(food);
		
	}
	
	
}

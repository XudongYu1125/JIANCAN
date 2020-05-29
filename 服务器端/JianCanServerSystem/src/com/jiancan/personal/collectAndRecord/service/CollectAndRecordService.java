package com.jiancan.personal.collectAndRecord.service;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jiancan.entity.vegetables.Food;
import com.jiancan.personal.collectAndRecord.dao.CollectAndRecordDao;

@Service
public class CollectAndRecordService {
	@Resource
	private CollectAndRecordDao collectAndRecordDao;
	public String add(int userId,int foodId,int type) {
		return collectAndRecordDao.insert(userId, foodId, type)+"";
	}
	public String remove(int userId,int foodId,int type) {
		return collectAndRecordDao.delete(userId, foodId, type)+"";
	}
	public String removeSome(int userId,String foodIds,int type) {
		List<Integer> foodIdList = new Gson().fromJson(foodIds, new TypeToken<List<Integer>>(){}.getType());
		return collectAndRecordDao.deleteSome(userId, foodIdList, type)+"";
	}
	public String findFoods(int userId,int type) {
		List<Food> foods= collectAndRecordDao.selectRecordsByUserId(userId, type);
		if(foods==null) {
			return "0";
		}else {
			return new GsonBuilder().serializeNulls().create().toJson(foods);
		}
	}
}

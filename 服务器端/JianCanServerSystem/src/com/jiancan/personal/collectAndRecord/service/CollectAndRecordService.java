package com.jiancan.personal.collectAndRecord.service;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
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
	public String findFoods(int userId,int type) {
		List<Food> foods= collectAndRecordDao.selectRecordsByUserId(userId, type);
		if(foods==null) {
			return "0";
		}else {
			return new Gson().toJson(foods);
		}
	}
}

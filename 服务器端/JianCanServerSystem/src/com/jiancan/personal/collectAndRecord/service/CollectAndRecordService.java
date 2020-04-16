package com.jiancan.personal.collectAndRecord.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiancan.entity.vegetables.Food;
import com.jiancan.personal.collectAndRecord.dao.CollectAndRecordDao;

@Service
public class CollectAndRecordService {
	@Resource
	private CollectAndRecordDao collectAndRecordDao;
	public int add(int userId,int foodId,int type) {
		return collectAndRecordDao.insert(userId, foodId, type);
	}
	public int remove(int userId,int foodId,int type) {
		return collectAndRecordDao.delete(userId, foodId, type);
	}
	public List<Food> findFoods(int userId,int type) {
		return collectAndRecordDao.selectRecordsByUserId(userId, type);
	}
}

package com.jiancan.personal.collectAndRecord.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiancan.personal.collectAndRecord.service.CollectAndRecordService;

@Controller
@ResponseBody
@RequestMapping("/car")
public class CollectAndRecordController {
	@Resource
	CollectAndRecordService collectAndRecordService;
	//通过userId和foodId添加收藏
	@RequestMapping(value = "/ac/{userId}/{foodId}",method = RequestMethod.GET)
	public String addCollect(@PathVariable int userId,@PathVariable int foodId) {	
		System.out.println("//通过userId和foodId添加收藏");
		return collectAndRecordService.add(userId, foodId, 0);
	}
	//通过userId和foodId删除收藏
	@RequestMapping(value = "/rc/{userId}/{foodId}",method = RequestMethod.GET)
	public String removeCollect(@PathVariable int userId,@PathVariable int foodId) {
		System.out.println("//通过userId和foodId删除收藏");
		return collectAndRecordService.remove(userId, foodId, 0);
	}
	//通过userId查询收藏
	@RequestMapping(value = "/fc/{userId}",method = RequestMethod.GET)
	public String findCollectFoods(@PathVariable int userId) {
		System.out.println("//通过userId查询收藏");
		return collectAndRecordService.findFoods(userId, 0);
	}
	//通过userId和foodId添加历史记录
	@RequestMapping(value = "/ar/{userId}/{foodId}",method = RequestMethod.GET)
	public String addRecord(@PathVariable int userId,@PathVariable int foodId) {
		System.out.println("//通过userId和foodId添加历史记录");
		return collectAndRecordService.add(userId, foodId, 1);
	}
	//通过userId和foodId删除历史记录
	@RequestMapping(value = "/rr/{userId}/{foodId}",method = RequestMethod.GET)
	public String removeRecord(@PathVariable int userId,@PathVariable int foodId) {
		System.out.println("//通过userId和foodId删除历史记录");
		return collectAndRecordService.remove(userId, foodId, 1);
	}
	//通过userId和foodIdList批量删除历史记录
	@RequestMapping(value = "/rsrs/{userId}/{foodIds}",method = RequestMethod.GET)
	public String removeSomeRecords(@PathVariable int userId,@PathVariable String foodIds) {
		System.out.println("//通过userId和foodIdList批量删除历史记录");
		return collectAndRecordService.removeSomeRecords(userId, foodIds, 1);
	}
	//通过userId查询历史记录
	@RequestMapping(value = "/fr/{userId}",method = RequestMethod.GET)
	public String findRecordFoods(@PathVariable int userId) {
		System.out.println("//通过userId查询历史记录");
		return collectAndRecordService.findFoods(userId, 1);
	}
}

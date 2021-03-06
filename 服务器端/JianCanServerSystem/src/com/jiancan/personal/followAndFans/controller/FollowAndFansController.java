package com.jiancan.personal.followAndFans.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiancan.personal.followAndFans.service.FollowAndFansService;

@Controller
@RequestMapping(value = "/faf",produces="application/json;charset=UTF-8")
@ResponseBody
public class FollowAndFansController {
	@Resource
	private FollowAndFansService followAndFansService;
	//通过userId获得关注列表
	@RequestMapping(value = "/fwl/{userId}",method =RequestMethod.GET )
	public String followList(@PathVariable int userId){
		System.out.println("//通过userId获得关注列表");
		return followAndFansService.findFollows(userId);
	}
	//通过userId获得粉丝列表
	@RequestMapping(value = "/fsl/{userId}",method = RequestMethod.GET)
	public String fansList(@PathVariable int userId){
		System.out.println("//通过userId获得粉丝列表");
		return followAndFansService.findFanss(userId);
	}
	//通过userId和followId添加关注
	@RequestMapping(value = "/af/{userId}/{followId}",method = RequestMethod.GET)
	public String addFollow(@PathVariable int userId,@PathVariable int followId) {
		System.out.println("//通过userId和followId添加关注");
		return followAndFansService.addFollow(userId, followId);
	}
	//通过userId和followId取消关注
	@RequestMapping(value = "/rf/{userId}/{followId}",method = RequestMethod.GET)
	public String removeFollow(@PathVariable int userId,@PathVariable int followId) {
		System.out.println("//通过userId和followId取消关注");
		return followAndFansService.removeFollow(userId, followId);
	}
	
}

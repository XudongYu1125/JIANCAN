package com.jiancan.personal.user.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.jiancan.entity.personal.User;
import com.jiancan.personal.user.dao.UserDao;

@Service
public class UserService {
	@Resource
	private UserDao userDao;
	//注册用户
	//返回0代表注册失败
	public String addUser(String userGson) {
		User user = new Gson().fromJson(userGson, User.class);
		return userDao.insertUser(user)+"";
	}
	//注销用户
	//返回0代表注销失败
	public String removeUser(String id) {
		return userDao.deleteUser(Integer.parseInt(id))+"";
	}
	//修改用户信息
	//返回0代表修改失败
	public String editUser(String userGson) {
		User user = new Gson().fromJson(userGson, User.class);
		return userDao.updateUser(user)+"";
	}
	//通过昵称登录
	//返回值0代表用户不存在，1代表密码错误
	public String loginByNickname(String nickname,String password) {
		User user = userDao.selectUserByNickname(nickname);
		if(user==null) {
			return "0";
		}else if(user.getPassword()==password) {
			return new Gson().toJson(user);
		}else {
			return "1";
		}
	}
	//通过手机号登录
	//返回值0代表用户不存在，1代表密码错误
	public String loginByPhone(String phone) {
		User user = userDao.selectUserByPhone(phone);
		if(user==null) {
			return "0";
		}else {
			return new Gson().toJson(user);
		}
	}
	//通过id查找用户
	//返回值0代表用户不存在
	public String findUserById(String id) {
		User user = userDao.selectUserById(Integer.parseInt(id));
		if(user==null) {
			return "0";
		}else{
			return new Gson().toJson(user);
		}
	}
}

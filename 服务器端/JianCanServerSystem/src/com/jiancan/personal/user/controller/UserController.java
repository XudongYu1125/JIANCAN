package com.jiancan.personal.user.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiancan.personal.user.service.UserService;

@Controller
@ResponseBody
@RequestMapping(value = "/user",produces="application/json;charset=UTF-8")
public class UserController {
	@Resource
	private UserService userService;
	//注册用户
	@RequestMapping(value = "/register/{userGson}",method = RequestMethod.GET)
	public String register(@PathVariable String userGson) {
		System.out.println("//注册用户");
		return userService.addUser(userGson);
	}
	//注销用户
	@RequestMapping(value = "/cancel/{id}",method = RequestMethod.GET)
	public String cancel(@PathVariable String id) {
		System.out.println("//注销用户");
		return userService.removeUser(id);
	}
	//修改用户信息
	@RequestMapping(value = "/edit/{userGson}",method = RequestMethod.GET)
	public String edit(@PathVariable String userGson) {
		System.out.println("//修改用户信息");
		return userService.editUser(userGson);
	}
	//通过昵称登录
	@RequestMapping(value = "/loginbn/{nickname}/{password}",method = RequestMethod.GET)
	public String loginByNickname(@PathVariable String nickname,@PathVariable String password) {
		System.out.println("//通过昵称登录");
		return userService.loginByNickname(nickname, password);
	}
	//通过手机号登录
	@RequestMapping(value = "/loginbp/{phone}",method = RequestMethod.GET)
	public String loginByPhone(@PathVariable String phone) {
		System.out.println("//通过手机号登录");
		return userService.loginByPhone(phone);
	}
	//上传头像
	@RequestMapping(value = "/upload/{imgname}",method = RequestMethod.POST)
	public void uploadAvatar(@PathVariable String imgname,HttpServletRequest request) throws IOException {
		try {
			request.setCharacterEncoding("utf-8");
			System.out.println("upload...............................");
			InputStream input =  request.getInputStream();
			String realPath = request.getServletContext().getRealPath(File.separator);
			File file = new File(realPath + File.separator +"upload"+  File.separator +"avatarimgs"+ File.separator + imgname+".jpg");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] a = new byte[1024];
			int len;
			while ((len = input.read(a)) != -1) {
				fileOutputStream.write(a,0,len);
			}
			input.close();
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	//通过userId获得user
	@RequestMapping(value = "/getu/{id}",method = RequestMethod.GET)
	public String getUser(@PathVariable String id) {
		System.out.println("//通过userId获得user");
		return userService.findUserById(id);
	}
	@RequestMapping(value = "/getatu",method = RequestMethod.GET)
	public String getTUsers() {
		System.out.println("查询所有用户，TUser");
		return userService.findAllTUsers();
	}
}

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
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserService userService;
	//注册用户
	@RequestMapping(value = "/register/{userGson}",method = RequestMethod.GET)
	public String register(@PathVariable String userGson) {
		return userService.addUser(userGson);
	}
	//注销用户
	@RequestMapping(value = "/cancel/{id}",method = RequestMethod.GET)
	public String cancel(@PathVariable String id) {
		return userService.removeUser(id);
	}
	//修改用户信息
	@RequestMapping(value = "/edit/{userGson}",method = RequestMethod.GET)
	public String edit(@PathVariable String userGson) {
		return userService.editUser(userGson);
	}
	//通过昵称登录
	@RequestMapping(value = "/loginbn/{nickname}/{password}",method = RequestMethod.GET)
	public String loginByNickname(@PathVariable String nickname,@PathVariable String password) {
		return userService.loginByNickname(nickname, password);
	}
	//通过手机号登录
	@RequestMapping(value = "/loginbp/{phone}/{password}",method = RequestMethod.GET)
	public String loginByPhone(@PathVariable String phone,@PathVariable String password) {
		return userService.loginByPhone(phone, password);
	}
	//上传头像
	@RequestMapping(value = "/uploada/{imgname}",method = RequestMethod.GET)
	public void uploadAvatar(@PathVariable String imgname,HttpServletRequest request) throws IOException {
		String str = request.getServletContext().getRealPath("/avatarimg");
		System.out.println(str);
		if(!(imgname==null)) {
			InputStream input =  request.getInputStream();
			String realPath = request.getServletContext().getRealPath("/avatarimg");
			File file = new File(realPath + "/" + imgname);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] a = new byte[1024];
			int len;
			while ((len = input.read(a)) != -1) {
				fileOutputStream.write(a,0,len);
			}
			input.close();
			fileOutputStream.flush();
			fileOutputStream.close();
		}
	}
	//通过userId获得user
	@RequestMapping(value = "/getu/{id}",method = RequestMethod.GET)
	public String getUser(@PathVariable String id) {
		return userService.findUserById(id);
	}
}

package com.example.user.jiancan;

public class Constant {
    public static final String BASE_IP = "";
    public static final String BASE_URL= BASE_IP + "JianCanServerSystem";
    //上传头像
    public static final String URL_UPLOAD_PIC = BASE_URL + "/user/uploada/";
    //更改用户名字
    public static final String URL_CHANGE_NAME = BASE_URL + "/user/edit/";
    //获取动态列表
    public static final String URL_TRENDS = BASE_URL + "";
    //获取关注列表
    public static final String URL_FOLLOW_LIST = BASE_URL + "/faf/fwl/";
    //获取粉丝列表
    public static final String URL_FAN = BASE_URL + "/faf/fsl/";
    //获取收藏列表
    public static final String URL_COLLECTION = BASE_URL + "/car/ar/";
    //获取历史记录
    public static final String URL_HISTORY = BASE_URL + "/car/fr/";
    //取消关注
    public static final String URL_UNFOLLOW = BASE_URL + "/faf/rf/";
    //关注
    public static final String URL_FOLLOW = BASE_URL + "/faf/af/";

}

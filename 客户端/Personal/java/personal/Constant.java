package com.example.user.jiancan;

public class Constant {
    public static final String BASE_IP = "http://39.97.3.111:8080/";
    public static final String BASE_URL= BASE_IP + "JianCanServerSystem";
    //上传头像
    public static final String URL_UPLOAD_PIC = BASE_URL + "/user/upload/";
    //更改用户名字
    public static final String URL_CHANGE_NAME = BASE_URL + "/user/edit/";
    //获取动态列表
    public static final String URL_TRENDS = BASE_URL + "/food/getByUser/";
    //获取关注列表
    public static final String URL_FOLLOW_LIST = BASE_URL + "/faf/fwl/";
    //获取粉丝列表
    public static final String URL_FAN = BASE_URL + "/faf/fsl/";
    //获取收藏列表
    public static final String URL_COLLECTION = BASE_URL + "/car/fc/";
    //获取历史记录
    public static final String URL_HISTORY = BASE_URL + "/car/fr/";
    //取消关注
    public static final String URL_UNFOLLOW = BASE_URL + "/faf/rf/";
    //关注
    public static final String URL_FOLLOW = BASE_URL + "/faf/af/";
    //删除一个收藏
    public static final String URL_DELETE_COLLECTION= BASE_URL + "/car/rc/";
    //批量删除收藏
    public static final String URL_DELETE_COLLECTIONS= BASE_URL + "/car/rc/";
    //删除历史记录
    public static final String URL_DELETE_HISTORY= BASE_URL + "/car/rr/";
    //批量删除历史记录
    public static final String URL_DELETE_HISTORYS= BASE_URL + "/car/rsrs/";
    //修改用户
    public static final String URL_CHANGE_USER= BASE_URL + "/user/edit/";
    //删除动态
    public static final String URL_DELETE_TREND= BASE_URL + "/food/delete/";
}

package com.example.user.jiancan;

public class Constant {
    public static  String HOME_BASE_URL ="http://39.97.3.111:8080/JianCanServerSystem/food/";
    public static final String BASE_IP = "http://39.97.3.111:8080/";
    public static final String BASE_URL= BASE_IP + "JianCanServerSystem";
    //用户
    public static final String PERSONAL_USER_URL = BASE_URL+"/user/";
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
    //添加历史记录
    public static final String RECORD_ADD_URL= BASE_URL +"/car/ar/";
    //删除历史记录
    public static final String URL_DELETE_HISTORY= BASE_URL + "/car/rr/";
    //批量删除历史记录
    public static final String URL_DELETE_HISTORYS= BASE_URL + "/car/rsrs/";
    //修改用户
    public static final String URL_CHANGE_USER= BASE_URL + "/user/edit/";
    //删除动态
    public static final String URL_DELETE_TREND= BASE_URL + "/food/delete/";
    //下载
    public static final String URL_DOWNLOAD_LIST= BASE_URL + "/car/fd/";
    //下载删除
    public static final String URL_DELETE_DOWNLOAD= BASE_URL + "/car/rd/";



    /**获取所有菜列表 无需参数*/
    public static final String URL_ALL_FOOD = BASE_URL + "/food/getAll";
    /**获取用户信息  需要参数：userId*/
    public static final String URL_USER_INFO = BASE_URL + "/user/getu/";
    /**通过菜id获取评论列表  需要参数：foodId*/
    public static final String URL_COMMENT_BY_FOOD_ID = BASE_URL + "/comment/getByFood/";
    /**通过菜的种类获取菜品列表 参数：菜的种类id*/
    public static final String URL_FOOD_CATEGORY = BASE_URL + "/food/getByVegetables/";
    /**通过用户id获取菜品列表*/
    public static final String URL_GET_FOOD_BY_USER = BASE_URL + "/food/getByUser/";
    /**点赞评论 参数：评论id*/
    public static final String URL_COMMENT_LIKE = BASE_URL + "/comment/praiseComment/";
    /**取消赞评论 参数：评论id*/
    public static final String URL_COMMENT_UNLIKE = BASE_URL + "/comment/abolishComment/";
    /**点赞菜品 参数：菜品id*/
    public static final String URL_FOOD_LIKE = BASE_URL + "/food/praiseFood/";
    /**取消赞菜品 参数：菜品id*/
    public static final String URL_FOOD_UNLIKE = BASE_URL + "/food/abolishFood/";

    /**上传下载记录 参数：userid  ， foodid*/
    public static final String URL_UPLOAD_DOWNLOAD_DATA = BASE_URL + "/car/ad/";
    /**获取所有用户*/
    public static final String URL_GET_ALL_USER = "http://39.97.3.111:8080/JianCanServerSystem/user/getatu";
    /**通过foodID查food 参数：foodID*/
    public static final String URL_GET_FOOD_BY_ID = BASE_URL + "/food/getById/";
}

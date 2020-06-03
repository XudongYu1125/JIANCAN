package com.example.user.jiancan.home.entity;
import java.util.Set;

/**
 * 首页列表item实体bean
 * @author 于旭东
 */
public class  HomeListItemBean {

    private int type;
    /**食品ID*/
    public int foodId;
    /**食品名*/
    public String name;
    /**用户名*/
    public String user;
    /**点赞数*/
    public String likeNum;
    /**封面图*/
    public String showImg;
    /**用户id*/
    public int userId;
    /**用户头像*/
    public String userLogo;
    /**视频地址*/
    public String videoUrl;
    /**标题*/
    public String title;
    /**介绍内容*/
    public String content;
    /**图片集*/
    public Set<Image> images;
    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId( int foodId ) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser( String user ) {
        this.user = user;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum( String likeNum ) {
        this.likeNum = likeNum;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg( String showImg ) {
        this.showImg = showImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId( int userId ) {
        this.userId = userId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl( String videoUrl ) {
        this.videoUrl = videoUrl;
    }

    public Set <Image> getImages() {
        return images;
    }

    public void setImages( Set <Image> images ) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    @Override
    public String toString() {
        return "HomeListItemBean{" +
                "type=" + type +
                ", foodId=" + foodId +
                ", name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", likeNum='" + likeNum + '\'' +
                ", showImg='" + showImg + '\'' +
                ", userId=" + userId +
                ", userLogo=" + userLogo +
                ", videoUrl='" + videoUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", images=" + images +
                '}';
    }
}

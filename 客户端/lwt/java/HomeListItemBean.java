package com.example.user.jiancan.bean;

/**
 * 首页列表item实体bean
 * @author june
 */
public class HomeListItemBean {
    public String name;
    public String user;
    public String likeNum;
    public int showImg;

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

    public int getShowImg() {
        return showImg;
    }

    public void setShowImg( int showImg ) {
        this.showImg = showImg;
    }

    @Override
    public String toString() {
        return "HomeListItemBean{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", likeNum='" + likeNum + '\'' +
                ", showImg=" + showImg +
                '}';
    }
}

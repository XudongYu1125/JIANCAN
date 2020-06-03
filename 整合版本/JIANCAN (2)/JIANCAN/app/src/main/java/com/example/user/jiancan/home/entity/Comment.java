package com.example.user.jiancan.home.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author june
 */
@Data
public class Comment {
    private int id;
    private int foodId;
    /**发表评论的用户id*/
    private int userId;
    /**所属评论id*/
    private int parentId;
    private int fabulous;
    private String content;
    private Date date;
    /**回复的id*/
    private int replyId;

    public Comment() {

    }

    public Comment( int id , int foodId , int userId ,int fabulous , String content , Date date ,int replyId ) {
        this.id = id;
        this.foodId = foodId;
        this.userId = userId;
        this.fabulous = fabulous;
        this.content = content;
        this.date = date;
        this.replyId = replyId;
    }

    public Comment( int id , int foodId , int userId , int parentId , int fabulous , String content , Date date ,int replyId ) {
        this.id = id;
        this.foodId = foodId;
        this.userId = userId;
        this.parentId = parentId;
        this.fabulous = fabulous;
        this.content = content;
        this.date = date;
        this.replyId = replyId;
    }
}

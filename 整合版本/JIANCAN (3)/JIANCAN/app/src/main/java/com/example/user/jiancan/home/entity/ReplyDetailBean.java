package com.example.user.jiancan.home.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author june
 * 回复实体类
 */
@Data
public class ReplyDetailBean {
    /**数据库中存储的的id*/
    private int id;
    /**回复的那个用户的id*/
    private int replyId;
    /**所属评论的ID*/
    private int parentId;

    private String nickName;
    private String imageUrl;
    private String content;
    private String replyToOtherUser;
    private String date;

    public ReplyDetailBean() {
    }

    public ReplyDetailBean( String nickName, String replyToOtherUser, String content){
        this.nickName = nickName;
        this.replyToOtherUser = replyToOtherUser;
        this.content = content;
    }


}

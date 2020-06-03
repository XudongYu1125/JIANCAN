package com.example.user.jiancan.home.activityAndFragment;

import java.util.List;

import lombok.Data;

/**
 * @author june
 * 详细的评论实体类
 */
@Data
public class CommentDetailBean {
    private int id;
    private String nickName;
    private String userLogo;
    private String content;
    private String date;
    private int likeNum;
    private List<ReplyDetailBean> replyList;

    public CommentDetailBean(){}

    public CommentDetailBean(String nickName, String content, String date){
        this.nickName = nickName;
        this.content = content;
        this.date = date;
    }
}

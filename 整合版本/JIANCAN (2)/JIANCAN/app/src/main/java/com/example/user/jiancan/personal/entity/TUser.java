package com.example.user.jiancan.personal.entity;

import java.util.Objects;

public class TUser {
    private int  id;
    private String sex;
    private String nickname;
    private String imageUrl;

    public TUser() {
    }

    public TUser( int id , String nickname , String imageUrl ) {
        this.id = id;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TUser tUser = (TUser) o;
        return id == tUser.id &&
                Objects.equals(sex, tUser.sex) &&
                Objects.equals(nickname, tUser.nickname) &&
                Objects.equals(imageUrl, tUser.imageUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sex, nickname, imageUrl);
    }

    @Override
    public String toString() {
        return "TUser{" +
                "id=" + id +
                ", sex='" + sex + '\'' +
                ", nickname='" + nickname + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

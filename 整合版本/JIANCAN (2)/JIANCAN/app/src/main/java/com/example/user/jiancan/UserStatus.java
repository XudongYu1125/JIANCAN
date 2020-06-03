package com.example.user.jiancan;

import com.example.user.jiancan.personal.entity.User;

public class UserStatus {
    private String status;
    private User user;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.kiddygambles.wrappers;


import lombok.Getter;

@Getter
public class LoginWrapper {
    private String username;
    private String password;

    public LoginWrapper() {

    }

    public LoginWrapper(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

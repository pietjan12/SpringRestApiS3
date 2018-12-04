package com.kiddygambles.domain.principal;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUser extends User {
    @Getter
    private int userID;

    public JwtUser(int userID, String username, String password,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username,password, authorities);
        this.userID = userID;
    }
}

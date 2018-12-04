package com.kiddygambles.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//Account class to store data from authorization server, also stores gambling specific token balance.
@Entity
@Getter
@Setter
public class Account {
    @Id
    private int accountID;
    private String username;
    //Custom currency for gambling
    private int tokens;

    public Account() {

    }

    public Account(int accountID, String username) {
        this.accountID = accountID;
        this.username = username;
    }
}

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private int accountID;
    private String username;
    //Custom currency for gambling
    private int tokens;

    public Account() {

    }
}

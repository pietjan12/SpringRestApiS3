package com.kiddygambles.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

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
}

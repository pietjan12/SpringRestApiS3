package com.kiddygambles.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;

    @JsonProperty("password")
    @JsonIgnore
    private String password;
    //Custom currency op gambling platform.
    private int tokens;

    //Alle skins die de gebruiker momenteel heeft. TODO: KOMT IN DE TOEKOMST VANUIT MARTIJN ZIJN MODULE
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "accountItems",
            joinColumns = {@JoinColumn(name = "AccountID")},
            inverseJoinColumns = {@JoinColumn(name = "ItemID")}
    )
    private List<Item> items = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public List<Item> getItems() {
        return items;
    }
}

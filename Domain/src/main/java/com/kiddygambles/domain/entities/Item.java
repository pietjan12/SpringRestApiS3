package com.kiddygambles.domain.entities;

import com.kiddygambles.domain.enums.Condition;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private Condition condition;
    private Float price;

    //Cases waarvan dit item een onderdeel is.
    @ManyToMany(fetch = FetchType.LAZY ,cascade = CascadeType.PERSIST, mappedBy = "items")
    private Set<Case> cases = new HashSet<>();

    //Accounts waarbij dit item tot de inventory behoord.
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "items")
    private Set<Account> accounts = new HashSet<>();

    public Item() {}

    public Item(String name, String description, Condition condition, Float price){
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Condition getCondition() {
        return condition;
    }

    public Float getPrice() {
        return price;
    }

}
package com.kiddygambles.domain.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private Float price;

    //Items die behoren tot bepaalde cases
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name="caseitems",
            joinColumns = {@JoinColumn(name = "CaseID")},
            inverseJoinColumns = {@JoinColumn(name = "ItemID")}
    )
    private Set<Item> items = new HashSet<>();

    public Case() {

    }

    public Case(String name, String description, Float price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Set<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}

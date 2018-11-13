package com.kiddygambles.domain.entities;

import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//item entity used to store inventory API data, mainly linking item IDS to cases.
@Entity
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int itemID;

    //Cases waarvan dit item een onderdeel is.
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<Case> cases = new HashSet<>();

    public Item() {}

    public Item(int itemID){
        this.itemID = itemID;
    }
}

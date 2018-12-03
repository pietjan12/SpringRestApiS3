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
    private int itemID;

    //Cases waarvan dit item een onderdeel is.
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<Case> cases = new HashSet<>();

    @OneToMany(mappedBy = "item")
    private Set<CaseHistory> winHistories;

    public Item() {}

    public Item(int itemID){
        this.itemID = itemID;
    }
}

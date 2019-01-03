package com.kiddygambles.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kiddygambles.domain.Enum.Rarity;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//item entity used to store inventory API data, mainly linking item IDS to cases.
@Entity
@Getter
public class Item {
    @Id
    private int itemID;

    private Rarity rarity;

    //Cases waarvan dit item een onderdeel is.
    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private List<Case> cases = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "item")
    private Set<CaseHistory> winHistories;

    public Item() {}

    public Item(int itemID, Rarity rarity){
        this.itemID = itemID;
        this.rarity = rarity;
    }
}

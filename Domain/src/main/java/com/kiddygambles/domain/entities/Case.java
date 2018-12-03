package com.kiddygambles.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Lootcase")
@Getter
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private int price;

    //Items die behoren tot bepaalde cases
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="caseitems",
            joinColumns = {@JoinColumn(name = "CaseID")},
            inverseJoinColumns = {@JoinColumn(name = "ItemID")}
    )
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "wonCase")
    private Set<CaseHistory> history;

    public Case() {

    }

    public Case(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}

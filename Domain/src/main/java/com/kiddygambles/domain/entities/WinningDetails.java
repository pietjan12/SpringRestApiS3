package com.kiddygambles.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class WinningDetails {
    private Item item;
    private Double rolledNumber;

    public WinningDetails(Item item, Double rolledNumber) {
        this.item = item;
        this.rolledNumber = rolledNumber;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case wonCase;
}

package com.kiddygambles.domain.entities;

public class WinningDetails {
    private Item item;
    private Double rolledNumber;

    public WinningDetails(Item item, Double rolledNumber) {
        this.item = item;
        this.rolledNumber = rolledNumber;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getRolledNumber() {
        return rolledNumber;
    }

    public void setRolledNumber(Double rolledNumber) {
        this.rolledNumber = rolledNumber;
    }
}

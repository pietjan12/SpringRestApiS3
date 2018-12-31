package com.kiddygambles.domain.DTO;

import com.kiddygambles.domain.Enum.Rarity;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ItemDTO {
    private int itemID;
    private String name;
    private int price;
    @Setter
    private Rarity rarity;

    public ItemDTO() {

    }

    public ItemDTO(int itemID, String name, int price) {
        this.itemID = itemID;
        this.name = name;
        this.price = price; }
}

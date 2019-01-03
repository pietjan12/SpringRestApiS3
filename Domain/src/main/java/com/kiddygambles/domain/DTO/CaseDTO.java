package com.kiddygambles.domain.DTO;

import com.kiddygambles.domain.entities.CaseHistory;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class CaseDTO {
    private int id;
    private String name;
    private String description;
    private String image;
    private int price;
    private List<ItemDTO> items;
    private List<CaseHistoryDTO> history;

    public CaseDTO(int id, String name, String description, String image, int price, List<ItemDTO> items, List<CaseHistoryDTO> history) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.items = items;
        this.history = history;
    }
}

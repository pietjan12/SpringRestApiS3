package com.kiddygambles.domain.DTO;

import lombok.Getter;

@Getter
public class CaseHistoryDTO {
    private int id;
    private Double rolledNumber;
    private ItemDTO item;

    public CaseHistoryDTO(int id, Double rolledNumber, ItemDTO item) {
        this.id = id;
        this.rolledNumber = rolledNumber;
        this.item = item;
    }
}

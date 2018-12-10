package com.kiddygambles.wrappers;

import lombok.Getter;

@Getter
public class CaseRequestModel {
    private String name;
    private String description;
    private int price;

    public CaseRequestModel() {

    }

    public CaseRequestModel(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}

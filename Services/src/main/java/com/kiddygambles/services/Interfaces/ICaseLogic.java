package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.Item;

import java.util.List;

public interface ICaseLogic {
    Case getCase(int id);
    Iterable<Case>  getAllCases();
    Case createCase(String caseName, String caseDescription, int price);
    List<Item> getWinHistory(int caseID);
}

package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.Item;

import java.util.List;

public interface ICaseLogic {
    Case getCase(int id);
    Iterable<Case>  getAllCases();
    void createCase(Case caseToCreate);
    List<Item> getWinHistory(int caseID);
}

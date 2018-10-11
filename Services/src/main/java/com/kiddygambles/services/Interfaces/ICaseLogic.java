package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.Case;

public interface ICaseLogic {
    Case getCase(int id);
    Iterable<Case>  getAllCases();
}

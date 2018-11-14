package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.WinHistory;

public interface IGamblingLogic {
    WinHistory openCase(String username, String token, int caseID);
}

package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.GameHistory;

public interface IHiLowLogic {
    public GameHistory playHiLow(String username, int betAmount, int currentCardNumber, boolean higher);
}

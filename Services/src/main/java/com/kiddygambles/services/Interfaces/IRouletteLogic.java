package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.GameHistory;

public interface IRouletteLogic {
    GameHistory playRoulette(String username, int betAmount, Integer intChoice, String stringChoice);
}

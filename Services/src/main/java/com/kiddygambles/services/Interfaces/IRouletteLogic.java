package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.GameHistory;

public interface IRouletteLogic {
    /**
     * Play a game of roulette
     * @param username the user who is playing
     * @param betAmount the amount the user wants to bet
     * @param intChoice the number choice the user wanted
     * @param stringChoice the color choice the user wanted
     * @return a gamehistory with the results.
     * @throws IllegalArgumentException if anything is wrong with the given input of the user does not have enough tokens.
     */
    GameHistory playRoulette(String username, int betAmount, Integer intChoice, String stringChoice);
}

package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.GameHistory;

public interface IHiLowLogic {
    /**
     * Start a higher/lower game roll
     * @param username the user playing the game
     * @param betAmount the amount of tokens the user wants to bet
     * @param currentCardNumber the card that's currently visible
     * @param higher the choice the user made
     * @return a gamehistory with the results of the roll
     * @throws IllegalArgumentException if user does not have enough tokens , or any of the parameters are missing.
     */
    public GameHistory playHiLow(String username, int betAmount, int currentCardNumber, boolean higher);
}

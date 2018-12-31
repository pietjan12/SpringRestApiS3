package com.kiddygambles.services;

import com.kiddygambles.domain.entities.GameHistory;
import com.kiddygambles.services.Constants.RouletteConstants;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Helper.TokenHelper;
import com.kiddygambles.services.Interfaces.IRouletteLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouletteLogic implements IRouletteLogic {
    private LootRollHelper lootRollHelper;
    private TokenHelper tokenHelper;

    @Autowired
    public RouletteLogic(LootRollHelper lootRollHelper, TokenHelper tokenHelper) {
        this.lootRollHelper = lootRollHelper;
        this.tokenHelper = tokenHelper;
    }

    @Override
    public GameHistory playRoulette(String username, int betAmount, Integer intChoice, String stringChoice) {
        //check if values are present
        if(intChoice == null && stringChoice == null) {
            throw new IllegalArgumentException("No bet choice given!");
        }
        //check if the bet is valid
        tokenHelper.hasEnoughTokens(username, betAmount);
        tokenHelper.removeTokens(username, betAmount);

        //make a roulette roll, values between 0 - 36
        int rolledNumber = lootRollHelper.getRandomIntRoll(0, 36);
        GameHistory history = new GameHistory();
        history.setRolledNumber(rolledNumber);

        //Most valued win first
        if (intChoice.equals(rolledNumber)) {
            //won 35 : 1
            history.setWon(true);
            history.setWonTokens(betAmount * 35);
            tokenHelper.addTokens(username, history.getWonTokens());
        } else if(stringChoice.equals(RouletteConstants.numberWithColors.get(rolledNumber))) {
            history.setWon(true);
            history.setWonTokens(betAmount * 2);
            tokenHelper.addTokens(username, history.getWonTokens());
        }

        return history;
    }
}

package com.kiddygambles.services;

import com.kiddygambles.domain.entities.GameHistory;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Helper.TokenHelper;
import com.kiddygambles.services.Interfaces.IHiLowLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HiLowLogic implements IHiLowLogic {
    private LootRollHelper lootRollHelper;
    private TokenHelper tokenHelper;

    @Autowired
    public HiLowLogic(LootRollHelper lootRollHelper, TokenHelper tokenHelper) {
        this.lootRollHelper = lootRollHelper;
        this.tokenHelper = tokenHelper;
    }

    @Override
    public GameHistory playHiLow(String username, int betAmount, int currentCardNumber, boolean higher) {
        //check if bet is valid and take tokens from user
        tokenHelper.hasEnoughTokens(username, betAmount);

        //roll a random number and determine outcome.
        int rolledNumber = lootRollHelper.getRandomIntRoll(0, 12);
        boolean outcome = rolledNumber > currentCardNumber;

        //create game history
        GameHistory gameHistory = new GameHistory();
        gameHistory.setRolledNumber(rolledNumber);

        if(higher == outcome) {
            //won 2:1
            gameHistory.setWon(true);
            gameHistory.setWonTokens(betAmount * 2);
            //add tokens to user.

            tokenHelper.addTokens(username, gameHistory.getWonTokens());
        }

        return gameHistory;
    }
}

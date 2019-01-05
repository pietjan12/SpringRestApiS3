package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.entities.GameHistory;
import com.kiddygambles.services.Constants.RouletteConstants;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Interfaces.IRouletteLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RouletteLogic implements IRouletteLogic {
    private IAccountRepository accountContext;
    private LootRollHelper lootRollHelper;

    @Autowired
    public RouletteLogic(IAccountRepository accountContext, LootRollHelper lootRollHelper) {
        this.accountContext = accountContext;
        this.lootRollHelper = lootRollHelper;
    }

    @Override
    public GameHistory playRoulette(String username, int betAmount, Integer intChoice, String stringChoice) {
        //check if values are present
        if(intChoice == null && stringChoice == null) {
            throw new IllegalArgumentException("No bet choice given!");
        }

        Account account = getUser(username);
        //check if user has enough tokens
        if(betAmount > account.getTokens()) {
            throw new IllegalArgumentException("User does not have enough tokens!");
        }

        //Remove tokens from user
        account.setTokens(account.getTokens() - betAmount);

        //make a roulette roll, values between 0 - 36
        int rolledNumber = lootRollHelper.getRandomIntRoll(0, 36);
        GameHistory history = new GameHistory();
        history.setRolledNumber(rolledNumber);

        //Most valued win first
        if (intChoice != null && intChoice.equals(rolledNumber)) {
            //won 35 : 1
            history.setWon(true);
            history.setWonTokens(betAmount * 35);

            //add tokens to user
            account.setTokens(account.getTokens() + history.getWonTokens());
        } else if(stringChoice.equals(RouletteConstants.numberWithColors.get(rolledNumber))) {
            history.setWon(true);
            history.setWonTokens(betAmount * 2);
            //add tokens to user
            account.setTokens(account.getTokens() + history.getWonTokens());
        }

        //update account token balance
        accountContext.save(account);

        return history;
    }

    private Account getUser(String username) {
        Optional<Account> foundAccount = accountContext.findByUsername(username);

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("Account with username + " + username + " Not found");
        }

        return foundAccount.get();
    }
}

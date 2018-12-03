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
    private IAccountRepository accountRepository;
    private LootRollHelper lootRollHelper;

    @Autowired
    public RouletteLogic(LootRollHelper lootRollHelper, IAccountRepository accountRepository) {
        this.lootRollHelper = lootRollHelper;
        this.accountRepository = accountRepository;
    }

    @Override
    public GameHistory playRoulette(String username, int betAmount, Integer intChoice) {
        //check if the bet is valid
        checkBet(username, betAmount);

        GameHistory history = new GameHistory();
        //make a roulette roll, values between 0 - 36
        int rolledNumber = lootRollHelper.getRandomIntRoll(0, 36);

        //Most valued win first
        if (intChoice.equals(rolledNumber)) {
            //won 35 : 1
            history.setWon(true);
            history.setWonTokens(betAmount * 35);

            addTokens(username, history.getWonTokens());
        }

        history.setRolledNumber(rolledNumber);

        return history;
    }

    @Override
    public GameHistory playRoulette(String username, int betAmount, String stringChoice) {
        //check if the bet is valid
        checkBet(username, betAmount);

        GameHistory history = new GameHistory();

        int rolledNumber = lootRollHelper.getRandomIntRoll(0, 36);
        String colorValue = RouletteConstants.numberWithColors.get(rolledNumber);

        if(stringChoice.equals(colorValue)) {
            //won 2 : 1
            history.setWonTokens(betAmount * 2);
            history.setWon(true);

            addTokens(username, history.getWonTokens());
        }

        history.setRolledNumber(rolledNumber);

        return history;
    }

    //TODO create helper to do this for all game related stuff, duplicate code as of now.
    private void addTokens(String username, int amountToAdd) {
        Account account = accountRepository.findByUsername(username).get();
        account.setTokens(account.getTokens() + amountToAdd);


    }

    private void checkBet(String username, int betAmount) {
        Optional<Account> foundAccount = accountRepository.findByUsername(username);

        if(!foundAccount.isPresent()) {
            throw new IllegalArgumentException("Account not found!");
        }

        Account account = foundAccount.get();
        if(account.getTokens() < betAmount) {
            throw new IllegalArgumentException("Account does not have enough tokens");
        }

        //remove tokens for bet.
        account.setTokens(account.getTokens() - betAmount);
        accountRepository.save(account);
    }
}

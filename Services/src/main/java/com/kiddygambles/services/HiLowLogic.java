package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.entities.GameHistory;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Interfaces.IHiLowLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HiLowLogic implements IHiLowLogic {
    private IAccountRepository accountContext;
    private LootRollHelper lootRollHelper;

    @Autowired
    public HiLowLogic(IAccountRepository accountContext, LootRollHelper lootRollHelper) {
        this.accountContext = accountContext;
        this.lootRollHelper = lootRollHelper;
    }

    @Override
    public GameHistory playHiLow(String username, int betAmount, int currentCardNumber, boolean higher) {
        Account account = getUser(username);

        //Check if user has enough tokens
        if(betAmount > account.getTokens()) {
            throw new IllegalArgumentException("User does not have enough tokens!");
        }

        //Remove tokens from user
        account.setTokens(account.getTokens() - betAmount);

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
            account.setTokens(account.getTokens() + gameHistory.getWonTokens());
        }

        //update account token balance
        accountContext.save(account);

        return gameHistory;
    }

    private Account getUser(String username) {
        Optional<Account> foundAccount = accountContext.findByUsername(username);

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("Account with username + " + username + " Not found");
        }

        return foundAccount.get();
    }
}

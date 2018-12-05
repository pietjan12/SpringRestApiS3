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
    private IAccountRepository accountRepository;
    private LootRollHelper lootRollHelper;

    @Autowired
    public HiLowLogic(IAccountRepository accountRepository, LootRollHelper lootRollHelper) {
        this.accountRepository = accountRepository;
        this.lootRollHelper = lootRollHelper;
    }

    @Override
    public GameHistory playHiLow(String username, int betAmount, int currentCardNumber, boolean higher) {
        //check if bet is valid and take tokens from user
        checkBet(username, betAmount);

        GameHistory gameHistory = new GameHistory();
        //roll a random number
        int rolledNumber = lootRollHelper.getRandomIntRoll(0, 13);
        gameHistory.setRolledNumber(rolledNumber);

        boolean outcome = rolledNumber > currentCardNumber;

        if(higher == outcome) {
            //won 2:1
            gameHistory.setWon(true);
            gameHistory.setWonTokens(betAmount * 2);
            //add tokens to user.
            addTokens(username, gameHistory.getWonTokens());
        }

        return gameHistory;
    }

    private void addTokens(String username, int amountToAdd) {
        Account account = accountRepository.findByUsername(username).get();
        account.setTokens(account.getTokens() + amountToAdd);
        accountRepository.save(account);
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

package com.kiddygambles.services.Helper;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenHelper {
    private IAccountRepository accountRepository;

    @Autowired
    public TokenHelper(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void addTokens(String username, int amountToAdd) {
        Account account = accountRepository.findByUsername(username).get();
        account.setTokens(account.getTokens() + amountToAdd);
        accountRepository.save(account);
    }

    public void hasEnoughTokens(String username, int betAmount) {
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

package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.IAccountLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AccountLogic implements IAccountLogic {

    private IAccountRepository accountContext;

    @Autowired
    public AccountLogic(IAccountRepository accountContext) {
        this.accountContext = accountContext;
    }

    @Override
    public Account GetUser(int userID) {
        Optional<Account> foundAccount = accountContext.findById(userID);

        if(!foundAccount.isPresent()) {
            throw new IllegalArgumentException("User not found in the system");
        }

        return foundAccount.get();
    }
}

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
    private BCryptPasswordEncoder bCrypt;

    @Autowired
    public AccountLogic(IAccountRepository accountContext, BCryptPasswordEncoder bCrypt) {
        this.accountContext = accountContext;
        this.bCrypt = bCrypt;
    }

    @Override
    public Account GetUser(Principal user, int userID) throws IllegalArgumentException, NullPointerException {
        Optional<Account> foundAccount = accountContext.findById(userID);

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("User not found in the system");
        }
        //Optional omzetten naar juiste klasse om te gaan vergelijken of claim geldig is
        Account account = foundAccount.get();

        //In ons geval mag alleen de gebruiker zijn eigen gegevens ophalen
        if(!account.getUsername().equals(user.getName())) {
            throw new IllegalArgumentException("You are not authorized to access this users info!");
        }

        return foundAccount.get();
    }

}

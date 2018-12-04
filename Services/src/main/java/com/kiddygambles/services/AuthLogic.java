package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.principal.JwtUser;
import com.kiddygambles.domain.principal.JwtUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import static java.util.Collections.emptyList;

@Service
public class AuthLogic {
    private IAccountRepository accountRepository;

    @Autowired
    public AuthLogic(IAccountRepository context) {
        this.accountRepository = context;
    }

    //TODO: make this loadUserById (in order for this to work you need to send the id trough with the jwt token)
    public UserDetails loadUserByUsername(int userId, String username) throws UsernameNotFoundException {
        Optional<Account> foundAccount = accountRepository.findById(userId);

        Account account;

        //check if user has logged in to our inventory API before, if not create new account
        if (!foundAccount.isPresent()) {
            account = accountRepository.save(new Account(userId, username));
        } else {
            account = foundAccount.get();
        }

        return new JwtUserPrincipal(account);
    }
}


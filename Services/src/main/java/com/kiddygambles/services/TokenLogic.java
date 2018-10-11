package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.ITokenLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class TokenLogic implements ITokenLogic {
    //Handelen van het omzetten van balans naar tokens waarde op account.
    private IAccountRepository accountContext;

    @Autowired
    public TokenLogic(IAccountRepository accountContext) {
        this.accountContext = accountContext;
    }

    @Override
    public void BuyToken(Principal user, int amount) throws NullPointerException {
        Optional<Account> foundAccount = accountContext.findByUsername(user.getName());

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("Account not found");
        }

        //TODO : Call maken naar bank of er genoeg balans is en geld overmaken naar 'casino bankrekening';
    }
}

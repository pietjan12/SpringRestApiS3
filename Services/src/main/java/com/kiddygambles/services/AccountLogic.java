package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.services.Interfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountLogic implements IAccountLogic {

    private IAccountRepository accountContext;

    @Autowired
    public AccountLogic(IAccountRepository accountContext) {
        this.accountContext = accountContext;
    }

    //inventory ophalen bijvoorbeeld.

}

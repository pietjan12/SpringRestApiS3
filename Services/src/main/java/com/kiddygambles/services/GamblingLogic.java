package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.Item;
import com.kiddygambles.domain.entities.WinningDetails;
import com.kiddygambles.services.Interfaces.IGamblingLogic;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Optional;

public class GamblingLogic implements IGamblingLogic {
    private ICaseRepository caseContext;
    private IAccountRepository accountContext;

    @Autowired
    public GamblingLogic(ICaseRepository caseContext, IAccountRepository accountContext) {
        this.caseContext = caseContext;
        this.accountContext = accountContext;
    }

    @Override
    public WinningDetails openCase(Principal User, int caseID) throws IllegalArgumentException {
        Optional<Case> foundCase = caseContext.findById(caseID);

        if(!foundCase.isPresent()) {
            throw new IllegalArgumentException("caseID invalid");
        }
        //case ophalen en lootroll doen om item te bepalen.T
        Case caseToOpen = foundCase.get();
        double lootRoll = getRandomRoll(0, 100);

        //Get correct item from inventory TODO : GEEN IDEE HOE IK DIT NU WIL DOEN?
        Item item = caseToOpen.getItems().get(1);

        //Save item to winning account
        Optional<Account> foundAccount = accountContext.findByUsername(User.getName());
        if(!foundAccount.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        Account account = foundAccount.get();
        //Add to many to many connection and save. TODO : WRS GEBEURT DIT ALLEMAAL BIJ MARTIJN ZIJN MODULE EN HOEF IK ALLEEN REST CALL TE MAKEN
        account.getItems().add(item);
        item.getAccounts().add(account);
        accountContext.save(account);
        //return winningdetails to user.
        return new WinningDetails(item, lootRoll);
    }


    private double getRandomRoll(int min, int max) {
        double random = min + Math.random() * (max - min);
        return random;
    }
}

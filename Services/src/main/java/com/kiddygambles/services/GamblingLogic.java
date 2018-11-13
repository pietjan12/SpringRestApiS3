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
        //case ophalen en lootroll doen om item te bepalen.
        Case caseToOpen = getCaseByID(caseID);
        double lootRoll = getRandomRoll(0, 100);

        //Get correct item from inventory TODO : GEEN IDEE HOE IK DIT NU WIL DOEN?
        Item item = caseToOpen.getItems().get(1);

        WinningDetails winningDetails = new WinningDetails(item, lootRoll);

        //save winning details to case as history
        caseToOpen.getHistory().add(winningDetails);
        winningDetails.setWonCase(caseToOpen);
        caseContext.save(caseToOpen);

        //Save item to winning account
        //TODO : CALL MAKEN NAAR INVENTORY API OM ITEM TE SAVEN NAAR ACCOUNT!

        //return winningdetails to user.
        return new WinningDetails(item, lootRoll);
    }

    private Case getCaseByID(int caseID) {
        Optional<Case> foundCase = caseContext.findById(caseID);

        if(!foundCase.isPresent()) {
            throw new IllegalArgumentException("caseID invalid");
        }
        //case ophalen en lootroll doen om item te bepalen.T
        return foundCase.get();
    }

    private double getRandomRoll(int min, int max) {
        double random = min + Math.random() * (max - min);
        return random;
    }
}

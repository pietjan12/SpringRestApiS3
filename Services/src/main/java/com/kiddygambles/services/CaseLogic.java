package com.kiddygambles.services;

import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.Item;
import com.kiddygambles.services.Interfaces.ICaseLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaseLogic implements ICaseLogic {

    private ICaseRepository caseContext;

    @Autowired
    public CaseLogic(ICaseRepository caseContext) {
        this.caseContext = caseContext;
    }

    @Override
    public Case getCase(int id) throws NullPointerException{
        Optional<Case> foundCase = caseContext.findById(id);

        if(!foundCase.isPresent()) {
            throw new NullPointerException("Case with id + " + String.valueOf(id) + " Not found");
        }

        return foundCase.get();
    }

    @Override
    public List<Case> getAllCases() throws NullPointerException {
        List<Case> allCases = caseContext.findAll();
        //Controleren of lijst niet leeg is
        if(allCases.size() <= 0) {
            throw new NullPointerException("No cases found");
        }

        return allCases;
    }

    @Override
    public Case createCase(String caseName, String caseDescription, int price) {

        if(Strings.isNullOrEmpty(caseName) || Strings.isNullOrEmpty(caseDescription) || price <= 0) {
            throw new IllegalArgumentException("Case parameters name, description and items cannot be null!");
        }

        Case caseToCreate = new Case(caseName, caseDescription, price);
        return caseContext.save(caseToCreate);
    }

    @Override
    public List<Item> getWinHistory(int caseID) {
        //TODO : COULD REQUIREMENT, DOEN ZODRA DE REST WERKT.

        return null;
    }


}

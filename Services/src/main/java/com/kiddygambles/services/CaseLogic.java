package com.kiddygambles.services;

import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.services.Interfaces.ICaseLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Iterable<Case> getAllCases() throws NullPointerException {
        Iterable<Case> allCases = caseContext.findAll();
        //Controleren of lijst niet leeg is
        if(allCases.spliterator().getExactSizeIfKnown() < 1) {
            throw new NullPointerException("No cases found");
        }

        return allCases;
    }


}

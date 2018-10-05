package com.kiddygambles.services;

import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.services.Interfaces.ICaseLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseLogic implements ICaseLogic {

    private ICaseRepository caseContext;

    @Autowired
    public CaseLogic(ICaseRepository caseContext) {
        this.caseContext = caseContext;
    }


}

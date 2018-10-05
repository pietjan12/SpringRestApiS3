package com.kiddygambles.controllers;

import com.kiddygambles.services.Interfaces.ICaseLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaseController {
    private ICaseLogic caseLogic;

    @Autowired
    public CaseController(ICaseLogic caseLogic) {
        this.caseLogic = caseLogic;
    }


    //public CaseRoll GetWinnings(@Requestparam int caseIdToOpen) {
    //}
}

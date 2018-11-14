package com.kiddygambles.controllers;

import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.services.Interfaces.ICaseLogic;
import com.kiddygambles.wrappers.CaseRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CaseController {
    private ICaseLogic caseLogic;

    @Autowired
    public CaseController(ICaseLogic caseLogic) {
        this.caseLogic = caseLogic;
    }

    @PostMapping(path = "")
    public ResponseEntity<Case> createCase(@RequestBody CaseRequestModel caseRequestModel) {
        return new ResponseEntity<>(caseLogic.createCase(caseRequestModel);, HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Case> getCase(@PathVariable("id") int id) {
        return new ResponseEntity<>(caseLogic.getCase(id), HttpStatus.OK);
    }





    //public CaseRoll GetWinnings(@Requestparam int caseIdToOpen) {
    //}
}

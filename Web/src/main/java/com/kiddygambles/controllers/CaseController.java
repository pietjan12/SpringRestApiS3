package com.kiddygambles.controllers;

import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.CaseHistory;
import com.kiddygambles.services.Interfaces.ICaseLogic;
import com.kiddygambles.wrappers.CaseRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cases")
public class CaseController {
    private ICaseLogic caseLogic;

    @Autowired
    public CaseController(ICaseLogic caseLogic) {
        this.caseLogic = caseLogic;
    }

    @PostMapping(path = "")
    public ResponseEntity<Case> createCase(@RequestBody CaseRequestModel caseRequestModel) {
        return new ResponseEntity<>(caseLogic.createCase(caseRequestModel.getName(), caseRequestModel.getDescription(), caseRequestModel.getPrice()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Case> getCase(@PathVariable("id") int id) {
        return new ResponseEntity<>(caseLogic.getCase(id), HttpStatus.OK);
    }

    @GetMapping(path = "")
    public ResponseEntity<Iterable<Case>> getAllCases() {
        return new ResponseEntity<>(caseLogic.getAllCases(), HttpStatus.OK);
    }

    //Show list of recently won items before websocket server takes over to update it.
    @GetMapping(path = "/{id}/winnings")
    public ResponseEntity<Iterable<CaseHistory>> GetRecentWinnings(@RequestParam("id") int caseID) {
        return new ResponseEntity<>(caseLogic.getWinHistory(caseID), HttpStatus.OK);
    }
}

package com.kiddygambles.controllers;

import com.kiddygambles.domain.DTO.CaseDTO;
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
    public ResponseEntity<CaseDTO> getCase(@PathVariable("id") int id) {
        return new ResponseEntity<>(caseLogic.getCase(id), HttpStatus.OK);
    }

    @GetMapping(path = "")
    public ResponseEntity<Iterable<Case>> getAllCases() {
        return new ResponseEntity<>(caseLogic.getAllCases(), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Case> updateCase(@PathVariable("id") int id, @RequestBody CaseRequestModel model) {
        return new ResponseEntity<>(caseLogic.updateCase(id, model.getName(), model.getDescription(),model.getPrice()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable("id") int id) {
        caseLogic.deleteCase(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.kiddygambles.controllers;

import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {
    private IAccountLogic accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Account> GetUserById(@PathVariable("id") int userID) {
        return new ResponseEntity<>(accountLogic.GetUser(userID), HttpStatus.OK);
    }
}

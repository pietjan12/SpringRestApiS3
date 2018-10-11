package com.kiddygambles.controllers;

import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/account")
public class AccountController {
    private IAccountLogic accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Account> createUser(@RequestBody Account account) throws IllegalArgumentException, NullPointerException {
        Account createdAccount = accountLogic.CreateUser(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteUser(@PathVariable("id") int userToDeleteID, Principal user) throws IllegalArgumentException, NullPointerException {
        accountLogic.DeleteUser(user, userToDeleteID);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Account> GetUserById(@PathVariable("id") int userID, Principal user) throws IllegalArgumentException, NullPointerException {
        Account createdAccount = accountLogic.GetUser(user, userID);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }
}

package com.kiddygambles.controllers;

import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.ITokenLogic;
import com.kiddygambles.wrappers.TokenRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/tokens")
public class TokenController {
    private ITokenLogic tokenLogic;

    @Autowired
    public TokenController(ITokenLogic tokenLogic) {
        this.tokenLogic = tokenLogic;
    }

    @PostMapping(path = "")
    public ResponseEntity<Void> BuyTokens(Principal user, @RequestBody TokenRequestModel requestModel) {
        tokenLogic.buyToken(user.getName(), requestModel.getSenderID(), requestModel.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

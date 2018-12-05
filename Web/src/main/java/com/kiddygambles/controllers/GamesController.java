package com.kiddygambles.controllers;

import com.kiddygambles.domain.entities.GameHistory;
import com.kiddygambles.services.Interfaces.IHiLowLogic;
import com.kiddygambles.services.Interfaces.IRouletteLogic;
import com.kiddygambles.wrappers.HiLowRequestModel;
import com.kiddygambles.wrappers.RouletteRequestModel;
import com.kiddygambles.wrappers.RouletteStringRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/games")
public class GamesController {
    private IRouletteLogic rouletteLogic;
    private IHiLowLogic hiLowLogic;

    @Autowired
    public GamesController(IRouletteLogic rouletteLogic, IHiLowLogic hiLowLogic) {
        this.rouletteLogic = rouletteLogic;
        this.hiLowLogic = hiLowLogic;
    }

    @PostMapping(path = "/roulette")
    public ResponseEntity<GameHistory> playRoulette(@RequestBody RouletteRequestModel reqModel, Principal user) {
        return new ResponseEntity<>(rouletteLogic.playRoulette(user.getName() ,reqModel.getBet(), reqModel.getIntChoice(), reqModel.getStringChoice()), HttpStatus.CREATED);
    }

    @PostMapping(path = "/hilow")
    public ResponseEntity<GameHistory> playHiLow(@RequestBody HiLowRequestModel reqModel, Principal user) {
        return new ResponseEntity<>(hiLowLogic.playHiLow(user.getName(), reqModel.getBet(), reqModel.getCurrentCardNumber(), reqModel.isHigher()), HttpStatus.CREATED);
    }
}

package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.WinningDetails;

import java.security.Principal;

public interface IGamblingLogic {
    WinningDetails openCase(Principal user, int caseID);
}

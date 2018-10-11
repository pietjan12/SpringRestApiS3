package com.kiddygambles.services.Interfaces;

import java.security.Principal;

public interface ITokenLogic {
    void BuyToken(Principal user, int amount);
}

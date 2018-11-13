package com.kiddygambles.services.Interfaces;

import java.security.Principal;

public interface ITokenLogic {
    void buyToken(Principal user, String token, int amount);
}

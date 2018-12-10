package com.kiddygambles.services.Interfaces;

import java.security.Principal;

public interface ITokenLogic {
    /**
     * a function to add tokens to a user
     * @param user the user you want to add the tokens to
     * @param token the jwt token of the user to pass off to bank API
     * @param amount the amount of tokens you wish to buy
     */
    void buyToken(Principal user, String token, int amount);
}

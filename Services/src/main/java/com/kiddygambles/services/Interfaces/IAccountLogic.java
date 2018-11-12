package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.entities.Account;
import javax.security.auth.login.FailedLoginException;
import java.security.Principal;

public interface IAccountLogic {
    /**
     * Gets user details for logged in user
     * @param user the claim of the logged in user based on JWT token.
     * @param userID the userid you want to find
     * @return found user
     * @throws IllegalArgumentException if userID is not found in our system
     */
    Account GetUser(Principal user, int userID);
}

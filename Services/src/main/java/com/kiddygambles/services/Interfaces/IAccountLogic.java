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
    /**
     * Login User based on information given
     * @param account the account object translated from JSON into an Account object.
     * @return nothing if everything goes correct
     * @throws FailedLoginException if account contains wrong information
     */
    Account CreateUser(Account account);
    /**
     * Delete user with information given
     * @param user The claim of the logged in user based on JWT token. (For possible role based authorization).
     * @param userID the userid of the user you want to remove
     * @return nothing if everything goes correct, in most cases the client side should already have all necessary data
     * @throws IllegalArgumentException if account was not found
     */
    void DeleteUser(Principal user, int userID);
}

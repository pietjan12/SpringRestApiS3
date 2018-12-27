package com.kiddygambles.services.Interfaces;


public interface ITokenLogic {
    /**
     * a function to add tokens to a user
     * @param username the user you want to add the tokens to
     * @param senderID the bank account ID you wish to pay from
     * @param amount the amount of tokens you wish to buy
     */
    void buyToken(String username, int senderID, int amount);
}

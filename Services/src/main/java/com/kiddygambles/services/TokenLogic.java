package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Helper.RestCallHelper;
import com.kiddygambles.services.Interfaces.ITokenLogic;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.json.Json;
import java.security.Principal;
import java.util.Optional;

import static com.kiddygambles.services.Constants.KiddyAPIConstants.bankURL;
import static com.kiddygambles.services.Constants.KiddyAPIConstants.gamblingBankNumber;


@Service
//TODO : CLEAN THIS SHIT
public class TokenLogic implements ITokenLogic {
    //Handelen van het omzetten van balans naar tokens waarde op account.
    private IAccountRepository accountContext;
    private RestCallHelper restCallHelper;

    @Autowired
    public TokenLogic(IAccountRepository accountContext, RestCallHelper restCallHelper) {
        this.accountContext = accountContext;
        this.restCallHelper = restCallHelper;
    }

    @Override
    public void buyToken(Principal user, String token, int amount) {
        Account account = getUser(user.getName());

        checkBalance(token, amount);

        transferFunds(token, (amount * 10));

        //account tokens updaten en opslaan in database.
        account.setTokens(account.getTokens() + amount);
        accountContext.save(account);
    }


    /**
     * Gets the user and automatically checks if he is indeed present in the database
     * @param username the username taken from the jwt claim subject
     * @return the found account in the database
     * @throws IllegalArgumentException if the user could not be found.
     */
    private Account getUser(String username) {
        Optional<Account> foundAccount = accountContext.findByUsername(username);

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("Account not found");
        }

        return foundAccount.get();
    }

    /**
     * Gets balance from rest call and checks if the value is enough to cover the wanted amount of gambling tokens
     * @param token the Authorization token taken from the header, this is send with the request to the other API
     * @param amount the amount of tokens you are looking to buy
     * @return nothing, an exception with a correct error message is thrown if anything does not add up
     * @throws RuntimeException if anything server side goes wrong
     * @throws IllegalArgumentException if the user does not have enough balance to cover the cost
     */
    private void checkBalance(String token, int amount) {
        ResponseEntity<Float> balanceResponse = restCallHelper.makeGetRestCall(bankURL + "/bank/balance", token, Float.class);

        float myBalance = balanceResponse.getBody();

        if((myBalance / 10) < amount) {
            throw new IllegalArgumentException("you do not have enough balance to buy this amount of tokens");
        }
    }

    /**
     * transfers funds from users bank account to gambling bank account
     * @param token the Authorization token taken from the header, this is send with the request to the other API
     * @param amount the amount of tokens you are looking to buy
     * @return nothing, an exception with a error message is thrown if anything does not add up
     * @throws RuntimeException  if something went wrong server side
     */
    private void transferFunds(String token, int amount) {
        //create json string to send as data
        String jsonData = Json.createObjectBuilder()
                        .add("receiverID", gamblingBankNumber)
                        .add("price", amount)
                        .build().toString();

        ResponseEntity<String> balanceTransferCall = restCallHelper.makePostRestCall(bankURL + "/bank/transfer", token, jsonData);
    }
}

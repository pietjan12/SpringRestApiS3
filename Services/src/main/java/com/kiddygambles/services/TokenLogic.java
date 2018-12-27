package com.kiddygambles.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class TokenLogic implements ITokenLogic {
    private IAccountRepository accountContext;
    private RestCallHelper restCallHelper;

    @Autowired
    public TokenLogic(IAccountRepository accountContext, RestCallHelper restCallHelper) {
        this.accountContext = accountContext;
        this.restCallHelper = restCallHelper;
    }

    @Override
    public void buyToken(String username, int senderID, int amount) {
        Account account = getUser(username);

        //get the ratio of tokens per balance, this differs based on how many you buy.
        int ratio = determineTokenRatio(amount);

        transferFunds(senderID, amount, ratio);

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
            throw new IllegalArgumentException("Account not found");
        }

        return foundAccount.get();
    }

    private int determineTokenRatio(int tokenAmount) {
        int ratio = 100;

        if(tokenAmount > 500) {
            ratio = 110;
        }

        return ratio;
    }

    /**
     * transfers funds from users bank account to gambling bank account
     * @param amount the amount of tokens you are looking to buy
     * @return nothing, an exception with a error message is thrown if anything does not add up
     * @throws RuntimeException  if something went wrong server side
     */
    private void transferFunds(int senderID, int amount, int tokenRatio) {
        ObjectMapper mapper = new ObjectMapper();

        float cost = (float)amount / (float)tokenRatio;
        //create json string to send as data
        ObjectNode myTransaction = mapper.createObjectNode();
        myTransaction.put("senderID", senderID);
        myTransaction.put("receiverID", gamblingBankNumber);
        myTransaction.put("price", cost);

        ResponseEntity<String> balanceTransferCall = restCallHelper.makePostRestCall(bankURL + "/bank/transfer", myTransaction.toString());

        if(balanceTransferCall.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }
}

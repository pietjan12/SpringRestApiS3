package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.ITokenLogic;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Optional;

@Service
public class TokenLogic implements ITokenLogic {
    //Handelen van het omzetten van balans naar tokens waarde op account.
    private IAccountRepository accountContext;

    @Autowired
    public TokenLogic(IAccountRepository accountContext) {
        this.accountContext = accountContext;
    }

    @Override
    public void BuyToken(Principal user, int amount) throws Exception {
        Optional<Account> foundAccount = accountContext.findByUsername(user.getName());

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("Account not found");
        }

        //TODO : Call maken naar bank of er genoeg balans
        ResponseEntity<Float> balanceResponse = makeGetRestCall("http://safdkasdkask.nep/bank/balance", "HIERKOMTDETOKEN", Float.class);
        Float balance = balanceResponse.getBody();

        if( (balance / 10) < amount) {
            throw new IllegalArgumentException("Not enough balance to buy tokens");
        }

        //TODO : geld transfen naar "GAMBLING BANK NUMMER"
        JSONObject data = new JSONObject();
        data.put("receiverID", "BANKNRVANGAMBLINGHIER");
        data.put("price", (amount * 10));

        ResponseEntity<String> balanceTransferCall = makePostRestCall("http://safds.nep/bank/transfer", "HIERKOMTDETOKEN", data.toString());

        //Controleren of call goed uitgevoerd is.
        if(balanceTransferCall.getStatusCode() != HttpStatus.OK) {
            throw new Exception("transferring funds failed");
        }

        Account account = foundAccount.get();
        //account tokens updaten en opslaan in database.
        account.setTokens(account.getTokens() + amount);
        accountContext.save(account);

    }

    private ResponseEntity<String> makePostRestCall(String url, String token, String data) throws URISyntaxException {
        RequestEntity<String> request = RequestEntity
                .post(new URI(url))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",token)
                .body(data);

        RestTemplate restCall = new RestTemplate();
        return restCall.exchange(request, String.class);
    }

    //TODO TESTEN OF DEZE GENERIC METHOD WERKT.
    private <T> ResponseEntity<T> makeGetRestCall(String url, String token, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<?> httpEntity = new HttpEntity<>("" , headers);

        RestTemplate restcall = new RestTemplate();
        return restcall.exchange(url, HttpMethod.GET, httpEntity, type);
    }
}

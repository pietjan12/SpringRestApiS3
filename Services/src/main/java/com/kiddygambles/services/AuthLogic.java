package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Helper.RestCallHelper;
import net.minidev.json.JSONValue;
import org.assertj.core.util.Strings;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static com.kiddygambles.services.Constants.KiddyAPIConstants.bankURL;
import static com.kiddygambles.services.Constants.KiddyAPIConstants.inventoryURL;
import static java.util.Collections.emptyList;

@Service
public class AuthLogic implements UserDetailsService {
    private RestCallHelper restCallHelper;
    private IAccountRepository accountRepository;

    @Autowired
    public AuthLogic(RestCallHelper restCallHelper, IAccountRepository context) {
        this.restCallHelper = restCallHelper;
        this.accountRepository = context;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetch account from authorization server
        JSONObject account = retrieveAccountData(username);

        //Get optional values from json object
        int accountID = account.optInt("id");
        String accountName = account.optString("username");
        String password = account.optString("password");

        //check if values are indeed present
        if(accountID == 0 || Strings.isNullOrEmpty(accountName) || Strings.isNullOrEmpty(password)) {
            throw new UsernameNotFoundException("Something went wrong, please contact support");
        }

        Optional<Account> foundAccount = accountRepository.findById(accountID);

        //check if user has logged in to our gambling service before, if not create new account for them
        if (!foundAccount.isPresent()) {
            Account newAccount = new Account();
            newAccount.setAccountID(accountID);
            newAccount.setUsername(accountName);
            accountRepository.save(newAccount);
        }
        return new User(accountName, password, emptyList());
    }

    private JSONObject retrieveAccountData(String username) {
        ResponseEntity<String> response = restCallHelper.getCall(bankURL + "/account/" + username, String.class);

        //convert to json
        try {
            JSONObject account = new JSONObject(response.getBody());

            if (account == null) {
                throw new UsernameNotFoundException(username);
            }

            return account;

        } catch(JSONException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}

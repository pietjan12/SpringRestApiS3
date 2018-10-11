package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.IAccountLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AccountLogic implements IAccountLogic {

    private IAccountRepository accountContext;
    private BCryptPasswordEncoder bCrypt;

    @Autowired
    public AccountLogic(IAccountRepository accountContext, BCryptPasswordEncoder bCrypt) {
        this.accountContext = accountContext;
        this.bCrypt = bCrypt;
    }

    @Override
    public Account GetUser(Principal user, int userID) throws IllegalArgumentException, NullPointerException {
        Optional<Account> foundAccount = accountContext.findById(userID);

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("User not found in the system");
        }
        //Optional omzetten naar juiste klasse om te gaan vergelijken of claim geldig is
        Account account = foundAccount.get();

        //In ons geval mag alleen de gebruiker zijn eigen gegevens ophalen
        if(!account.getUsername().equals(user.getName())) {
            throw new IllegalArgumentException("You are not authorized to access this users info!");
        }

        return foundAccount.get();
    }

    @Override
    public Account CreateUser(Account account) throws IllegalArgumentException, NullPointerException {
        //Controleren of belangrijke waardes nul zijn.
        if (Strings.isNullOrEmpty(account.getUsername() )|| Strings.isNullOrEmpty(account.getPassword())) {
            throw new NullPointerException("Values cannot be null");
        }

        Optional<Account> accountInDatabase = this.accountContext.findByUsername(account.getUsername());
        if(accountInDatabase.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        //password encrypten
        String encryptedPassword = bCrypt.encode(account.getPassword());
        account.setPassword(encryptedPassword);

        //opslaan in database en result ophalen.
        Account createdUser = this.accountContext.save(account);

        //TODO : bank account etc creeren. OF DOEN WE TOCH EERST BANK ACCOUNT CREEREN EN VANUIT DAAR DOORPASEN VIA API NAAR DE ANDERE MODULES? > dan krijgen we een shared account.

        return createdUser;
    }

    @Override
    public void DeleteUser(Principal user, int userID) throws NullPointerException, IllegalArgumentException {
        Optional<Account> foundAccount = accountContext.findById(userID);

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("User not found in the system");
        }
        //Optional omzetten naar juiste klasse om te gaan vergelijken of claim geldig is
        Account account = foundAccount.get();

        //In ons geval mag alleen de gebruiker zijn eigen gegevens ophalen, controleren of authorized gebruiker ook gebruiker is die verwijderd moet worden.
        if(!account.getUsername().equals(user.getName())) {
            throw new IllegalArgumentException("You are not authorized to delete this user!");
        }

        //alles is goed, account deleten.
        accountContext.delete(account);
    }

    // TODO : ITEMS OPHALEN EN INVENTORY OPHALEN COMPLEET OPHALEN VANUIT INVENTORY MODULE, HET ENIGE DAT IK HOEF TE DOEN IS DE LOGICA VAN GEWONNEN ITEMS TE REGELEN EN DE JUISTE CALLS NAAR MARTIJN ZN MODULE TE MAKEN.

}

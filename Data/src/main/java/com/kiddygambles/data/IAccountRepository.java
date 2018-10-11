package com.kiddygambles.data;

import com.kiddygambles.domain.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
}

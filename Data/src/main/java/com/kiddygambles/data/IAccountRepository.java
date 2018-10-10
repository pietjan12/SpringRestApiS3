package com.kiddygambles.data;

import com.kiddygambles.domain.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface IAccountRepository extends CrudRepository<Account, Integer> {

}

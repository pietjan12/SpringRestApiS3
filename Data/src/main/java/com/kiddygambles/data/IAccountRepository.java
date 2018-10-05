package com.kiddygambles.data;

import com.kiddygambles.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface IAccountRepository extends CrudRepository<Account, Integer> {

}

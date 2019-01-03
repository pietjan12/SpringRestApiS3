package com.kiddygambles.data;

import com.kiddygambles.domain.entities.CaseHistory;
import org.springframework.data.repository.CrudRepository;

public interface ICaseHistoryRepository extends CrudRepository<CaseHistory, Integer> {

}

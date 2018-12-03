package com.kiddygambles.data;

import com.kiddygambles.domain.entities.CaseHistory;
import org.springframework.data.repository.CrudRepository;

public interface IWinHistoryRepository extends CrudRepository<CaseHistory, Integer> {

}

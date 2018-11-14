package com.kiddygambles.data;

import com.kiddygambles.domain.entities.WinHistory;
import org.springframework.data.repository.CrudRepository;

public interface IWinHistoryRepository extends CrudRepository<WinHistory, Integer> {

}

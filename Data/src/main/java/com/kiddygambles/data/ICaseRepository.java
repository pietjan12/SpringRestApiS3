package com.kiddygambles.data;

import com.kiddygambles.domain.Case;
import org.springframework.data.repository.CrudRepository;

public interface ICaseRepository extends CrudRepository<Case, Integer> {

}

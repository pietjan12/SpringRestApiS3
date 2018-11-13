package com.kiddygambles.data;

import com.kiddygambles.domain.entities.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ICaseRepository extends JpaRepository<Case, Integer> {

}

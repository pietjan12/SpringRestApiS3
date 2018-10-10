package com.kiddygambles.data;

import com.kiddygambles.domain.entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface IItemRepository extends CrudRepository<Item, Integer> {

}

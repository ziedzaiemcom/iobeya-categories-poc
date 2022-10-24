package com.iobeya.categories.poc.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iobeya.categories.poc.entities.Category;

@Repository 
public interface CategoryRepository extends CrudRepository<Category, Long> {
    
    Category findById(long id);
    

}

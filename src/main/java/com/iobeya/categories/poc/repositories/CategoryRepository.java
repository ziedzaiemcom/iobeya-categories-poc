package com.iobeya.categories.poc.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iobeya.categories.poc.entities.Category;

@Repository 
public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findByName(String name);

    List<Category> findByDepth(int depth);
    
    Category findById(long id);
    
    List<Category> findByDepthLessThanEqual(int depth);
    
    List<Category> findAll();

    List<Category> findByParent(long parent);    

}

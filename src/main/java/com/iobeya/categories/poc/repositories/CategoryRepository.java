package com.iobeya.categories.poc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iobeya.categories.poc.entities.Category;
import com.iobeya.categories.poc.entities.CategoryView;

@Repository 
public interface CategoryRepository extends CrudRepository<Category, Long> {
    
    Category findById(long id);

    @Query(value = "SELECT * FROM categories WHERE MATCH (name) AGAINST (?1 IN BOOLEAN MODE) LIMIT 1000", nativeQuery = true)
    public List<Category> searchTop1000ByName(String name);
    
    List<Category> findTop1000ByNameContainingIgnoreCase(@Param("name") String name);    
    

}

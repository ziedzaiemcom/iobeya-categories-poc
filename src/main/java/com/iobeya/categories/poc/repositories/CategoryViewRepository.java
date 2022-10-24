package com.iobeya.categories.poc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iobeya.categories.poc.entities.CategoryView;

@Repository
public interface CategoryViewRepository extends CrudRepository<CategoryView, Long> {

    List<CategoryView> findByName(String name);

    List<CategoryView> findByDepth(int depth);
    
    List<CategoryView> findByDepthLessThanEqual(int depth);

    List<CategoryView> findByParent(long parent);    
    
    List<CategoryView> findTop100ByNameContainingIgnoreCase(@Param("name") String name);    

    @Procedure("getCategoryParents")
    List<CategoryView> findParentsById(long id);    
    
}
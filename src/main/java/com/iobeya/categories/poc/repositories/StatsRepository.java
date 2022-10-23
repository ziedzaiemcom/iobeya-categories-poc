package com.iobeya.categories.poc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iobeya.categories.poc.entities.CategoryStats;


@Repository
public interface StatsRepository extends CrudRepository<CategoryStats, Long> {
    

    @Procedure("getStatsByDepth")
    List<CategoryStats> getStatsByDepth();    
    
    @Procedure("getStats")
    List<CategoryStats> getStats();    
    
}
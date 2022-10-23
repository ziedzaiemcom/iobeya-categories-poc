package com.iobeya.categories.poc.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.Data;

@Entity
@Data
@Immutable
@Table(name = "`categories_view`")
@Subselect("select * from categories_view")
public class CategoryView {
	
    @Id
    private Long id;

    private String name;
    
    private Long parent;
    
    private int depth;
    
    private int child_count;

}

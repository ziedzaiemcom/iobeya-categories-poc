package com.iobeya.categories.poc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "`categories`")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Using mysql auto increment
    private Long id;

    private String name;
    
    @Column(nullable = true)
    private Long parent;
    
    private int depth;
    
    
}

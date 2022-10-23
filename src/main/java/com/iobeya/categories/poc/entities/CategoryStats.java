package com.iobeya.categories.poc.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Entity
@Data
@Immutable
public class CategoryStats {
	@Id
	private int depth;
	private int  total;
}

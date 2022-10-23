package com.iobeya.categories.poc.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryOperation {
	private char type;
	private Category data;
	private Date timestamp;
	
	
}

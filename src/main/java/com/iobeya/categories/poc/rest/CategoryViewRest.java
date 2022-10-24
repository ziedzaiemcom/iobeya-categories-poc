package com.iobeya.categories.poc.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.iobeya.categories.poc.entities.CategoryView;
import com.iobeya.categories.poc.repositories.CategoryViewRepository;

@RestController
public class CategoryViewRest {

	
    @Autowired
    private CategoryViewRepository categoryViewRepository;

    
	@RequestMapping(value="/api/v1/categories/depth/{depth}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<CategoryView> getCategoriesViewsByDepth(@PathVariable int depth) {
		
		List<CategoryView> list = categoryViewRepository.findByDepthLessThanEqual(depth);
		
		return list;
	}
    
	@RequestMapping(value="/api/v1/categories/{parent}/children", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<CategoryView> getViewChildren(@PathVariable Long parent) {
		
		List<CategoryView> list = categoryViewRepository.findByParent(parent);
		
		return list;
	}
	
	@RequestMapping(value = "/api/v1/categories/search/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<CategoryView> getViewChildren(@PathVariable String name) {

		//List<CategoryView> list = categoryViewRepository.searchByName(name);
		List<CategoryView> list = categoryViewRepository.findTop1000ByNameContainingIgnoreCase(name);
		
		

		return list;
	}
	
	@RequestMapping(value = "/api/v1/categories/{id}/parents", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Transactional
	public List<CategoryView> findParentsById(@PathVariable long id) {

		List<CategoryView> list = categoryViewRepository.findParentsById(id);

		return list;
	}
    
}

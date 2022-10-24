package com.iobeya.categories.poc.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.iobeya.categories.poc.Utils;
import com.iobeya.categories.poc.entities.Category;
import com.iobeya.categories.poc.entities.CategoryOperation;
import com.iobeya.categories.poc.repositories.CategoryRepository;

@RestController
public class CategoryRest {

	@Value(value = "${light.mode}")
	private boolean light_mode;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private KafkaTemplate<String, CategoryOperation> kafkaTemplate;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@PostMapping("/api/v1/categories")
	public ResponseEntity<Object> save(@RequestBody Category category) {
		try {
			categoryRepository.save(category);
		    
			if(light_mode)
			    simpMessagingTemplate.convertAndSend("/topic/broadcast", new CategoryOperation('C', category, new Date()));
			else
				kafkaTemplate.send("events", new CategoryOperation('C', category, new Date()));
		    		    
			return new ResponseEntity<>(category, HttpStatus.CREATED);
		} catch (Exception e) {
		
			Throwable th = Utils.findCauseUsingPlainJava(e);
			String error = e.getMessage() + " : " + th.getMessage();

			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PatchMapping("/api/v1/categories/{id}")
	public ResponseEntity<Object> patch(@PathVariable long id, @RequestBody Category category) {
		try {
			Category existing_category = categoryRepository.findById(id);
			existing_category.setName(category.getName());
			categoryRepository.save(existing_category);
		    		    
			if(light_mode)
			    simpMessagingTemplate.convertAndSend("/topic/broadcast", new CategoryOperation('M', existing_category, new Date()));
			else
				kafkaTemplate.send("events", new CategoryOperation('M', existing_category, new Date()));
		    
			return new ResponseEntity<>(category, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			
			Throwable th = Utils.findCauseUsingPlainJava(e);
			String error = e.getMessage() + " : " + th.getMessage();

			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/api/v1/categories/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id) {
		try {
			Category category = categoryRepository.findById(id);
			categoryRepository.delete(category);
					    		    
			if(light_mode)
			    simpMessagingTemplate.convertAndSend("/topic/broadcast", new CategoryOperation('D', category, new Date()));
			else
				kafkaTemplate.send("events", new CategoryOperation('D', category, new Date()));
		    
			return new ResponseEntity<>(category, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			
			Throwable th = Utils.findCauseUsingPlainJava(e);
			String error = e.getMessage() + " : " + th.getMessage();

			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}

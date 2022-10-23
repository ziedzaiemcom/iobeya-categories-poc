package com.iobeya.categories.poc.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.iobeya.categories.poc.entities.CategoryStats;
import com.iobeya.categories.poc.repositories.StatsRepository;

@RestController
public class StatsRest {

    @Autowired
    private StatsRepository statswRepository;
	
	@RequestMapping(value = "/api/v1/stats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Transactional
	public List<CategoryStats> getStats() {

		List<CategoryStats> list = statswRepository.getStats();

		return list;
	}
	
	@RequestMapping(value = "/api/v1/stats/depth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Transactional
	public List<CategoryStats> getStatsByDepth() {

		List<CategoryStats> list = statswRepository.getStatsByDepth();

		return list;
	}
}

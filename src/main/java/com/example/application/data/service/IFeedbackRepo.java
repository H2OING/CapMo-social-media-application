package com.example.application.data.service;

import org.springframework.data.repository.CrudRepository;

import com.example.application.data.entity.Feedback;

public interface IFeedbackRepo extends CrudRepository<Feedback, Integer>{
	
	
}

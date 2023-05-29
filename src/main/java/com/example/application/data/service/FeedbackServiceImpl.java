package com.example.application.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Feedback;
import com.example.application.data.entity.User;

@Service
public class FeedbackServiceImpl implements IFeedbackService{

	@Autowired
	private IFeedbackRepo feedbackRepo;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserRepo userRepo;
	
	public void saveFeedback(Feedback feedback) {
		feedbackRepo.save(feedback);
		
	}
	
}

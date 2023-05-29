package com.example.application.data.service;

import java.util.List;

import com.example.application.data.entity.Notification;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface INotificationService{

	public void addFollowNotification(User user, Profile profile);
	
	public List<Notification> getAllNotifications(Profile profile);
	
	public void addLikeNotification(User user,Profile profile);
	
	public void addCommentNotification(User user, Profile profile);
}

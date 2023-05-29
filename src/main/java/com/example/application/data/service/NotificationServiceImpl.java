package com.example.application.data.service;

import java.util.List;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Notification;
import com.example.application.data.entity.NotificationType;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

@Service
public class NotificationServiceImpl implements INotificationService{

	@Autowired
	private INotificationRepo notiRepo;
	
	public void addFollowNotification(User user, Profile profile) {
		Notification notif = new Notification();
		notif.setDescription("User " + user.getUsername().toString() + " just followed you.");
		notif.setProfile(profile);
		notif.setNotifType(NotificationType.FOLLOW);
		notiRepo.save(notif);
	}
	
	public List<Notification> getAllNotifications(Profile profile){
		List<Notification> notifications = notiRepo.findByProfile(profile);		
		return notifications;	
	}
	
	public void addLikeNotification(User user, Profile profile) {
		Notification notif = new Notification();
		notif.setDescription("User " + user.getUsername().toString() + " just liked your post");
		notif.setProfile(profile);
		notif.setNotifType(NotificationType.LIKE);
		notiRepo.save(notif); 
	}
	
	public void addCommentNotification(User user, Profile profile) {
		Notification notif = new Notification();
		notif.setDescription("User " + user.getUsername().toString() + " just commented on your post");
		notif.setProfile(profile);
		notif.setNotifType(NotificationType.COMMENT);
		notiRepo.save(notif); 
	}
}

package com.example.application.data.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.example.application.data.entity.Notification;
import com.example.application.data.entity.Profile;

public interface INotificationRepo extends CrudRepository<Notification, Integer>{

	List<Notification> findByProfile(Profile profile);
}

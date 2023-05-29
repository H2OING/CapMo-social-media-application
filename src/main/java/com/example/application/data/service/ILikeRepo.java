package com.example.application.data.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Notification;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface ILikeRepo extends CrudRepository<Likes, Integer>{
	
	boolean existsByUserPostAndUser(UserPost userPost, User user);
	
	Likes findByUserPostAndUser(UserPost userPost, User user);
	
	List<Likes> findByIsLikedAndUserPost(boolean isLiked, UserPost userPost);
	
	List<Likes> findByUserPost(UserPost userPost);

	void deleteByUserPost(UserPost userPost);
}

package com.example.application.data.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.application.data.entity.Comments;
import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Notification;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface ICommentRepo extends CrudRepository<Comments, Integer>{
	
	boolean existsByUserPostAndUser(UserPost userPost, User user);
	
	Comments findByUserPostAndUser(UserPost userPost, User user);

	List<Comments> findByUserPost(UserPost userPost);

	void deleteByUserPost(UserPost userPost);
}

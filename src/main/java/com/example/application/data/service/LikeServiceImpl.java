package com.example.application.data.service;

import java.util.List;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Notification;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

@Service
public class LikeServiceImpl implements ILikeService{

	@Autowired
	private ILikeRepo likeRepo;
	
	@Autowired
	private IUserPostRepo userPostRepo;
	
	public void addLike(UserPost userPost, User currentUser) {
		if(likeRepo.existsByUserPostAndUser(userPost, currentUser)) {
			Likes existingLike = likeRepo.findByUserPostAndUser(userPost, currentUser);
			if(existingLike.isLiked()) {
				existingLike.setLiked(false);
			}else {
				existingLike.setLiked(true);	
			}
			likeRepo.save(existingLike);
		}else {
			Likes like = new Likes(true, userPost, currentUser);
			likeRepo.save(like);
			userPost.getLike().add(like);
			
		}
		userPostRepo.save(userPost);			
	}
	
	public int getLikedCount(UserPost userPost) {
		return likeRepo.findByIsLikedAndUserPost(true, userPost).size();
	}
	
	public void deleteLikes(UserPost userPost) {
		List<Likes> likes = likeRepo.findByUserPost(userPost);
		userPost.getLike().removeAll(likes);
		likeRepo.deleteAll(likes);
	}
}

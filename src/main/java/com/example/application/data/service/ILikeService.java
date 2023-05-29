package com.example.application.data.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Notification;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface ILikeService{
	
	public void addLike(UserPost userPost, User currentUser);
	public int getLikedCount(UserPost userPost);
	public void deleteLikes(UserPost userPost);
}

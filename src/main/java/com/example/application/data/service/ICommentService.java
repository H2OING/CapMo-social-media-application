package com.example.application.data.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Notification;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface ICommentService{
	
	public void addComment(UserPost userPost, String message, User currentUser);
	public void deleteComments(UserPost userPost);
	public void openCommentDialog(UserPost userPost, User currentUser, Profile profile);
}

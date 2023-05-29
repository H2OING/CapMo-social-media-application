package com.example.application.data.service;

import java.util.List;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface IUserService{

	public User getCurrentUser();
	
	public User update(User user);
	
	public User getByUsername(String username);
	
	public boolean isUserFollowingProfile(User userWhoFollows, Profile profile);

	public List<UserPost> getFollowingUserPosts(User user);
}

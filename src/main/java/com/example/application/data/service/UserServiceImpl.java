package com.example.application.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;
import com.vaadin.flow.server.VaadinSession;


@Service
public class UserServiceImpl implements IUserService{

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	IUserRepo userRepo;
	
	public User getCurrentUser() {
	    User activeUser = VaadinSession.getCurrent().getAttribute(User.class);
	    return activeUser;
	  }
	
	public User update(User entity) {
        return userRepo.save(entity);
    }
	
	public User getByUsername(String username) {
		return userRepo.getByUsername(username);
	}
	
	public boolean isUserFollowingProfile(User userWhoFollows, Profile profile) {
		return profile.getFollowers().contains(userWhoFollows);		
	};
	
	public List<UserPost> getFollowingUserPosts(User user){
		
		List<UserPost> allFollowingPosts = new ArrayList<>();
		Set<Profile> profiles = user.getFollowing();
		
		for (Profile profile : profiles) {
		    List<UserPost> posts = (List<UserPost>) profile.getPost();
		    allFollowingPosts.addAll(posts);
		}
		return allFollowingPosts;		
	}
}

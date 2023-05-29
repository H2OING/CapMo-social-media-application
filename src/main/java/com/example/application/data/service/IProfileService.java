package com.example.application.data.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface IProfileService{

	Profile getCurrentProfile();
	
    public Optional<Profile> get(Long id);

    public Profile update(Profile entity);

    public void delete(Long id);

    public Page<Profile> list(Pageable pageable);

    public Page<Profile> list(Pageable pageable, Specification<Profile> filter);

    public int count();

	public void saveUserPost(UserPost newPost);
	
	public void addFollower(Profile profile, User user);
	
	public boolean isUserFollowingProfile(User userWhoFollows, Profile profile);

	void updateProfilePicture(Profile profile, byte[] newPicture);
	
	Profile getProfileByUsername(String username);
	
	public void removeFollowing(User user, Profile profile);

}

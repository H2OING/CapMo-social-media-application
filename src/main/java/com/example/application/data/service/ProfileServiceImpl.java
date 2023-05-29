package com.example.application.data.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.SamplePerson;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProfileServiceImpl implements IProfileService{

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserPostRepo postRepo;
	
	@Autowired
    private IProfileRepo profileRepo;
	
	@Autowired
	private IUserRepo userRepo;
    
	public Profile getCurrentProfile() {
	    return userService.getCurrentUser().getProfile();
	  }
	 
    public ProfileServiceImpl(IProfileRepo profileRepo) {
        this.profileRepo = profileRepo;
    }

    public Optional<Profile> get(Long id) {
        return profileRepo.findById(id);
    }

    public Profile update(Profile entity) {
        return profileRepo.save(entity);
    }

    public void delete(Long id) {
    	profileRepo.deleteById(id);
    }

    public Page<Profile> list(Pageable pageable) {
        return profileRepo.findAll(pageable);
    }

    public Page<Profile> list(Pageable pageable, Specification<Profile> filter) {
        return profileRepo.findAll(filter, pageable);
    }

    public int count() {
        return (int) profileRepo.count();
    }

	@Override
	public void saveUserPost(UserPost newPost) {
		postRepo.save(newPost);
	}
	
	@Override
	public void addFollower(Profile profile, User user) {
        profile.getFollowers().add(user);        
        user.getFollowing().add(profile);
        userRepo.save(user);
        profileRepo.save(profile);
    }

	@Transactional
    public void removeFollowing(User user, Profile profile) {
		user.getFollowing().remove(profile);
	    profile.getFollowers().remove(user);

	    userRepo.save(user);
        profileRepo.save(profile);
    }
	
	public boolean isUserFollowingProfile(User userWhoFollows, Profile profile) {
		return userWhoFollows.isFollowing(profile);		
	};
	
	@Override
    public void updateProfilePicture(Profile profile, byte[] newPicture) {
        profile.setProfilePicture(newPicture);
        profileRepo.save(profile);
    }
	
	public Profile getProfileByUsername(String username) {
		return userRepo.getByUsername(username).getProfile();
	}

	

}

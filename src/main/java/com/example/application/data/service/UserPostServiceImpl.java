package com.example.application.data.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Feedback;
import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserPostServiceImpl implements IUserPostService{

	@Autowired
	public IUserPostRepo userPostRepo;
	
	@Autowired
	public ILikeRepo likeRepo;
	
	@Autowired
	public ICommentRepo commentRepo;
	
	@Autowired
	public ILikeService likeService;
	
	public void deletePost(UserPost userPost) {
	    if (userPost != null) {
	      likeRepo.deleteByUserPost(userPost);
	      commentRepo.deleteByUserPost(userPost);
	      userPostRepo.delete(userPost);
	    }
	  }
	
	public List<UserPost> getAllUserPosts() {
		return (List<UserPost>) userPostRepo.findAll();
	}
	
	public Page<UserPost> list(Pageable pageable) {
        return userPostRepo.findAll(pageable);
    }

    public Page<UserPost> list(Pageable pageable, Specification<UserPost> filter) {
        return userPostRepo.findAll(filter, pageable);
    }
    
    public Optional<UserPost> get(Long id) {
        return userPostRepo.findById(id);
    }
    
    public UserPost update(UserPost entity) {
        return userPostRepo.save(entity);
    }
}

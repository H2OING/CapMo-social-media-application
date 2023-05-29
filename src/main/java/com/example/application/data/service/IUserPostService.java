package com.example.application.data.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.application.data.entity.Feedback;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

public interface IUserPostService{
	
	public void deletePost(UserPost post);
	
	public List<UserPost> getAllUserPosts();
	
	public Page<UserPost> list(Pageable pageable);

    public Page<UserPost> list(Pageable pageable, Specification<UserPost> filter);
    
    public Optional<UserPost> get(Long id);
    
    public UserPost update(UserPost entity);
}

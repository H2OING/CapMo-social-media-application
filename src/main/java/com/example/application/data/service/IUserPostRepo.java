package com.example.application.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.UserPost;

public interface IUserPostRepo extends JpaRepository<UserPost, Long>, JpaSpecificationExecutor<UserPost>{

	
}

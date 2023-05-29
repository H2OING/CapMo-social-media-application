package com.example.application.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;

public interface IProfileRepo extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile>{
	
}

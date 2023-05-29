package com.example.application.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.example.application.data.entity.Role;

public interface IRoleRepo extends CrudRepository<Role, Integer>{

	Role findByTitle(String string);

	
}

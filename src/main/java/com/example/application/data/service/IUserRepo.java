package com.example.application.data.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;

public interface IUserRepo extends JpaRepository<User, Long>{

	User getByUsername(String username);

}

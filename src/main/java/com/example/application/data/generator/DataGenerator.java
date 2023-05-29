package com.example.application.data.generator;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.IProfileRepo;
import com.example.application.data.service.IRoleRepo;
import com.example.application.data.service.IUserRepo;
import com.example.application.data.service.IUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(IUserRepo userRepository, IRoleRepo roleRepo, IProfileRepo profileRepo, AuthService authServ) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            
            logger.info("Generating demo data");

           
            
            Role userRole = new Role("USER", "User role");
            Role adminRole = new Role("ADMIN", "Admin role");
            roleRepo.save(userRole);
            roleRepo.save(adminRole);
            
            
            Profile userProfile = new Profile(false, null, "Markuss", "Kārklins", "Sveiki, šis ir mans profils ;)");
            profileRepo.save(userProfile);
            
            User user = new User("user", "u", userRole, true);
            User admin = new User("admin", "a", adminRole, true);
            user.setProfile(userProfile);
            userProfile.setUser(user);
            userRepository.save(user);
            profileRepo.save(userProfile);
            
            userRepository.save(admin);
            
//            authServ.register("mark1", "m");
//            authServ.register("mark2", "m");
//            authServ.register("mark3", "m");
            
            userRole.addUserAccount(user);
            adminRole.addUserAccount(admin);
            roleRepo.save(userRole);
            roleRepo.save(adminRole);

            logger.info("Generated demo data");
        };
    }

}
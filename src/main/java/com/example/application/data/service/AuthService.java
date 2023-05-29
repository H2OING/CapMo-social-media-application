package com.example.application.data.service;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.util.Utils;
import com.example.application.views.HomeView;
import com.example.application.views.LogoutView;
import com.example.application.views.MainLayout;
import com.example.application.views.MessageView;
import com.example.application.views.NotificationView;
import com.example.application.views.ProfileView;
import com.example.application.views.SearchView;
import com.example.application.views.SingleProfileView;
import com.example.application.views.admin.AdminView;
import com.example.application.views.admin.UserPostAdminView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    public record AuthorizedRoute(String route, String name, Class<? extends Component> view) {

    }

    public class AuthException extends Exception {

    }

    private final IUserRepo userRepository;
    private final IRoleRepo roleRepository;
    private final IProfileRepo profileRepo;
    private final Utils utils;

    public AuthService(IUserRepo userRepository, IRoleRepo roleRepository,IProfileRepo profileRepo, Utils utils /*, IAlbumRepo albumRepo , MailSender mailSender*/) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepo = profileRepo;
        this.utils = utils;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);
        if (user != null && user.checkPassword(password) && user.isActive()) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            Hibernate.initialize(user.getFollowing());
            createRoutes(user.getRole().getTitle());
        } else {
            throw new AuthException();
        }
    }

    private void createRoutes(String string) {
        getAuthorizedRoutes(string).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(
                                route.route, route.view, MainLayout.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(String string) {
        var routes = new ArrayList<AuthorizedRoute>();

        if (string.equals("USER")) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("profile", "Profile", ProfileView.class));
            routes.add(new AuthorizedRoute("notificiation", "Notificiation", NotificationView.class));
            routes.add(new AuthorizedRoute("message", "Message", MessageView.class));
            routes.add(new AuthorizedRoute("search", "Search", SearchView.class));           
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));
            new AuthorizedRoute("profiles", "Profiles", SingleProfileView.class);
        } else if (string.equals("ADMIN")) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("admin/*", "Admin-Profile", AdminView.class));
            routes.add(new AuthorizedRoute("posts/*", "Admin-Post", UserPostAdminView.class));
//            routes.add(new AuthorizedRoute("admin/%s/edit", "Admin", AdminView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));
        } else if (string.equals("MODERATOR")) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("admin/**", "Admin", AdminView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));
        }
        routes.removeIf(route -> route.name().equals("Login"));
        
        return routes;
    }

    public void register(String username, String password) {
    	Role role = roleRepository.findByTitle("USER");
    	if(userRepository.getByUsername(username) == null) {
    		User user = new User(username, password, role, true);	
    		Profile userProfile = new Profile(true, user, "Name", "Surname", "Your bio goes here.");
    		
    		byte[] defaultImageData = utils.setDefaultImage();   				

    		userProfile.setProfilePicture(defaultImageData);    		
    		user.setProfile(userProfile);
    		userProfile.setUser(user);
    		
    		profileRepo.save(userProfile);
    		userRepository.save(user);
    		
    		Notification.show("Registration suceeded.");
    		UI.getCurrent().navigate("login");
    	}
    	else {
    		Notification.show("Username already exists.");
    	}

    }

}

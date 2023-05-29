package com.example.application.views;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.service.INotificationService;
import com.example.application.data.service.IProfileRepo;
import com.example.application.data.service.IProfileService;
import com.example.application.data.service.IUserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;

@PageTitle("Search")
@CssImport("./styles/views/search-view.css")
public class SearchView extends VerticalLayout {

	private final TextField usernameField;
    private final Button searchButton;
    private VerticalLayout resultLayout = new VerticalLayout();
    private Label resultLabel = new Label();;

    @Autowired
    private IProfileService profileService; 
    @Autowired
    private IUserService userService;
    
    @Autowired
    private INotificationService notifService;

    
    public SearchView(IProfileRepo profileRepo, IProfileService profileService, IUserService userService, INotificationService notifService) {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);
        
        add(new H1("Search for a User"));

        usernameField = new TextField("Username");
        searchButton = new Button();
        searchButton.setIcon(new Icon(VaadinIcon.SEARCH));
        searchButton.setText("Search");
        searchButton.addClickListener(event -> {
            String username = usernameField.getValue();
            List<Profile> profileList = profileRepo.findAll();
            List<Profile> results = new ArrayList<>();

            for (Profile profile : profileList) {
                if (profile.getUser().getUsername().contains(username)) {
                    results.add(profile);
                }
            }

            resultLabel.setText("Showing " + results.size() + " search results");
            resultLayout.removeAll();

            for (Profile profile : results) {
                resultLayout.add(createCard(profile));
            }
        });
        searchButton.addClickShortcut(Key.ENTER);
        HorizontalLayout searchLayout = new HorizontalLayout(usernameField, searchButton);
        searchLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        add(searchLayout);

        resultLabel = new Label();
        resultLayout = new VerticalLayout();
        add(resultLabel, resultLayout);
    }
    
    private Card createCard(Profile profile) {
        Image profilePicture = new Image();
        profilePicture.setSrc(new StreamResource("profile-picture", () -> 
            new ByteArrayInputStream(profile.getProfilePicture())));
        profilePicture.setWidth("200px");
        profilePicture.setHeight("200px");
        profilePicture.getStyle().set("margin", "0 auto");
        
        Label nameLabel = new Label(profile.getName() + " " + profile.getSurname());
        nameLabel.getStyle().set("font-size", "18px");

        Label usernameLabel = new Label("@" + profile.getUser().getUsername());
        usernameLabel.getStyle().set("font-size", "14px");

        VerticalLayout nameAndUsernameLayout = new VerticalLayout(nameLabel, usernameLabel);
        nameAndUsernameLayout.setSpacing(false);
        nameAndUsernameLayout.setWidth("100%");
        
        User userWhoFollows = userService.getCurrentUser();
        boolean isFollowing = profileService.isUserFollowingProfile(userWhoFollows, profile);
        Button followButton = new Button(isFollowing ? "Following" : "Follow");
        followButton.addClickListener(event -> {
        	profileService.addFollower(profile, userWhoFollows);
        	
        	notifService.addFollowNotification(userWhoFollows, profile);
            followButton.setText("Following");
            followButton.setEnabled(false);
        });
        
        Button messageButton = new Button("View");
        messageButton.addClickListener(event -> {
            String username = profile.getUser().getUsername();
            UI.getCurrent().getPage().setLocation("/profiles/" + username);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(followButton,messageButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidth("100%");

        VerticalLayout cardBodyLayout = new VerticalLayout(profilePicture, nameAndUsernameLayout, buttonLayout);
        cardBodyLayout.setSpacing(false);
        cardBodyLayout.setWidth("100%");

        Card card = new Card(cardBodyLayout);
        card.setWidth("300px");
        card.getStyle().set("margin", "10px");
        return card;
    }
}

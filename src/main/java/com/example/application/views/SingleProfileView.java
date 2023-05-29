package com.example.application.views;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;
import com.example.application.data.service.ICommentService;
import com.example.application.data.service.INotificationService;
import com.example.application.data.service.IProfileService;
import com.example.application.data.service.IUserService;
import com.example.application.data.service.LikeServiceImpl;
import com.example.application.data.service.ProfileServiceImpl;
import com.example.application.util.Utils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import jakarta.websocket.server.PathParam;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

@Route(value = "profiles", layout = MainLayout.class)
public class SingleProfileView extends Div implements HasUrlParameter<String> {

	@Autowired
	private IProfileService profileService;
	@Autowired
	private IUserService userService;
	@Autowired
	private LikeServiceImpl likeService;
	@Autowired
	private Utils utils;
	@Autowired
	private INotificationService notifService;
	@Autowired
	private ICommentService commentService;
	
	private Label nameLabel;
	private Label surnameLabel;
	private Label bioLabel;
	private Button following;
	private Button followers;
	private Image profileImage;
	private VerticalLayout postLayout;
  
    @Override
    public void setParameter(BeforeEvent events, @OptionalParameter String parameter) {
    	
    	User currentUser = userService.getCurrentUser();
    	Profile currentProfile = profileService.getProfileByUsername(parameter);
        if (parameter == null) {
            setText("Welcome anonymous.");
        } else {
          
        }
               
        nameLabel = new Label(currentProfile.getName() + " ");
        surnameLabel = new Label(currentProfile.getSurname());
        bioLabel = new Label(currentProfile.getBio());
        
        following = new Button("Following (" + currentProfile.getUser().getFollowing().size() + ")");
        following.addClickListener(clickEvent -> {
        	
            // List ar lietotājiem, kuriem pašreizējais lietotājs seko
            Collection<Profile> followingUsers = currentProfile.getUser().getFollowing();
            
            Dialog followingDialog = new Dialog();
            followingDialog.setCloseOnEsc(true);
            followingDialog.setCloseOnOutsideClick(true);

            // Izveido following skatu kas uzglabā lietotājus
            VerticalLayout followingLayout = new VerticalLayout();
            followingLayout.setPadding(false);
            followingLayout.setSpacing(false);

            for (Profile followingUser : followingUsers) {
                //followingLayout.add(new Label(followingUser.getUser().getUsername()));
            	HorizontalLayout followingLayouts = new HorizontalLayout();
            	followingLayouts.setAlignItems(Alignment.CENTER);
            	followingLayouts.setSpacing(true);

                Image profilePic = utils.byteToImage(followingUser.getProfilePicture());
                profilePic.setWidth("50px");
                profilePic.setHeight("50px");
                profilePic.getStyle().set("border-radius", "50%");
                
                followingLayouts.add(profilePic, new Label(followingUser.getUser().getUsername()));

                followingLayouts.addClickListener(innerClickEvent -> {
                    UI.getCurrent().navigate("/profiles/" + followingUser.getUser().getUsername());
//                    followingDialog.close();
                    innerClickEvent.getSource().getUI().ifPresent(ui -> {

                        ui.getPage().reload();
                    	followingDialog.close();
                        
                    });
                });                
                followingLayout.add(followingLayouts);
            }
            followingDialog.add(followingLayout);
            followingDialog.open();
        });
        
        //followers = new Label("Followers " + String.valueOf(currentProfile.getFollowers().size()));
        followers = new Button("Followers (" + currentProfile.getFollowers().size() + ")");
        followers.addClickListener(clickEvent -> {
            // List ar lietotājiem, kuri seko pašreizējam lietotājam
            Collection<User> followersUsers = currentProfile.getFollowers();
            
            Dialog followersDialog = new Dialog();
            followersDialog.setCloseOnEsc(true);
            followersDialog.setCloseOnOutsideClick(true);

            VerticalLayout followersLayout = new VerticalLayout();
            followersLayout.setPadding(false);
            followersLayout.setSpacing(false);

            for (User followersUser : followersUsers) {
            	//followersLayout.add(new Label(followersUser.getUsername()));
            	HorizontalLayout followerLayout = new HorizontalLayout();
                followerLayout.setAlignItems(Alignment.CENTER);
                followerLayout.setSpacing(true);

                Image profilePic = utils.byteToImage(followersUser.getProfile().getProfilePicture());
                profilePic.setWidth("50px");
                profilePic.setHeight("50px");
                profilePic.getStyle().set("border-radius", "50%");
                
                followerLayout.add(profilePic, new Label(followersUser.getUsername()));

                followerLayout.addClickListener(innerClickEvent -> {
                    UI.getCurrent().navigate("/profiles/" + followersUser.getUsername());
//                    followersDialog.close();
                    innerClickEvent.getSource().getUI().ifPresent(ui -> {
                    	followersDialog.close();
                        ui.getPage().reload();
                    });
                });
                followersLayout.add(followerLayout);
            }
            followersDialog.add(followersLayout);
            followersDialog.open();
        });
        
        if(currentProfile.getProfilePicture() == null) {
        	currentProfile.setProfilePicture(utils.setDefaultImage());
        }
        profileImage = utils.byteToImage(currentProfile.getProfilePicture());
        profileImage.setWidth("200px");
        profileImage.setHeight("200px");
        profileImage.getStyle().set("border-radius", "50%");
        
        postLayout = new VerticalLayout();
        postLayout.setWidth("100%");

        Collection<UserPost> userPosts = currentProfile.getPost();

        Div postsDiv = new Div();
        
        if(userPosts.isEmpty()) {
        	Label noPostsLabel = new Label("There are no posts.");
            postLayout.add(noPostsLabel);
        }else {
        	for (UserPost post : userPosts) {
	            Image postImage = utils.byteToImage(post.getImage());
	            postImage.setWidth("200px");
	            postImage.setHeight("200px");

	            Dialog dialog = new Dialog();
	            dialog.setModal(true);
	            dialog.setDraggable(false);
	            dialog.setResizable(false);
	            
	            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM HH:mm");
	            Label creationDateLabel = new Label(post.getCreatedDate().format(dateFormatter).toString());
	            creationDateLabel.getStyle().set("margin-left", "auto");
	            
	            Image enlargedImage = utils.byteToImage(post.getImage());
	            enlargedImage.setWidth("400px");
//	            enlargedImage.setHeight("400px");

	            Label postDescriptionPopup = new Label(post.getDescription());
	            Button likeButtonPopup = likeButton(post, currentUser, currentProfile);
	            
	            Button commentButton = new Button("Comment");
        	    Icon messageIcon = VaadinIcon.CHAT.create(); 
        	    commentButton.setIcon(messageIcon);
        	    commentButton.addClickListener(e -> commentService.openCommentDialog(post, currentUser, currentProfile));
	            
        	    postImage.addClickListener(event -> dialog.open());
	            
	            Label postDescription = new Label(post.getDescription());
                
                Button likeButton = likeButton(post, currentUser, currentProfile);
	
                HorizontalLayout commentAndLike = new HorizontalLayout(likeButton, commentButton);
        	    VerticalLayout postColumn = new VerticalLayout( postDescription,commentAndLike);
        	    HorizontalLayout postRow = new HorizontalLayout(postImage, postColumn);
	            postRow.addClassName("every-post");
	            postRow.setWidth("100%");
	            postsDiv.add(postRow);
	            
	            VerticalLayout dialogContent = new VerticalLayout();
	            dialogContent.add(creationDateLabel, enlargedImage, postDescriptionPopup, likeButtonPopup);
	            dialog.add(dialogContent);
	        }
        }
        
        Button reportButton = new Button("Report");
        reportButton.setIcon(new Icon(VaadinIcon.WARNING));
        
        Div nameSurnameDiv = new Div(nameLabel, surnameLabel, followers, following, reportButton);
        nameSurnameDiv.addClassName("name-surname-div");

        Div bioDiv = new Div(bioLabel);
        bioDiv.addClassName("bio-div");

        Div profileImageContainer = new Div(profileImage);
        profileImageContainer.addClassName("profile-image-container");
        
        Div noPostsDiv = new Div(postLayout);
        noPostsDiv.setWidth( "100%");
        noPostsDiv.getStyle().set("text-align", "center");
        
        Div profileContainer = new Div(profileImageContainer, nameSurnameDiv, bioDiv, postLayout);
        profileContainer.addClassName("profile-view");
        
        Div postsContainer = new Div(postsDiv);
        postsContainer.addClassName("post-view");

        Div mainContainer = new Div(profileContainer, postsContainer);
        add(mainContainer);
    }
    
    private Button likeButton(UserPost post, User currentUser, Profile currentProfile) {
    	Button likeButton = new Button(String.valueOf(likeService.getLikedCount(post)));
	    Icon heartIcon = VaadinIcon.HEART.create(); 
	    likeButton.setIcon(heartIcon);

	    likeButton.addClickListener(likeEvent -> {
	        likeService.addLike(post, currentUser);
	        int updatedLikeCount = likeService.getLikedCount(post);
	        likeButton.setText(String.valueOf(updatedLikeCount));
	        notifService.addLikeNotification(currentUser,currentProfile);
	    });
	    return likeButton;
    }
}
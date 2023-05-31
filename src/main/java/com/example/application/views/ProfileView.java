package com.example.application.views;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;
import com.example.application.data.service.ICommentService;
import com.example.application.data.service.ILikeService;
import com.example.application.data.service.INotificationService;
import com.example.application.data.service.IProfileService;
import com.example.application.data.service.IUserPostService;
import com.example.application.data.service.IUserService;
import com.example.application.data.service.LikeServiceImpl;
import com.example.application.util.Utils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


@PageTitle("Profile")
@CssImport("./styles/views/profile-view.css")
public class ProfileView extends Div {

    private final Label nameLabel;
    private final Label surnameLabel;
    private final Label bioLabel;
    private final Button following;
    private final Button followers;
    private final Image profileImage;
    private final VerticalLayout postLayout;
    
    private final Button editProfileButton;
    private final Dialog editProfilePopup;
    private final TextField nameField;
    private final TextField surnameField;
    private final TextArea bioField;
    private final Image profileImagePreview;
    private final Upload profileImageUpload;

    public ProfileView(IProfileService profileService,
    		IUserService userService, Utils utils, 
    		LikeServiceImpl likeService, INotificationService notifService,
    		IUserPostService userPostService, ICommentService commentService) {
//        Profile currentProfile = profileService.getCurrentProfile();

        User currentUser = userService.getCurrentUser();
        
        Profile currentProfile = profileService.getProfileByUsername(currentUser.getUsername());
        nameLabel = new Label(currentProfile.getName() + " ");
        surnameLabel = new Label(currentProfile.getSurname());
        bioLabel = new Label(currentProfile.getBio());
        
        following = new Button("Following (" + currentUser.getFollowing().size() + ")");
        following.addClickListener(event -> {
        	
            // List ar lietotājiem, kuriem pašreizējais lietotājs seko
            Collection<Profile> followingUsers = currentUser.getFollowing();
            
            
            // Following pop up window
            Dialog followingDialog = new Dialog();
            followingDialog.setCloseOnEsc(true);
            followingDialog.setCloseOnOutsideClick(true);

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
                
                Button unfollowButton = new Button(new Icon(VaadinIcon.BAN));
                unfollowButton.getStyle().set("color", "#ff8795");
                
                followingLayouts.add(profilePic, new Label(followingUser.getUser().getUsername()), unfollowButton);

                profilePic.addClickListener(clickEvent -> {
                    UI.getCurrent().navigate("/profiles/" + followingUser.getUser().getUsername());
                    followingDialog.close();
                });
            
                unfollowButton.addClickListener(unfollowEvent -> {
                    profileService.removeFollowing(currentUser, followingUser);
                	followingDialog.close();
                    UI.getCurrent().getPage().reload();
                });
                
                followingLayout.add(followingLayouts);
            }
            followingDialog.add(followingLayout);
            followingDialog.open();
        });
        
        //followers = new Label("Followers " + String.valueOf(currentProfile.getFollowers().size()));
        followers = new Button("Followers (" + currentProfile.getFollowers().size() + ")");
        followers.addClickListener(event -> {
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

                followerLayout.addClickListener(clickEvent -> {
                    UI.getCurrent().navigate("/profiles/" + followersUser.getUsername());
                    followersDialog.close();
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

        List<UserPost> userPosts = (List<UserPost>) currentProfile.getPost();
        Collections.sort(userPosts, Comparator.comparing(UserPost::getCreatedDate));
        
        Div postsDiv = new Div();
        
        if(userPosts.isEmpty()) {
        	Label noPostsLabel = new Label("There are no posts.");
            postLayout.add(noPostsLabel);
        }else {
        	for (UserPost post : userPosts) {
        	    Image postImage = utils.byteToImage(post.getImage());
        	    postImage.setWidth("200px");
        	    postImage.setHeight("200px");

        	    Label postDescription = new Label(post.getDescription());
        	    
        	    Button likeButton = new Button(String.valueOf(likeService.getLikedCount(post)));
        	    Icon heartIcon = VaadinIcon.HEART.create(); 
        	    likeButton.setIcon(heartIcon);
        	    likeButton.addClickListener(likeEvent -> {
        	        likeService.addLike(post, currentUser);
        	        int updatedLikeCount = likeService.getLikedCount(post);
        	        likeButton.setText(String.valueOf(updatedLikeCount));
        	    });
        	    
        	    Button commentButton = new Button("Comment");
        	    Icon messageIcon = VaadinIcon.CHAT.create(); 
        	    commentButton.setIcon(messageIcon);
        	    commentButton.addClickListener(e -> commentService.openCommentDialog(post, currentUser, currentProfile));
        	    
        	    Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        	    deleteButton.addClickListener(deleteEvent -> {
        	        Dialog confirmationDialog = new Dialog();
        	        confirmationDialog.setCloseOnEsc(false);
        	        confirmationDialog.setCloseOnOutsideClick(false);

        	        Label confirmationMessage = new Label("You want to delete this post?");
        	        Button confirmDeleteButton = new Button("Yes");
        	        Button cancelDeleteButton = new Button("No");

        	        confirmDeleteButton.addClickListener(confirmDeleteEvent -> {
        	        	userPostService.deletePost(post);
        	            confirmationDialog.close();
        	            UI.getCurrent().getPage().reload();
        	        });

        	        cancelDeleteButton.addClickListener(cancelDeleteEvent -> {
        	            confirmationDialog.close();
        	        });

        	        confirmationDialog.add(confirmationMessage, confirmDeleteButton, cancelDeleteButton);
        	        confirmationDialog.open();
        	    });
        	    
        	    HorizontalLayout commentAndLike = new HorizontalLayout(likeButton, commentButton);
        	    VerticalLayout postColumn = new VerticalLayout( postDescription,commentAndLike, deleteButton);
        	    HorizontalLayout postRow = new HorizontalLayout(postImage, postColumn);
        	    postRow.addClassName("every-post");
        	    postRow.setWidth("100%");
        	    postsDiv.add(postRow);
        	}
        }
        
        Button addPostButton = new Button();
        addPostButton.setIcon(new Icon(VaadinIcon.PLUS));
        addPostButton.setText("Add Post");
        
        addPostButton.addClickListener(event -> {
            Dialog postDialog = new Dialog();
            postDialog.setCloseOnEsc(false);
            postDialog.setCloseOnOutsideClick(false);

            TextField descriptionField = new TextField("Description");
            Upload imageUpload = new Upload();
            imageUpload.setAcceptedFileTypes("image/jpeg", "image/png");
            MemoryBuffer buffer = new MemoryBuffer();
            imageUpload.setReceiver(buffer::receiveUpload);
           
            Button saveButton = new Button("Save");
            saveButton.addClickListener(saveEvent -> {
               
                byte[] imageBytes = new byte[0];
                try {
                    imageBytes = buffer.getInputStream().readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UserPost newPost = new UserPost(descriptionField.getValue(), 0, imageBytes, currentProfile);
                profileService.saveUserPost(newPost);
                currentProfile.addPost(newPost);               
                
                postDialog.close();
                saveEvent.getSource().getUI().ifPresent(ui -> {
                	postDialog.close();
                	
                    ui.getPage().reload();
                });
                
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.addClickListener(cancelEvent -> {
                postDialog.close();
            });

            HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

            VerticalLayout dialogContent = new VerticalLayout(descriptionField, imageUpload, buttonLayout);
            postDialog.add(dialogContent);

            postDialog.open();
        });
        
        // Edit Profile pop up
        editProfilePopup = new Dialog();
        editProfilePopup.setCloseOnEsc(true);
        editProfilePopup.setCloseOnOutsideClick(true);
        editProfilePopup.setModal(true);
        editProfilePopup.setWidth("400px");
        editProfilePopup.setHeight("500px");
        
        // Edit Profile button
        editProfileButton = new Button();
        editProfileButton.setIcon(new Icon(VaadinIcon.EDIT));
        editProfileButton.setText("Edit Profile");
        editProfileButton.addClickListener(event -> editProfilePopup.open());
        editProfileButton.addClassName("edit-profile-button");


        // edit profile image
        profileImagePreview = new Image();
        profileImagePreview.setWidth("200px");
        profileImagePreview.setHeight("200px");
        profileImagePreview.getStyle().set("border-radius", "50%");
            
        // update profile image upload
        MemoryBuffer buffer = new MemoryBuffer();
        profileImageUpload = new Upload(buffer);
        profileImageUpload.setAcceptedFileTypes("image/jpeg", "image/png");
        profileImageUpload.setDropLabel(new Label("Drag and drop a profile image here"));
        profileImageUpload.setUploadButton(new Button("Upload"));
        profileImageUpload.setMaxFiles(1);
        profileImageUpload.addSucceededListener(event -> {
            try {
                InputStream inputStream = buffer.getInputStream();
                byte[] imageBytes = inputStream.readAllBytes();
                String fileName = event.getFileName();
                profileService.updateProfilePicture(currentProfile, imageBytes);
                profileImagePreview.setSrc(utils.byteToDataUrl(imageBytes,fileName));
                profileImage.setSrc(utils.byteToDataUrl(imageBytes,fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Name
        nameField = new TextField("Name");
        nameField.setValue(currentProfile.getName());
        nameField.setRequiredIndicatorVisible(true);
        nameField.setRequired(true);
        nameField.setPattern("[a-zA-Z]+");
        nameField.setErrorMessage("Name can only contain letters.");

        // Surname
        surnameField = new TextField("Surname");
        surnameField.setValue(currentProfile.getSurname());
        surnameField.setRequiredIndicatorVisible(true);
        surnameField.setRequired(true);
        surnameField.setPattern("[a-zA-Z]+");
        surnameField.setErrorMessage("Surname can only contain letters.");

        // Bio
        bioField = new TextArea("Bio");
        bioField.setValue(currentProfile.getBio());

        // Save button
        Button saveButton = new Button("Save");
        saveButton.addClickListener(event -> {
            if(nameField.getValue() != null && surnameField.getValue() != null) {
	        	currentProfile.setName(nameField.getValue());
	            currentProfile.setSurname(surnameField.getValue());
	            currentProfile.setBio(bioField.getValue());
	            
	            profileService.update(currentProfile);
	            
	            nameLabel.setText(currentProfile.getName() + " ");
	            surnameLabel.setText(currentProfile.getSurname());
	            bioLabel.setText(currentProfile.getBio());
	            
	            editProfilePopup.close();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> editProfilePopup.close());

        // Edit layout
        VerticalLayout editProfileLayout = new VerticalLayout();
        editProfileLayout.add(profileImagePreview, profileImageUpload, nameField, surnameField, bioField, saveButton, cancelButton);
        editProfilePopup.add(editProfileLayout);
        
        Div nameSurnameDiv = new Div(nameLabel, surnameLabel, followers, following);
        nameSurnameDiv.addClassName("name-surname-div");

        Div bioDiv = new Div(bioLabel);
        bioDiv.addClassName("bio-div");

        Div profileImageContainer = new Div(profileImage);
        profileImageContainer.addClassName("profile-image-container");
        
        Div noPostsDiv = new Div(postLayout);
        noPostsDiv.setWidth( "100%");
        noPostsDiv.getStyle().set("text-align", "center");
        
        Div profileContainer = new Div(profileImageContainer, nameSurnameDiv, bioDiv, postLayout, addPostButton, editProfileButton);
        profileContainer.addClassName("profile-view");
        
        Div postsContainer = new Div(postsDiv);
        postsContainer.addClassName("post-view");

        Div mainContainer = new Div(profileContainer, postsContainer);
        add(mainContainer);
    }
}
package com.example.application.views;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;
import com.example.application.data.service.ICommentService;
import com.example.application.data.service.ILikeService;
import com.example.application.data.service.INotificationService;
import com.example.application.data.service.IUserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;


@PageTitle("Home")
@CssImport("./styles/views/home-view.css")
public class HomeView extends VerticalLayout {
	
	@Autowired
	private INotificationService notifService;
	
	@Autowired
	private ILikeService likeService;
	
	@Autowired
	private ICommentService commentService;
	
	public HomeView(IUserService userService, ILikeService likeService, INotificationService notifService, ICommentService commentService) {
        setAlignItems(Alignment.CENTER);
        
        User currentUser = userService.getCurrentUser();
        Tabs tabs = new Tabs();
        Tab followingTab = new Tab("Following");
        Tab randomTab = new Tab("Random");

        tabs.add(followingTab, randomTab);
        add(tabs);

        VerticalLayout userPostLayout = new VerticalLayout();
        userPostLayout.setWidthFull();
        userPostLayout.setAlignItems(Alignment.CENTER);
        
//        List<UserPost> userPosts = userService.getFollowingUserPosts(currentUser);
        Set<Profile> profiles = currentUser.getFollowing();
        List<UserPost> userPosts = new ArrayList<>();
        for (Profile profile : profiles) {
            List<UserPost> posts = (List<UserPost>) profile.getPost();
            for (UserPost post : posts) {
                userPosts.add(post);
            }
        }

        if (userPosts != null && !userPosts.isEmpty()) {
            for (UserPost userPost : userPosts) {
            	int postLikes = likeService.getLikedCount(userPost);
                Card userPostCard = createUserPostCard(userPost, currentUser, postLikes);
                userPostLayout.add(userPostCard);
            }
        } else {
        	Div noPostsDiv = new Div();
            noPostsDiv.addClassName("no-posts-div");
            Text noPostsText = new Text("No posts available");
            Icon postIcon = VaadinIcon.PICTURE.create();
            postIcon.setSize("48px");

            noPostsDiv.add(postIcon, noPostsText);
            userPostLayout.add(noPostsDiv);
        }

        add(userPostLayout);
    }

	private Card createUserPostCard(UserPost userPost, User currentUser, int postLikes) {
	    Profile profile = userPost.getProfile();
	    Image profilePicture = new Image(new StreamResource("profile-picture.jpg", () -> new ByteArrayInputStream(profile.getProfilePicture())), "Profile Picture");
	    profilePicture.setWidth("50px");
	    profilePicture.setHeight("50px");
	    profilePicture.getStyle().set("border-radius", "50%");

	    Span username = new Span(profile.getUser().getUsername());

	    Image image = new Image(new StreamResource("image.jpg", () -> new ByteArrayInputStream(userPost.getImage())), "Post Image");
	    image.setWidthFull();

	    Div content = new Div(new Span(userPost.getDescription()));

	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM HH:mm");
	    Label creationDateLabel = new Label(userPost.getCreatedDate().format(dateFormatter).toString());

	    HorizontalLayout userInfo = new HorizontalLayout(profilePicture, username, new Div(), creationDateLabel);
	    userInfo.getStyle()
	        .set("display", "flex")
	        .set("justify-content", "space-between")
	        .set("align-items", "center");

	    Button likeButton = new Button(String.valueOf(postLikes));
	    Icon heartIcon = VaadinIcon.HEART.create(); 
	    likeButton.setIcon(heartIcon);

	    likeButton.addClickListener(likeEvent -> {
	        likeService.addLike(userPost, currentUser);
	        int updatedLikeCount = likeService.getLikedCount(userPost);
	        likeButton.setText(String.valueOf(updatedLikeCount));
	        notifService.addLikeNotification(currentUser,profile);
	    });
	    
	    Button commentButton = new Button("Comment");
	    Icon messageIcon = VaadinIcon.CHAT.create(); 
	    commentButton.setIcon(messageIcon);
	    commentButton.addClickListener(e -> commentService.openCommentDialog(userPost, currentUser, profile));
	    
	    VerticalLayout cardBodyLayout = new VerticalLayout(userInfo, image, content, likeButton, commentButton);

	    Card card = new Card(cardBodyLayout);
	    card.setWidth("400px");
	    card.getStyle().set("margin", "10px");
	    return card;
	}
	
}

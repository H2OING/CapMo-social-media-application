package com.example.application.data.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Comments;
import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Notification;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.StreamResource;

@Service
public class CommentServiceImpl implements ICommentService{

	@Autowired
	private ICommentRepo commentRepo;
	
	@Autowired
	private IUserPostRepo userPostRepo;
	
	@Autowired
	private INotificationService notifService;
	
	public void addComment(UserPost userPost, String message, User currentUser) {
		Comments comment = new Comments(userPost, message, currentUser);
		commentRepo.save(comment);
		
		userPost.getComments().add(comment);
		userPostRepo.save(userPost);			
	}
	
	public void deleteComments(UserPost userPost) {
		List<Comments> comments = commentRepo.findByUserPost(userPost);
		userPost.getComments().removeAll(comments);
		commentRepo.deleteAll(comments);
	}
	
	public void openCommentDialog(UserPost userPost, User currentUser, Profile profile) {
	    Dialog dialog = new Dialog();
	    dialog.setWidth("400px");

	    List<Comments> comments = (List<Comments>) userPost.getComments();
	    VerticalLayout commentLayout = new VerticalLayout();

	    Div commentsContainer = new Div();
	    commentsContainer.setHeight("200px");
	    commentsContainer.getStyle()
	            .set("overflow-y", "auto")
	            .set("padding-right", "10px");

	    commentLayout.setWidth("100%");

	    comments.forEach(comment -> {
	        HorizontalLayout commentHeaderLayout = new HorizontalLayout();
	        	commentHeaderLayout.setAlignItems(Alignment.CENTER);

	        Profile commenterProfile = comment.getUser().getProfile();

	        Image profilePicture = new Image(new StreamResource("profile-picture.jpg",
	                () -> new ByteArrayInputStream(commenterProfile.getProfilePicture())), "Profile Picture");
	        	profilePicture.setWidth("30px");
	        	profilePicture.setHeight("30px");
	        	profilePicture.getStyle().set("border-radius", "50%");

	        Span username = new Span(commenterProfile.getUser().getUsername());

	        commentHeaderLayout.add(profilePicture, username);

	        Span commentText = new Span(comment.getMessage());
	        Div commentTextContainer = new Div(commentText);
	        commentTextContainer.getStyle().set("word-wrap", "break-word");

	        VerticalLayout commentItemLayout = new VerticalLayout(commentHeaderLayout, commentTextContainer);
	        commentItemLayout.getStyle()
            .set("background-color", "#F2F2F2")
            .set("border-radius", "10px")
            .set("padding", "10px")
            .set("margin-bottom", "10px");
	        
	        commentLayout.add(commentItemLayout);
	    });
	    
	    commentsContainer.add(commentLayout);

	    TextArea newCommentField = new TextArea();
	    newCommentField.setPlaceholder("Add a comment");

	    Button submitButton = new Button("Submit");
	    submitButton.addClickListener(e -> {
	        String newCommentText = newCommentField.getValue();
	        addComment(userPost, newCommentText, currentUser);
	        notifService.addCommentNotification(currentUser, profile);

	        HorizontalLayout commentHeaderLayout = new HorizontalLayout();
	        commentHeaderLayout.setAlignItems(Alignment.CENTER);
	        
	        Image profilePicture = new Image(new StreamResource("profile-picture.jpg",
	                () -> new ByteArrayInputStream(currentUser.getProfile().getProfilePicture())), "Profile Picture");
	        profilePicture.setWidth("30px");
	        profilePicture.setHeight("30px");
	        profilePicture.getStyle().set("border-radius", "50%");

	        Span username = new Span(currentUser.getUsername());
	        commentHeaderLayout.add(profilePicture, username);
	        
	        Span newCommentTextSpan = new Span(newCommentText);
	        Div commentTextContainer = new Div(newCommentTextSpan);
	        commentTextContainer.getStyle().set("word-wrap", "break-word");

	        VerticalLayout commentItemLayout = new VerticalLayout(commentHeaderLayout, commentTextContainer);
	        commentItemLayout.getStyle()
            .set("background-color", "#F2F2F2")
            .set("border-radius", "10px")
            .set("padding", "10px")
            .set("margin-bottom", "10px");
	        
	        commentLayout.add(commentItemLayout);
	        newCommentField.clear();
	        commentsContainer.getElement().executeJs("this.scrollTop = this.scrollHeight;");
	    });

	    FormLayout commentFormLayout = new FormLayout(newCommentField, submitButton);

	    dialog.add(commentsContainer, commentFormLayout);
	    dialog.open();
	}
}

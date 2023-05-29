package com.example.application.data.entity;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Table
@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Profile{

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(value = AccessLevel.NONE)
	@Column(name="PID")
	private Long pID;
	
	@NonNull
	@Column(name = "Private")
	private boolean isPrivate;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "idU")
    private User user;

	private String name;
	
	private String surname;

	private String bio;
	
	@Lob
	private byte[] profilePicture;
	
	@OneToMany(mappedBy="profile", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<UserPost> post = new ArrayList<>();
	
	@OneToMany(mappedBy="profile", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<Notification> notification = new ArrayList<>();
	
	@Column(name = "followers")
	@ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
	@BatchSize(size = 10)
	@Fetch(FetchMode.SUBSELECT)
    private Set<User> followers = new HashSet<>();
	
	public Profile(boolean isPrivate, User user, String name, String surname, String bio) {
		super();
		this.isPrivate = isPrivate;
		this.user = user;
		this.name = name;
		this.surname = surname;
		this.bio = bio;
	}

	public void addPost(UserPost newPost) {
		post.add(newPost);	
	}
	
	public void addFollower(User user) {
		followers.add(user);
		user.addFollowing(this);
	}
	
	public void removeFollower(User user) {
		user.removeFollowing(this);
		followers.remove(user);		
	}
	
	public int getUserPostCount() {
	    return post.size();
	}
	
	public int getFollowersCount() {
	    return followers.size();
	}
	
	public int getFollowingCount() {
	    return user.getFollowing().size();
	}
	
	public int getNotificationCount() {
	    return notification.size();
	}
	
}

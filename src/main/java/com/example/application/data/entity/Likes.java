package com.example.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table
@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Likes extends Auditable<String>{

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(value = AccessLevel.NONE)
	@Column(name="lID")
	private Long lID;
	
	private boolean isLiked;
	
	@ManyToOne
	@JoinColumn(name = "upID")
	@ToString.Exclude
	private UserPost userPost;
	
	@ManyToOne
	@JoinColumn(name = "IdU")
	@ToString.Exclude
	private User user;

	public Likes(boolean isLiked, UserPost userPost, User user) {
		super();
		this.isLiked = isLiked;
		this.userPost = userPost;
		this.user = user;
	}
	
}

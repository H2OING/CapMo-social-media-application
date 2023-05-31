package com.example.application.data.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
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
public class UserPost extends Auditable<String>{

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(value = AccessLevel.NONE)
	@Column(name="upID")
	private Long upID;
	
	private String description;
	
	@Lob
	@Column(length = 5242880)
	private byte[] image;
	
	@ManyToOne
	@JoinColumn(name = "PID")
	@ToString.Exclude
	private Profile profile;
	
	@OneToMany(mappedBy="userPost", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<Likes> like = new ArrayList<>();
	
	@OneToMany(mappedBy="userPost", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<Comments> comments = new ArrayList<>();

	public UserPost(String description, int likes, byte[] image, Profile profile) {
		super();
		this.description = description;
		this.image = image;
		this.profile = profile;
	}
	
	public int likes(){
		return like.stream().filter(Likes::isLiked).collect(Collectors.toList()).size();
	}
	
	public int commentCount(){
		return comments.size();
	}	
}

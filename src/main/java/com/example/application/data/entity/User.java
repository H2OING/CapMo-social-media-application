package com.example.application.data.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "UserAcc")
@Entity
public class User{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="IdU")
	private int idU;

	@NonNull
	@NotBlank //TODO (message = "...")
	@Column(name = "Username")
	private String username;
	
	@NonNull
	private String passwordHashed;
	
	@NonNull
	private String passwordSalt;
	
	@ManyToOne
	@JoinColumn(name = "RoleID")
	@ToString.Exclude
	private Role role;
	
	@OneToOne(mappedBy = "user")
	private Profile profile;
	
	@NonNull
	@Column(name="Is_Active")
	public boolean active;
	
	@Column(name = "following")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		    name = "USER_ACC_FOLLOWING",
		    joinColumns = @JoinColumn(name = "FOLLOWERS_IDU"),
		    inverseJoinColumns = @JoinColumn(name = "FOLLOWING_PID")
		)
	@BatchSize(size = 10)
	@Fetch(FetchMode.SUBSELECT)
    private Set<Profile> following = new HashSet<>();
	
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<Likes> like = new ArrayList<>();
	
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<Likes> comment = new ArrayList<>();
	
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<Feedback> feedBack = new ArrayList<>();
	
	public User(String username, String password, Role role, boolean active) {
        this.username = username;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHashed = DigestUtils.sha1Hex(password + passwordSalt);
        this.role = role;
        this.active = active;
    }
	
	public boolean checkPassword(String password) {
		return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHashed);
	}

    public boolean isActive() {
        return active;
    }
    
    public void addFollowing(Profile profile) {
    	following.add(profile);
	}
    
    public void removeFollowing(Profile profile) {
    	following.remove(profile);
	}
    
    public void addFeedbakc(Feedback feedback) {
    	feedBack.add(feedback);
	}
    
    public boolean isFollowing(Profile profile) {
    	return following.contains(profile);
    }
}
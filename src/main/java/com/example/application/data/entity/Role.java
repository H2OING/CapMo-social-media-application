package com.example.application.data.entity;

import java.util.ArrayList;
import java.util.Collection;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
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
@NoArgsConstructor
public class Role{

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(value = AccessLevel.NONE)
	@Column(name="RoleID")
	private Long roleId;
	
	@Column(name="Title")
	private String title;
	
	@Column(name = "Description")
	private String desc;
	
	@OneToMany(mappedBy="role")
	@ToString.Exclude
	private Collection<User> users = new ArrayList<>();

	public void addUserAccount(User userAccount){
		users.add(userAccount);
    }
	
	public void deleteUserAccount(User userAccount){
		users.remove(userAccount);
    }

    public Role(String title, String desc) {
        setTitle(title);
        setDesc(desc);
        this.users = new ArrayList<>();
    }
}

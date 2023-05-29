package com.example.application.data.entity;

import java.time.LocalDateTime;

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

@Data
@Table
@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends Auditable<String>{

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(value = AccessLevel.NONE)
	@Column(name="nID")
	private Long nID;
	
	private String description;
	
	private NotificationType notifType;
	
	@ManyToOne
	@JoinColumn(name = "PID")
	@ToString.Exclude
	private Profile profile;

	public Notification(String description, Profile profile, NotificationType notifType) {
		super();
		this.description = description;
		this.profile = profile;
		this.notifType = notifType;
	}
}

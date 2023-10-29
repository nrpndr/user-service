package com.cineevent.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
	private int id;
	
	@Column(name="username", nullable = false)
	private String userName;
	
	@Column(name="password", nullable = false)
	private String password;
	
	@Column(name="firstname", nullable = false)
	private String firstName;
	
	@Column(name="lastname", nullable = true)
	private String lastName;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="userrole", nullable = false)
	private String userRole;
	
}

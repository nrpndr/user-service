package com.cineevent.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cineevent.userservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUserName(String username);

	public User findByEmail(String email);

}

package com.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.model.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser, Integer> {

	Optional<MyUser> findByUsername(String username);

	Optional<MyUser> findById(int id);

	public void deleteById(int id);
}

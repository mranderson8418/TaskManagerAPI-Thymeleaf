package com.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taskmanager.model.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser, Integer> {

	Optional<MyUser> findByUsername(String username);

	Optional<MyUser> findById(int id);

	public MyUser save(MyUser myUser);

	public void deleteById(int id);

	@Query("SELECT t FROM MyUser t WHERE t.username = :username")
	List<MyUser> findAllUsers(@Param("username") String username);
}

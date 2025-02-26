
package com.taskmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taskmanager.model.MyTask;

@Repository // Mark this Longerface as a Spring Data repository
public interface TaskRepository extends JpaRepository<MyTask, Integer> {

	// void save(String string);

	// public void saveById(int id);

	public void deleteById(int taskNumber);

	@Query("SELECT t FROM MyTask t WHERE t.username = :username")
	Page<MyTask> findAllTasksByUsername(@Param("username") String username, PageRequest pageRequest);

	@Query("SELECT t FROM MyTask t WHERE t.username = :username")
	List<MyTask> findAllTasksByUsernameObjectList(@Param("username") String username);

	// @Query("SELECT t FROM MyTask t WHERE t.id = :id")
	// <Optional> MyTask saveTaskById(@Param("id") Integer id);

}

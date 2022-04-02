package com.joey.databasemanager.repository.sql;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.joey.databasemanager.beans.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	
	@Query("select u from User u where u.username= :name")
	public User searchByName(@Param("name") String username);
}

package com.joey.databasemanager.repository.nosql;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.joey.databasemanager.dto.User;

public interface UserNosqlRepository extends MongoRepository<User,String>{
	
}

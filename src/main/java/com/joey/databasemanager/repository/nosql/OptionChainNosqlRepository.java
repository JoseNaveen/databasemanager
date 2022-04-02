package com.joey.databasemanager.repository.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.joey.databasemanager.dto.OptionChain;

public interface OptionChainNosqlRepository extends MongoRepository<OptionChain, String> {
	
}
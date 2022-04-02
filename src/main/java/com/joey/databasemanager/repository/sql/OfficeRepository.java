package com.joey.databasemanager.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joey.databasemanager.classicmodelsbeans.Office;

public interface OfficeRepository extends JpaRepository<Office, String> {
	
}

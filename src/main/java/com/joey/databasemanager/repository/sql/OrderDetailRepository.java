package com.joey.databasemanager.repository.sql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.joey.databasemanager.classicmodelsbeans.OrderDetailId;
import com.joey.databasemanager.classicmodelsbeans.Orderdetail;

public interface OrderDetailRepository extends JpaRepository<Orderdetail, OrderDetailId> {

	
}

package com.joey.databasemanager.repository.sql;

import org.springframework.data.repository.CrudRepository;

import com.joey.databasemanager.classicmodelsbeans.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

}

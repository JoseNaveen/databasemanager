package com.joey.databasemanager.repository.sql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.joey.databasemanager.classicmodelsbeans.Product;


@Repository
public interface ProductRepository extends JpaRepository<com.joey.databasemanager.classicmodelsbeans.Product, Integer>{
	
	
	@Query(value="""
			select distinct quantity_in_stock
			from products p1
			where :max=(select count(distinct quantity_in_stock) from products p2
			where p1.quantity_in_stock<=p2.quantity_in_stock);
			""",nativeQuery=true)
	public short findMax(int max);
	
	
}

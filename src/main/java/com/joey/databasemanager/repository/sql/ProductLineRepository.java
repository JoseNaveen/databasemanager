package com.joey.databasemanager.repository.sql;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.joey.databasemanager.classicmodelsbeans.Product;
import com.joey.databasemanager.classicmodelsbeans.ProductLine;


@Repository
public interface ProductLineRepository extends CrudRepository<ProductLine, String>{
	
	@Query("select p from ProductLine p where p.productLine= :plinename")
	public ProductLine searchByplinename(@Param("plinename") String plinename);
}

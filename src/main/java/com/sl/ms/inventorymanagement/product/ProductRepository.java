package com.sl.ms.inventorymanagement.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;




public interface ProductRepository extends JpaRepository<Product, Long>{
	
	@Query("select id , name from SL_PRODUCT ")
	Object[] findSupportedProducts();

	

}

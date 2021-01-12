package com.sl.ms.inventorymanagement.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sl.ms.inventorymanagement.product.Product;
import com.sl.ms.inventorymanagement.product.ProductController;
import com.sl.ms.inventorymanagement.product.ProductRepository;

@Service
public class productservice {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductRepository prodrepo;

	public List<Product> save(List<Product> product) {
		return prodrepo.saveAll(product);		
	}
	
	public Product save(Product product) {
		return prodrepo.save(product);		
	}


	public void delete(Long id) {
		
		 prodrepo.deleteById(id);
	}

	public Optional<Product> getById(Long id) {
		return prodrepo.findById(id);
	}

	
	@Cacheable("Products")
	public Object[] findSupportedProducts() {
		try
        {
            log.info("Going to sleep for 5 Secs.. to simulate backend call.");
            Thread.sleep(1000*5);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
		return prodrepo.findSupportedProducts();
	}


	
	

}

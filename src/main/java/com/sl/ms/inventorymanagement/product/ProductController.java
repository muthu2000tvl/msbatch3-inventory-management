package com.sl.ms.inventorymanagement.product;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sl.ms.inventorymanagement.exception.ProductNotFoundException;
import com.sl.ms.inventorymanagement.inventory.Inventory;
import com.sl.ms.inventorymanagement.inventory.InventoryRepository;
import com.sl.ms.inventorymanagement.service.ProductFileUploadService;
import com.sl.ms.inventorymanagement.service.productservice;



@RestController
public class ProductController {
	
	@Autowired
	ProductRepository prrepo;
	
	@Autowired
	InventoryRepository invrepo;
	
	@Autowired
	productservice prodservice;
	
	@Autowired
	ProductFileUploadService prodfileservice;
	
	
	//private Logger log = LogManager.getLogger(ProductController.class.getName());
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class.getName());
	
	/*
	 * To display the Products from repo
	 */
	
	@GetMapping("/products")
	public List<Product> getProducts(){
		
		log.info("Entering getProducts() Controller");	
				
		List<Product> product = (List<Product>) prrepo.findAll();
		//return log.traceExit(product);
		return product;
	}
	
	/*
	 * To display the supported products from repo
	 */
	
	@GetMapping("/supportedProducts")	
	public Object[] getSupportedProducts(){
		
		log.info("Entering getSupportedProducts Controller");
		Object[] product = prodservice.findSupportedProducts();
		return product;
	}
	
	/*
	 * To check products are available in repo
	 */
	
	@GetMapping("/checkProducts/{Id}")
	public boolean getAvailableProducts(@PathVariable("Id") Long Id){
		log.info("Rest call from Orders API ");
		
		Optional<Product> product = prrepo.findById(Id);
		
		int quan = product.get().getQuantity();
		
		log.info("Reached Inventory API ");
		
		if (quan > 0)
			return true;
		else {
			return false;
		}
		
	}
	
	/*
	 * To display the Products by ID from repo
	 */
	
	@RequestMapping("/products/{Id}")
	public Optional<Product> getProductsById(@PathVariable("Id") Long Id){
		
		log.info("Entering getProductsById Controller");
		
		Optional<Product> product = prrepo.findById(Id);
				
		if (!product.isPresent())
			throw new ProductNotFoundException("Id" + Id);
		else {
			return product;
		}
	
	}
	
	/*
	 * To save many Products
	 */

	
	@PostMapping(path = "/products", consumes = {"application/json"})
	@ResponseBody
	public ResponseEntity<List<Product>> saveProducts(@RequestBody List<Product> product) {
		log.info("Entering saveProducts Controller");
		prodservice.save(product);
		return new ResponseEntity<List<Product>>(product, HttpStatus.OK);
	}
	
	/*
	 * To save a new product against an inventory
	 */
	
	@PostMapping(path = "/products/create/{Id}", consumes = {"application/json"})
	@ResponseBody
	public ResponseEntity<Object> createProductsForInv(@PathVariable ("Id") Long Id, @RequestBody Product product) {
		log.info("Entering createProductsForInv Controller");
		Optional<Inventory> inv = invrepo.findById(Id);
		

		if(!inv.isPresent()) {
			throw new ProductNotFoundException("Id" + Id);
			
		}else {
			
			Inventory inventory = inv.get();			
			log.info("------------------inside else loop" + inv);			
			((Product) product).setInventory(inventory);			
			
			prrepo.save(product);
		}
		
		return new ResponseEntity<Object>(product, HttpStatus.OK);
	}
	
	
	/*
	 * To save products in multi part file
	 */
	
	@PostMapping("product/upload")
	  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		
		log.info("Entering uploadFile Controller");
		
	    String message = "";
	    try {
	    	prodfileservice.store(file);

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(message);
	      
	    } catch (Exception e) {
	    	
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
	      
	    }
	  }
	
		
	/*
	 * To update a product
	 */
	
	@PutMapping(path ="/products/{Id}" , consumes = {"application/json"})
	@ResponseBody
	private ResponseEntity<Object> updateProduct(@PathVariable ("Id") Long Id, @RequestBody Product product) {
		log.info("Entering updateProduct Controller");
		Optional<Inventory> inv = invrepo.findById(Id);
		Optional<Product> pro = prrepo.findById(Id);
		
		log.info("------------------inside else loop" + pro);
		
		if(!pro.isPresent()) {
			throw new ProductNotFoundException("Id" + Id);
		}else {
			Inventory inventory = inv.get();			
			log.info("------------------inside else loop" + inv);			
			((Product) product).setInventory(inventory);
			prrepo.save(product);
		
		}
		return new ResponseEntity<Object>(product, HttpStatus.OK);
	}
	
	/*
	 * To delete a particular product with product ID
	 */
	
		@DeleteMapping("/products/{id}")
		private Optional<Product> deleteProductsById(@PathVariable("id") Long id) {
			
			log.info("Entering deleteProductsById Controller");
			
			Optional<Product> pro = prrepo.findById(id);
			if(!pro.isPresent()) {
				throw new ProductNotFoundException("Id" + id);
			}else {
				Optional<Product> delete = prodservice.getById(id);
			prodservice.delete(id);
			return delete;
			}
			
		}
		
		
		/*
		 * **********************Unit Testing******************
		 */

		@PutMapping("/testProductUpdate")
		private Product updatetest(@RequestBody Product product) {
			prodservice.save(product);
			return product;
	
		}
		
//		@DeleteMapping("/testProductdelete/{id}")
//		private void deletetest(@PathVariable("id") Long id) {
//			prodservice.delete(id);		
//			//prrepo.deleteById(id);
//		}
		
}

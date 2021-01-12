package com.sl.ms.inventorymanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.sl.ms.inventorymanagement.product.Product;
import com.sl.ms.inventorymanagement.product.ProductRepository;
import com.sl.ms.inventorymanagement.product.ProductUpload;
import com.sl.ms.inventorymanagement.service.ProductFileUploadService;
import com.sl.ms.inventorymanagement.service.productservice;




@SpringBootTest
@RunWith(SpringRunner.class)
public class InventoryServiceTest {
	
	@Autowired
	productservice prodser;
	
	@Autowired
	ProductFileUploadService prodUp;
	
	@MockBean
	ProductRepository prodrepo;
	
	 private List<Product> prodList;
	
	 @Test
	    public void saveProductsTest()
	    {
	        Product prod = new Product(1L,"Muthu",500,400);
	        when(prodrepo.save(prod)).thenReturn(prod);	        
	        assertEquals(prod, prodser.save(prod));
	    }
	 
	 
	@Test
	 public void storeTest() throws IOException {
		 MockMultipartFile file = new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
		 
		 ProductUpload pro = new ProductUpload("foo", "foo.txt", "Hello World".getBytes());
		 prodUp.store(file);
	   assertThat(prodUp.getAllFiles()).isNotEqualTo(pro);
	 }
	
	 
	 @SuppressWarnings("deprecation")
	@Test
	    void getSupportedProductsTest() throws Exception {
		 final Long prodId = 1L;
		 Object[] product = {1L, "Muthu"};
		 Mockito.when(prodrepo.findSupportedProducts()).thenReturn(product);
		  assertEquals(product, prodser.findSupportedProducts());
	                
	    }
	 
	 @Test
	    void getById(){
	        final Long id = 1L;
	        final Product prod = new Product(1L, "Muthu",100, 200);
	        given(prodrepo.findById(id)).willReturn(Optional.of(prod));
	        final Optional<Product> expected  =prodser.getById(id);
	        assertThat(expected).isNotNull();
	    }
	 
	 @Test
	    public void saveListofProductsTest()
	    {
		 
		 this.prodList = new ArrayList<>();
	       this.prodList.add(new Product(1L,"Muthu",500,400));
	       this.prodList.add(new Product(2L,"Muthu",500,400));
	       this.prodList.add(new Product(3L,"Muthu",500,400));
		 
		 when(prodrepo.saveAll(prodList)).thenReturn(prodList);
			assertEquals(3, prodser.save(prodList).size());

	    }
	
	 @Test
	 public void deleteProductsTest() {
		 Product prod = new Product(1L,"Muthu",500,400);
		 prodser.delete(1L);
		 verify(prodrepo, times(1)).deleteById(1L);
	 }
	
}

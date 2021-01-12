package com.sl.ms.inventorymanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.ms.inventorymanagement.config.JwtUtil;
import com.sl.ms.inventorymanagement.inventory.InventoryRepository;
import com.sl.ms.inventorymanagement.product.Product;
import com.sl.ms.inventorymanagement.product.ProductController;
import com.sl.ms.inventorymanagement.product.ProductRepository;
import com.sl.ms.inventorymanagement.service.MyUserDetailsService;
import com.sl.ms.inventorymanagement.service.ProductFileUploadService;
import com.sl.ms.inventorymanagement.service.productservice;







@WebMvcTest(controllers = ProductController.class)
@ActiveProfiles("test")
public class InventoryControllerIntegrationTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private productservice prodser;
	
	@MockBean
	private MyUserDetailsService userser;
	
	@MockBean
	private ProductFileUploadService uploadservice;
	
	@MockBean
	private JwtUtil util;
	
	@MockBean
	private ProductRepository prodrepo;
	
	@MockBean
	private InventoryRepository invrepo;
	
	File file;
	
	private List<Product> prodList; 
	
	HttpHeaders httpHeaders = new HttpHeaders();
	
	@BeforeEach                           
    void setUp() {                               
       this.prodList = new ArrayList<>();                                    
       this.prodList.add(new Product(1L, "Item1", 100,299));                               
       this.prodList.add(new Product(2L, "Item2", 200,299));                               
       this.prodList.add(new Product(3L, "Item3", 300,299)); 
  
    }
	
	 @Test
	    void getAllProductTest() throws Exception {

	        given(prodrepo.findAll()).willReturn(prodList); 

	        this.mockMvc.perform(get("/products"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.size()", is(prodList.size())));
	    }
	 
	 
	 @Test
	    void getProductByIdTest() throws Exception {
		 final Long prodId = 1L;
		 Optional<Product> prod = Optional.ofNullable(new Product(1L, "Item1", 100,299));

		 Mockito.when(prodrepo.findById(prodId)).thenReturn(prod);

	        this.mockMvc.perform(get("/products/{Id}" , prodId))
	                .andExpect(status().isOk());
	                
	    }
	 
	 @Test
	    void availableProductTest() throws Exception {
		 final Long prodId = 1L;
		 Optional<Product> prod = Optional.ofNullable(new Product(1L, "Item1", 100,299));

		 Mockito.when(prodrepo.findById(prodId)).thenReturn(prod);

	        this.mockMvc.perform(get("/checkProducts/{Id}" , prodId))
	                .andExpect(status().isOk());
	                
	    }
	 
	 @Test
		void uploadFileTest() throws Exception {
			file = Paths.get("src", "test", "java", "resources", "sample-xls-file.xls").toFile();
			FileInputStream input = new FileInputStream(file);
			MockMultipartFile file1 = new MockMultipartFile("file",input);

			mockMvc.perform(MockMvcRequestBuilders.multipart("/product/upload").file(file1).headers(httpHeaders)
					.contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	 
	 @Test
		void updateProductTest1() throws Exception {
			//addProductsListTest();
		 final Long prodId = 1L;
			Product product = new Product();
			product.setId(1L);
			product.setName("Item1");
			product.setPrice(200);
			product.setQuantity(20);
			String inputInJson = this.mapToJson(product);
			

			mockMvc.perform(MockMvcRequestBuilders.put("/products/{Id}" , prodId).content(inputInJson).headers(httpHeaders)
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN_VALUE))
					.andExpect(MockMvcResultMatchers.status().isNotFound());

		}
	 
	 @Test
	    void getSupportedProductsTest() throws Exception {
		 final Long prodId = 1L;
		
		 Object[] product = {1L, "Muthu"};

		 Mockito.when(prodser.findSupportedProducts()).thenReturn(product);

	        this.mockMvc.perform(get("/supportedProducts"))
	                .andExpect(status().isOk());
	                
	    }
	 
	 @Test
	    void shouldReturn404WhenDeleteProductId() throws Exception {
	        final Long prodId = 1L;
	        given(prodser.getById(prodId)).willReturn(Optional.empty());

	        this.mockMvc.perform(delete("/products/{id}", prodId))
	                .andExpect(status().isNotFound());
	    }
	 
	 @Test
	    void shouldReturn404WhenGetProductId() throws Exception {
	        final Long prodId = 1L;
	        given(prodser.getById(prodId)).willReturn(Optional.empty());

	        this.mockMvc.perform(get("/products/{id}", prodId))
	                .andExpect(status().isNotFound());
	    }
	 
	 @Test
	    void saveProductToInventoryTest() throws Exception {
		 final Long prodId = 1L;
		 httpHeaders.add("Content-Type", "application/json");
		 
		 Product item1 = new Product(1L, "Item1", 100, 100); 
		 
		 String inputInJson = this.mapToJson(item1);

	        
	    	mockMvc.perform(MockMvcRequestBuilders.post("/products/create/{Id}", prodId , item1).content(inputInJson).headers(httpHeaders)
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isNotFound());
	                
	    }
	 
	 @Test
		public void postProductTest() throws Exception {
			Product mockProduct = new Product();
			mockProduct.setId(1L);
			mockProduct.setName("GOD");
			mockProduct.setPrice(599);
			mockProduct.setQuantity(699);
		
			String inputInJson = this.mapToJson(prodList);
			
			String URI = "/products";
			
			Mockito.when(prodser.save(prodList)).thenReturn(prodList);
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.post(URI)
					.accept(MediaType.APPLICATION_JSON).content(inputInJson)
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			
			String outputInJson = response.getContentAsString();
			
			assertThat(outputInJson).isEqualTo(inputInJson);
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		}
	 
	
	 
	 @Test
		public void updateProductTest() throws Exception {
			Product mockProduct = new Product();
			mockProduct.setId(1L);
			mockProduct.setName("GOD");
			mockProduct.setPrice(599);
			mockProduct.setQuantity(699);
		
			String inputInJson = this.mapToJson(mockProduct);
			
			String URI = "/testProductUpdate";
			
			Mockito.when(prodser.save(mockProduct)).thenReturn(mockProduct);
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.put(URI)
					.accept(MediaType.APPLICATION_JSON).content(inputInJson)
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			
			String outputInJson = response.getContentAsString();
			
			assertThat(outputInJson).isEqualTo(inputInJson);
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		}
	 
	 @Test
	    void deleteProductByIdTest() throws Exception {
		 final Long prodId = 1L;
		 
		 Optional<Product> id = prodser.getById(prodId);
		 prodser.delete(prodId);
		 verify(prodser, times(1)).delete(1L);

	        this.mockMvc.perform(delete("/products/{id}" , prodId))
	        .andExpect(status().isNotFound());
	                
	    }
	 
	 
	/**
		 * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
		 */
		private String mapToJson(Object object) throws JsonProcessingException {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);
		}
	
	
}

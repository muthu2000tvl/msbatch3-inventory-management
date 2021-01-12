package com.sl.ms.inventorymanagement.product;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SL_PROD_FILES")
public class ProductUpload {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;
	
	@Column(name = "PRICE")
	private double price;
	
	@Column(name = "QUANTITY")
	private int quantity;

  private String name;

  private String type;

  @Lob
  private byte[] data;

  public ProductUpload() {
  }

  public ProductUpload(String name, String type, byte[] data) {
	  	this.name = name;
	    this.type = type;
	    this.data = data;
	  }
  public String getId() {
	    return id;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public String getType() {
	    return type;
	  }

	  public void setType(String type) {
	    this.type = type;
	  }

	  public byte[] getData() {
	    return data;
	  }

	  public void setData(byte[] data) {
	    this.data = data;
	  }


}

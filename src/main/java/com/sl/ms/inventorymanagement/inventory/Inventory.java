package com.sl.ms.inventorymanagement.inventory;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sl.ms.inventorymanagement.product.Product;

@Entity(name = "SL_INV")
public class Inventory {
	
	@Id
	//@GeneratedValue
	private Long id;
	
	@Column(name = "DATE")
	private Date date;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,mappedBy="inventory",orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	private List<Product> product;
	
	protected Inventory() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}

	public Inventory(Long id, Date date) {
		super();
		this.id = id;
		this.date = date;
	}

	@Override
	public String toString() {
		return "Inventory [id=" + id + ", date=" + date + "]";
	}

}

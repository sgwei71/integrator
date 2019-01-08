package com.ibkglobal.integrator.test;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name="product")
public class Product {
	@XmlAttribute(name="id")
	private String productId;
	@XmlElement(name="description")
	private String description;
	@XmlElement(name="imageUrl")
	private String imageUrl;
	@XmlElement(name="price")
	private BigDecimal price;
	@XmlElement(name="createBy")
	private User createBy;
}

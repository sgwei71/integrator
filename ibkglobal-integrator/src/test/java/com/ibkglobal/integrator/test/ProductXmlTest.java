package com.ibkglobal.integrator.test;

import java.io.File;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProductXmlTest {
	private Product product;

	@Before
	public void setup() {
		long l = 10;
		Long longid = new Long(l);
		User  user = new User(longid, "sunggy","sunggyu@ibk.co.kr");
		product = new Product("PO1", "Sung Gyu Good", "https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg", new BigDecimal(19.95), user);
	}
	@After
	public void tearDown(){
		product = null;
	}
	
	@Test
	public void testObjectToXml() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Product.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(product, new File("product.xml"));
		marshaller.marshal(product,  System.err);
		
	}
}

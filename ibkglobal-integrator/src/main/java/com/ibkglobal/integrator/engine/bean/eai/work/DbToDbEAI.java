package com.ibkglobal.integrator.engine.bean.eai.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
//import org.apache.camel.component.sql.SqlEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.CamelConfig;

public class DbToDbEAI {
	
	@Autowired
	CamelConfig camelConfig;
	
	public void process(Exchange exchange) throws Exception {
		
		Message message = exchange.getIn();
		
//		SqlEndpoint sql = new SqlEndpoint();
		
		
	}
}

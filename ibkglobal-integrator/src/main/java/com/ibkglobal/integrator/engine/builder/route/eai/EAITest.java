package com.ibkglobal.integrator.engine.builder.route.eai;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EAITest extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("netty4:tcp://localhost:21522")
		.process(new Processor() {			
			@Override
			public void process(Exchange exchange) throws Exception {
				//System.out.println("수신받은 데이터 : " + exchange.getIn().getBody());
				System.out.println("수신받은 데이터 : ");
			}
		})
		.end();
	}
}

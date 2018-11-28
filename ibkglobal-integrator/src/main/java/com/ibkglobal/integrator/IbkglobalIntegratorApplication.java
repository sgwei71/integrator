package com.ibkglobal.integrator;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ibkglobal")
public class IbkglobalIntegratorApplication {
	@Autowired
	CamelContext context;
	
	public static void main(String[] args) {
		SpringApplication.run(IbkglobalIntegratorApplication.class, args);
	}
	
	
	@Bean
	RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {

			
				from("netty-http://127.0.0.1:9080/service/test").process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
					//	String eaiSample = "002100N0KO02420180903dcbkapa1ID1409027660161419000320180903APA11409023744120000021419172.18.190.71                          3440B5A8F876D20180903140902766N999EAI0000000020180903154230315R00000CBKLONCBKO00032643SN020180903154230292CRDOBG000009                                                                       00000        EAI2018090315423031590EAIEEAIEAI04001ITRBATLON                        20180903dcbkapa1ID14090276601600                      BATITR               800               N100000000000001000                                                                                                                                                                                                MC000124    000  0000        11        22                        0000100N          000-99999999999999-99999999999999-99999999999999 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                00000                                                                                                                MS0006590                              01EEAIEAI0400101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                        0101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                                                                                                                                                                                                                                00000000@@";
						//System.out.println("[netty]"+exchange.getIn().getBody(String.class));
						System.out.println("[netty]"+exchange.getIn().getBody().toString());
					
						byte[] output = exchange.getIn().getBody(String.class).getBytes("MS949");
						
							//byte[] sample = eaiSample.getBytes("MS949");
						exchange.getOut().setBody(output);
					//	exchange.getOut().setBody(sample);
						
					}
				});
				
				
				
			}
		};

	}
}

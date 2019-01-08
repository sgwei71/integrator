package com.ibkglobal.dummy;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DummyServer extends RouteBuilder{
	

	@Override
	public void configure() throws Exception {
		from("netty4-http:http://0.0.0.0:8082/echo?requestTimeout=1000&serverInitializerFactory=#ibkHttpServerInitializerFactory&nettyHttpBinding=#ibkNettyHttpBinding")
			.process(new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("#################DummyServer.Echo########################");
					String answer = "001454N0KO02420181213DESKTOP-re0144513800020000000020181213DESKTOP-A6U76ORre014451380192.16.56.1                            0A002700001BD20181213014451380N000GMC        20181213014451380S00000APBAPBITRO00060088SN020181213014451380CBKAPBP10001CBKAPBP10001                                                           00000                              APB            APBAPBAPB                       N20181213DESKTOP-A6U76ORre0144500                      OLTAPBCBKAPBP10001800                   100000000000001000    1   1                                               1                                                  1                             1    1                                                 MC000124         0000CHFEP00011        22                        0000000 9301         000000000000000000000000000000000000000000000 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                00000                                                                                                                IO0000132abcd20181213@@";
					answer = "001754N0KO02420181130TSSAPA01000000000005120000000320181130TSSAPA01000000000005120000172.18.190.81                          000000000000D20181130094723000N999MCA0101    20181130094729777R00001APBAPBAPBO00062642SN020181130094723000CBKAPBP10001                        TSSAPA01172.18.190.82                          05811dmca0101CBK2018113009472972700               APBAPBAPB                       N20181130TSSAPA010000000000051200                      OLTAPBCBKAPBP10001800800               N100000000000001000                                                                                                                                                                                                MC0001240817000  0000F15571  1                                   0000000 0817F15571000-99999999999999-99999999999999-99999999999999 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                00000                                                                                                                IO00031310000000000001002670영전왕(민)                                                                                          UZ HPVQJXCQTUR                                                                                      222222  KRKRNYNN                                       NN                                    @@";
					byte[] bytes = exchange.getIn().getBody(String.class).getBytes("MS949");
					System.out.println(new String(bytes,"MS949"));
					System.out.println(exchange.getIn().getBody(String.class));
					exchange.getOut().setBody(answer.getBytes("MS949"));

				}
			});
	}
}

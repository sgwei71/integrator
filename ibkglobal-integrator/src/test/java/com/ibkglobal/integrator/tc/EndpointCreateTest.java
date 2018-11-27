package com.ibkglobal.integrator.tc;

import org.springframework.stereotype.Component;

import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointType;
import com.ibkglobal.integrator.engine.builder.service.EndpointCreate;

@Component
public class EndpointCreateTest {
	
	// Consumer 및 Producer Endpoint 생성
	
	// 추후 FILE, FTP, DB등등의 기능 추가(테스트 클래스 생성 : FileToFileEAI / FileToFtpEAI / FtpToFileEAI / FtpToFtpEAI)
	
	// 파라미터 정보를 이용해서 Netty 서버 인스턴스 생성 시, 커스텀하게 기능을 추가
	// TCP Instance  : IBKTcpChannelInitializer
	// HTTP Instance : IBKHttpChannelInitializer
	
	// 로드 밸런싱(Fail Over) : RouteCreateDefault / createEndpoint 메소드
	
	public String endpointCreateDirect() throws Exception {
		EndpointInfo info = new EndpointInfo();
		
		info.setEndpointType(EndpointType.DIRECT);
		info.setEndpointDirect("TEST.DIRECT");
		
		return EndpointCreate.createDirect(info);
	}
	
	public String endpointCreateTcp(String type) throws Exception {
		EndpointInfo info = new EndpointInfo();
		
		info.setEndpointIp("localhost");	// IP
		info.setEndpointPort(76421);		// PORT
		info.setMaxSession(10);				// 최대 접속 수
		info.setCodecNm("codecTest");		// 다양한 코덱
		info.setSyncCd(true);				// 동기 비동기 설정
		info.setLengthLen(0);				// 바이트 길이 설정
		info.setLengthOffset(4);			// 시작점
		
		info.setEndpointType(EndpointType.TCP);
		
		return EndpointCreate.createTcp(type, info);
	}
	
	public String endpointCreateHttp(String type) throws Exception {
		EndpointInfo info = new EndpointInfo();
		
		info.setEndpointIp("localhost");	// IP
		info.setEndpointPort(33321);		// PORT
		info.setPathNm("/httpin");			// Path
		info.setMaxSession(10);				// 최대 접속 수
		
		info.setEndpointType(EndpointType.HTTP);
		
		return EndpointCreate.createHttp(type, info);
	}
	
	public String endpointCreateQueue() throws Exception {
		EndpointInfo info = new EndpointInfo();
		
		info.setEndpointQueue("TEST.QUEUE.Q");
		info.setMaxConcurrent(10);
		
		info.setEndpointType(EndpointType.QUEUE);
		
		return EndpointCreate.createQueue(info);
	}
}

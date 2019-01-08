package com.ibkglobal.integrator.engine.bean.rest.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.camel.Exchange;
import org.apache.camel.http.common.HttpBinding;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.ConstantCodeAPI;

public class SttlSender {

	private Logger logger = LoggerFactory.getLogger(SttlSender.class);
	
	public SttlSender() {
		// TODO Auto-generated constructor stub
	}
	
	public void sender(Exchange exchange) throws Exception {
		
		HttpURLConnection conn = null;
		
		try {
			String ip = exchange.getIn().getHeader("EndpointIp",String.class);
			String port=exchange.getIn().getHeader("EndpointPort",String.class);
			//String pathNm 추가 2018-12-18(김혁재)
			String pathNm = exchange.getIn().getHeader("EndpointPathNm",String.class);
			//URL url = new URL("http://"+ip+":"+port+"/");
			URL url = new URL("http://"+ip+":"+ port + pathNm);
			logger.info("TAGET URL = [{}]",url.toString());
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("content-type", "application/octet-stream");
			byte[] retMsg =exchange.getIn().getBody(byte[].class);
			OutputStream os = conn.getOutputStream();
			os.write(retMsg);

			
			byte[] bytes = ByteStreams.toByteArray(conn.getInputStream());

//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"MS949"));
//			StringBuffer answer = new StringBuffer();
//			String line;
//			while(	(line=bufferedReader.readLine()) !=null) {
//				answer.append(line);
//			}
			os.flush();
			os.close();
			exchange.getIn().setBody(bytes);
			exchange.getOut().setBody(bytes);
			logger.info("응답전문 ==> {}",new String(bytes,"MS949"));
			if (conn.getResponseCode() != 200) {
				logger.info("■ 서버 응답이 정상이 아닙니다. [" + conn.getResponseCode() + "]");
				exchange.getIn().setHeader(ConstantCodeAPI.HTTP_RESPONSE_CODE, conn.getResponseCode() );
				throw new Exception("서버응답이 정상이 아닙니다.[" + conn.getResponseCode() + "]");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
	}

}

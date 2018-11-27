package com.ibkglobal.integrator.util;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.camel.Message;

import com.ibkglobal.common.validator.sttl.SttlFieldUtil;
import com.ibkglobal.integrator.engine.model.HealthCheckInfo;
import com.ibkglobal.integrator.engine.model.HealthCheckInfo.HealthStatus;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.message.common.normal.StandardTelegram;

public class HealthCheckUtil {
	
	private static int HEADERLENGTH     = 8;
	private static int USERINFOLENGTH   = 6;
	private static int IPLENGTH         = 16;
	
	public  static String CONTEXTHEADER = "CamelNettyChannelHandlerContext";
	public  static String CHECKRESULT   = "CHECKRESULT";
	
	/**
	 * 정보 접속 시
	 * @param data
	 * @return
	 */
	public static HealthCheckInfo createInfo(Message message) throws CommonException {		
		byte[] data = message.getBody(byte[].class);
		
		HealthCheckInfo healthCheckInfo = new HealthCheckInfo();
		
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		byte[] header   = new byte[HEADERLENGTH];
		byte[] userinfo = new byte[USERINFOLENGTH];
		byte[] ip       = new byte[IPLENGTH];
		
		buffer.get(header);
		buffer.get(userinfo);
		buffer.get(ip);		
		
		int dataLength = Integer.parseInt((new String(header)));
		
		if (dataLength != USERINFOLENGTH + IPLENGTH) {
			return null;
		}
		
		healthCheckInfo.setLength(Integer.parseInt((new String(header))));
		healthCheckInfo.setUserInfo(new String(userinfo).trim());
		healthCheckInfo.setIp(new String(ip).trim());
		healthCheckInfo.setStatus(HealthStatus.NORMAL);
		
		healthCheckInfo.setHealthTime(currentTime());
		
		return healthCheckInfo;
	}
	
	public static long currentTime() {
		long currentTime = new Date().getTime() / 1000;
		
		return currentTime;
	}
	
	public static byte[] ackMessage() {
		//byte[] data = {0, 0, 0, 0, 0, 0, 0, 1, 0};
		String data = "000000010";
		
		return data.getBytes();
	}
	
	public static byte[] nakMessage() {
		//byte[] data = {0, 0, 0, 0, 0, 0, 0, 1, 1};
		String data = "000000011";
		
		return data.getBytes();
	}
	
	public static StandardTelegram logoutMessage() {
		StandardTelegram standardTelegram = SttlFieldUtil.defaultInit();
		
		standardTelegram.getSttlSysCopt().setWhbnSttlWrtnYmd("20180704");
		standardTelegram.getSttlSysCopt().setWhbnSttlCretSysNm("D0000001");
		standardTelegram.getSttlSysCopt().setWhbnSttlSrn("13204011100001");
		standardTelegram.getSttlSysCopt().setWhbnSttlPgrsDsncNo(0);
		standardTelegram.getSttlSysCopt().setWhbnSttlPgrsNo(1);
		standardTelegram.getSttlSysCopt().setSttlIp("10.104.162.82");
		standardTelegram.getSttlSysCopt().setRqstSysDcd("S");
		standardTelegram.getSttlSysCopt().setSttlIntfId("ITRO00000035");
		standardTelegram.getSttlSysCopt().setInptTmgtDcd("1");
		standardTelegram.getSttlSysCopt().setRqstRcvSvcId("GCBCOMLOGOUT");
		
		standardTelegram.getSttlTrnCopt().setSttlXcd("GCBCOMLOGOUT600");
		standardTelegram.getSttlTrnCopt().setSttlRqstFuncDsncId("600");
		standardTelegram.getSttlTrnCopt().setAhdIqtrYn("N");
		
		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		data.put("empNo", "F14858");
		data.put("secrtNo", "1234");
		
		standardTelegram.getUserData().setData(data);
		
		return standardTelegram;
	}
}

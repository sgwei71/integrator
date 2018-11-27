package com.ibkglobal.message.common.ifep;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlElement;

/**
 * IFEP 헤더
 *
 */
public class IFepCommon extends Common {

	@Min(7)
	@XmlElement(name="MSG_LEN", defaultValue="0")
	private int msgLen;
	
	@Min(4)
	@XmlElement(name="BIZ_CODE", defaultValue="")
	private String bizCode;
	
	@Min(4)
	@XmlElement(name="ORG_CODE", defaultValue="")
	private String orgCode;
	
	@Min(20)
	@XmlElement(name="MSG_CODE", defaultValue="")
	private String msgCode;
	
	@Min(20)
	@XmlElement(name="TX_CODE", defaultValue="")
	private String txCode;
	
	@Min(20)
	@XmlElement(name="ETC_CODE", defaultValue="")
	private String etcCode;
	
	@Min(30)
	@XmlElement(name="TRXQ_NO", defaultValue="")
	private String trxqNo;
	
	@Min(34)
	@XmlElement(name="GLOBAL_ID", defaultValue="")
	private String globalId;
	
	@Min(30)
	@XmlElement(name="SESSION_ID", defaultValue="")
	private String sessionId;
	
	@Min(31)
	@XmlElement(name="RESERVED_1", defaultValue="")
	private String reserved1;
	
	@Min(50)
	@XmlElement(name="RESERVED_2", defaultValue="")
	private String reserved2;
	
	/**
	 * new Instance.
	 * 
	 * @return
	 */
	public static IFepCommon getInstance() {
		return new IFepCommon();
	}
}

package com.ibkglobal.integrator.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.message.common.normal.DataSetCode;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt.ClotScreInfo;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt.PnmgInfo;

public class IBKMessageUtil {
	
	public static String getDynamicDestination(Interface intf) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(EndpointCode.DIRECT);
		sb.append(routingRuleCheck(intf.getInterfaceType().getTarget().getSystem().getCode()));
		sb.append(".");
		sb.append(routingRuleCheck("KR"));
		sb.append(".");
		sb.append("ROUTE");
		
		return sb.toString();
	}
	
	public static String routingRuleCheck(String data) {	
		
		String result = "";
		
		if (data.length() < 4) {
			result = String.format("%s%0" + (4-data.length()) + "d", data, 0);
		} else {
			result = data;
		}
		
		return result;
	}

	/**
	 * Get 전문 복원 키
	 * 
	 * @param exchange
	 * @param interfaceIds
	 * @return
	 * @throws IBKNormalMessageException
	 */
	public static String getRecvKey(Exchange exchange, String interfaceIds) throws IBKExceptionFEP {

		Message in = exchange.getIn();
		String biz_code = in.getHeader(ConstantCode.BIZ_CODE, String.class);
		String org_code = in.getHeader(ConstantCode.ORG_CODE, String.class);
		String msgKey = null; // 거래고유번호
		try {
			msgKey = in.getHeader(ConstantCode.TRXCQ2_NO, String.class);
		} catch (Exception e) {
			throw new IBKExceptionFEP(ErrorType.FEP_RECOVERY, "Can't find TRXCQ2_NO. (IBKNormalMessageUtil.getRecvKey)",
					e);
		}

		// 전문복원 key 변경: 업무_기관_인터페이스키_거래고유번호
		String[] interfaceIdArr = interfaceIds.split(";");
		StringBuilder recvKey = new StringBuilder();
		for (String interfaceId : interfaceIdArr) {
			if (recvKey.length() > 0)
				recvKey.append(";");
			recvKey.append(biz_code);
			recvKey.append("_");
			recvKey.append(org_code);
			recvKey.append("_");
			recvKey.append(interfaceId);
			recvKey.append("_");
			recvKey.append(msgKey);
		}
		return recvKey.toString();
	}

	/**
	 * Get GlobalId
	 * 
	 * @param standardTelegram
	 * @return
	 */
	public static String getGid(StandardTelegram standardTelegram) {
		String whbnSttlWrtnYmd = standardTelegram.getSttlSysCopt().getWhbnSttlWrtnYmd();
		String whbnSttlCretSysNm = standardTelegram.getSttlSysCopt().getWhbnSttlCretSysNm();
		String whbnSttlSrn = standardTelegram.getSttlSysCopt().getWhbnSttlSrn();

		String gid = whbnSttlWrtnYmd + whbnSttlCretSysNm + whbnSttlSrn;

		return gid;
	}

	/**
	 * Get InterfaceId
	 * 
	 * @param standardTelegram
	 * @return
	 */
	public static String getInterfaceId(StandardTelegram standardTelegram) {
		String interfaceId = standardTelegram.getSttlSysCopt().getSttlIntfId();

		return interfaceId;
	}

	/**
	 * Get ReqResFlag
	 * 
	 * @param standardTelegram
	 * @return
	 */
	public static String getReqResFlag(StandardTelegram standardTelegram) {
		String rqstRspnDcd = standardTelegram.getSttlSysCopt().getRqstRspnDcd();

		return rqstRspnDcd;
	}

	public static void replyMessageDefaultSet(StandardTelegram standardTelegram, String systemCode) {
		// 송신시스템구분코드
		standardTelegram.getSttlSysCopt().setTrnmSysDcd(systemCode);
	}

	public static void replyMessageDefaultSet(StandardTelegram standardTelegram, String systemCode, String sendRecv) {
		// 송신시스템구분코드
		standardTelegram.getSttlSysCopt().setTrnmSysDcd(systemCode);
		// 요청응답구분코드
		standardTelegram.getSttlSysCopt().setRqstRspnDcd(sendRecv);
	}

	public static void replyMessageBidDefaultSet(StandardTelegram standardTelegram, String systemCode, String sendRecv,
			String resultCode) {

		// UserData Null 응답
		standardTelegram.getUserData().setData(null);

		// 송신시스템구분코드
		standardTelegram.getSttlSysCopt().setTrnmSysDcd(systemCode);
		// 요청응답구분코드
		standardTelegram.getSttlSysCopt().setRqstRspnDcd(sendRecv);
		// 응답처리결과구분코드
		standardTelegram.getSttlSysCopt().setRspnPcrsDcd(resultCode);

		SttlMsgCopt sttlMsgCopt = new SttlMsgCopt();
		sttlMsgCopt.setDtstDcd(DataSetCode.MSG_COPT);
		sttlMsgCopt.setMsgRpsnDcd("0");

		// 주메시지정보(PNMG_INFO)
		List<PnmgInfo> pnmgInfoList = new ArrayList<>();

		PnmgInfo pnmgInfo = new PnmgInfo();
		pnmgInfo.setPnmgCd(standardTelegram.getSttlSysCopt().getSttlErcd());
		pnmgInfo.setPnmgDsncPgrsNo(1);
		pnmgInfo.setPnmgCon("정상처리되었습니다.");

		pnmgInfoList.add(pnmgInfo);

		sttlMsgCopt.setPnmgInfo(pnmgInfoList);

		// 표출화면정보(CLOT_SCRE_INFO)
		List<ClotScreInfo> clotScreInfoList = new ArrayList<>();

		ClotScreInfo clotScreInfo = new ClotScreInfo();
		clotScreInfo.setPnmgInkyVl(1);
		clotScreInfo.setClotScreNm("00000");
		clotScreInfo.setClotScreAdr("00000");

		clotScreInfoList.add(clotScreInfo);

		sttlMsgCopt.setClotScreInfo(clotScreInfoList);
	}
}

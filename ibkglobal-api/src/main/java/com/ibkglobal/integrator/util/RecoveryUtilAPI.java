package com.ibkglobal.integrator.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.common.validator.sttl.SttlFieldUtil;
import com.ibkglobal.common.validator.sttl.SttlFieldUtilAPI;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.DataSetCode;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;

public class RecoveryUtilAPI extends RecoveryUtil{
	
	public static String sysAPICode = "APB";
	public static StandardTelegram makeRequestNormalHeaderMCA(String ifid,String reqSvcID,String resSvcID,String resSvcIDErr, String errorCode,String normalTxCode)
	{		
		StandardTelegram ibkNormalHeader = SttlFieldUtilAPI.defaultAPI();
		
		try {
			SimpleDateFormat formatterYYYYMMDD          = new SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
			SimpleDateFormat formatterHHmmssSSS         =  new SimpleDateFormat ("HHmmssSSS", java.util.Locale.KOREA);
			SimpleDateFormat formatterYYYYMMDDHHmmssSSS = new SimpleDateFormat ("yyyyMMddHHmmssSSS", java.util.Locale.KOREA);
			Date currentTime = new Date();
			String dateYYYYMMDD          = formatterYYYYMMDD.format(currentTime);
			String dateHHmmssSSS         = formatterHHmmssSSS.format(currentTime);
			String dateYYYYMMDDHHmmssSSS = formatterYYYYMMDDHHmmssSSS.format(currentTime);
			
			////////////////////////////// 시스템 공통부 //////////////////////////////
			
			// 표준전문버전구분값
			// 고정인데... 개발, 테스트, 운영으로 변경된다.
			//String sysInfo = PropUtil.getPropertiesValue("SYS_ENV_DCD").substring(0, 1);
			String sysInfo = "D";
			//String sttlVer = PropUtil.getPropertiesValue("SYS_STTL_VER").substring(0, 3);
			String sttlVer = "024";
						
			// 표준전문 024 부터 변경 (D23 -> 024)
			ibkNormalHeader.getSttlSysCopt().setSttlVerDsnc(sttlVer);

			String gidDate = dateYYYYMMDD;
			String gidSysname = SystemUtil.getHostName();
			String gidSrn = SystemUtil.getWasInstanceID() + dateHHmmssSSS + SystemUtil.getUniqueGIDSubNum();
			
			// 표준전문작성년월일
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlWrtnYmd(gidDate);
			// 표준전문생성시스템명
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlCretSysNm(gidSysname);
			// 표준전문일련번호
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlSrn(gidSrn);
			
			// 표준전문인터페이스UNIQUEID
			ibkNormalHeader.getSttlSysCopt().setSttlIntfAnunId(gidDate + gidSysname + gidSrn + "0000");
			
			// 표준전문IP
			ibkNormalHeader.getSttlSysCopt().setSttlIp(SystemUtil.getIpAddr());
			// 표준전문MAC주소값
			ibkNormalHeader.getSttlSysCopt().setSttlMacAdrVl(SystemUtil.getMacAddr());
			
			// 시스템환경정보구분코드
			ibkNormalHeader.getSttlSysCopt().setSysEnvrInfoDcd(sysInfo);
			// 최초요청일시
			ibkNormalHeader.getSttlSysCopt().setFrstRqstTs(dateYYYYMMDDHHmmssSSS);
			
			// 송신시스템구분코드, 어댑터에서 셋?
			ibkNormalHeader.getSttlSysCopt().setTrnmSysDcd(sysAPICode);
			
			// 송신일시
			ibkNormalHeader.getSttlSysCopt().setTrnmTs(dateYYYYMMDDHHmmssSSS);
			
			// 요청시스템구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstSysDcd(sysAPICode);
			// 요청시스템업무구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstSysBswrDcd(sysAPICode);
			// 표준전문인터페이스ID
			ibkNormalHeader.getSttlSysCopt().setSttlIntfId(ifid);
			
			// 동기응답대기구분코드
//			if (!sync) {
//				ibkNormalHeader.getSttlSysCopt().setSyncRspnWaitDcd("K");
//			} else {
				ibkNormalHeader.getSttlSysCopt().setSyncRspnWaitDcd("S");
			//}
			
			// 표준전문요청일시
			ibkNormalHeader.getSttlSysCopt().setSttlRqstTs(dateYYYYMMDDHHmmssSSS);
			
			// 요청수신서비스ID
			if (reqSvcID != null) {
				ibkNormalHeader.getSttlSysCopt().setRqstRcvSvcId(reqSvcID);
			}
			else {
				ibkNormalHeader.getSttlSysCopt().setRqstRcvSvcId("");
			}
			
			// 응답수신서비스ID
			if (resSvcID != null) {
				ibkNormalHeader.getSttlSysCopt().setRspnRcvSvcId(reqSvcID);
			}
			else {
				ibkNormalHeader.getSttlSysCopt().setRspnRcvSvcId("");
			}
			
			// 오류응답서비스ID
			if (resSvcIDErr != null) {
				ibkNormalHeader.getSttlSysCopt().setErorRspnSvcId(resSvcIDErr);
			}
			else {
				ibkNormalHeader.getSttlSysCopt().setErorRspnSvcId("");
			}
			
			// 오류코드
			if (errorCode != null) {
				// 오류발생시스템구분코드
				ibkNormalHeader.getSttlSysCopt().setErocSysDcd(sysAPICode);
				// 표준전문오류코드
				// 오류코드에 해당하는 값 찾아서... (나중에...)
				ibkNormalHeader.getSttlSysCopt().setSttlErcd(errorCode);
			} else {
				// 오류발생시스템구분코드
				ibkNormalHeader.getSttlSysCopt().setErocSysDcd("");
				// 표준전문오류코드
				ibkNormalHeader.getSttlSysCopt().setSttlErcd("");
			}
			
			// 해당사항 없음
			// 요청채널유형구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstChptDcd(sysAPICode);
			// 요청채널유형세부구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstChptDtlsDcd(sysAPICode);
			// 요청채널업무구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstChbsDcd(sysAPICode);			
			// 대량입출력거래글로벌ID
			ibkNormalHeader.getSttlSysCopt().setLrqnInopTrnGlblId(gidDate + gidSysname + gidSrn);
			
			////////////////////////////// 거래 공통부 //////////////////////////////
			
			// 거래요청업무구분코드
			ibkNormalHeader.getSttlTrnCopt().setTrrqBswrDcd(sysAPICode);
			
			//표준전문거래코드
			if (normalTxCode != null) {
				ibkNormalHeader.getSttlTrnCopt().setSttlXcd(normalTxCode);
			} else if (ibkNormalHeader.getSttlSysCopt().getRqstRcvSvcId().trim().length() > 0) {
				ibkNormalHeader.getSttlTrnCopt().setSttlXcd(ibkNormalHeader.getSttlSysCopt().getRqstRcvSvcId() + "000");
			} else {
				ibkNormalHeader.getSttlTrnCopt().setSttlXcd("");
			}
			
			// 표준전문요청기능구분ID
		//	ibkNormalHeader.getSttlTrnCopt().setSttlRqstFuncDsncId(ibkNormalHeader.getSttlTrnCopt().getSttlXcd().substring(12));
			
			// 소속금융회사코드
			ibkNormalHeader.getSttlTrnCopt().setBlngFncmCd(intOrgCode);		
			
			// 대외거래업무구분코드
		//	ibkNormalHeader.getSttlTrnCopt().setExtTrnBswrDcd(bizCode);
			// 대외거래기관구분코드
		//	ibkNormalHeader.getSttlTrnCopt().setExtTrnInttDcd(orgCode);
			// 대외거래고유ID
			// 현재 에러로 막음
//			if (msgKeyValue != null) {
//				msgKeyValue = "";
//			}
//			
			// 대외거래세션ID
//			if (sessionID != null) {
//				ibkNormalHeader.getSttlTrnCopt().setExtTrnSsnId(sessionID);
//			} else {
//				ibkNormalHeader.getSttlTrnCopt().setExtTrnSsnId("");
//			}
//			
//			////////////////////////////// 대면 채널 공통부 //////////////////////////////
			
			// 데이터셋구분코드
			ibkNormalHeader.getSttlAmgcCopt().setDtstDcd(DataSetCode.AMGC_COPT);
			// 데이터셋길이
			ibkNormalHeader.getSttlAmgcCopt().setDtstLen((NORMAL_AMGC_AREA_LENGTH - NORMAL_DATASET_CODE_LENGTH - NORMAL_DATASET_LENGTH_LENGTH));
			
			////////////////////////////// 비대면 채널 공통부 //////////////////////////////
			
			// 데이터셋구분코드
			ibkNormalHeader.getSttlNfchCopt().setDtstDcd(DataSetCode.NFCH_COPT);
			// 데이터셋길이
			ibkNormalHeader.getSttlAmgcCopt().setDtstLen((NORMAL_NFCH_AREA_LENGTH - NORMAL_DATASET_CODE_LENGTH - NORMAL_DATASET_LENGTH_LENGTH));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ibkNormalHeader;
	}
}

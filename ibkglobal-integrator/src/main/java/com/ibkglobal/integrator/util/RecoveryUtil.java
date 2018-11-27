package com.ibkglobal.integrator.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.common.validator.sttl.SttlFieldUtil;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.DataSetCode;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;

public class RecoveryUtil {
	
	// FEP 시스템 구분 코드
	public static String sysFEPCode = "FEP";
		
	// FEP 업무 구분 코드
	public static String bizFEPCode = "FEP";
		
	// IBK 사내 금융사 코드 (IBK : 001)
	public static String intOrgCode = "001";
		
	// 표준전문 시스템 공통부 길이
	public static int NORMAL_SYS_AREA_LENGTH = 450;
		
	// 표준전문 거래 공통부 길이
	public static int NORMAL_BIZ_AREA_LENGTH = 250;
		
	// 표준전문 대면 채널 공통부 길이 (데이터셋구분코드/길이 포함)
	public static int NORMAL_AMGC_AREA_LENGTH = 132;
		
	// 표준전문 비대면 채널 공통부 길이 (데이터셋구분코드/길이 포함)
	public static int NORMAL_NFCH_AREA_LENGTH = 605;
		
	// 표준전문 헤더부 (시스템 + 거래) 길이
	public static int NORMAL_HEADER_AREA_LENGTH = NORMAL_SYS_AREA_LENGTH + NORMAL_BIZ_AREA_LENGTH;
		
	// 표준전문 고정 공통부 (시스템 + 거래 + 대면 + 비대면) 길이
	public static int NORMAL_FIX_AREA_LENGTH = NORMAL_SYS_AREA_LENGTH + NORMAL_BIZ_AREA_LENGTH
			+ NORMAL_AMGC_AREA_LENGTH + NORMAL_NFCH_AREA_LENGTH;
		
	// 표준전문 데이터셋 고정 코드 부분 길이
	public static int NORMAL_DATASET_CODE_LENGTH = 2;
		
	// 표준전문 데이터셋 고정 길이 부분 길이
	public static int NORMAL_DATASET_LENGTH_LENGTH = 6;
	// 길이 최대값 (이 부분을... 컴파일 단계에 미리 계산하는...
	public static int NORMAL_DATASET_MAX_LENGTH = 0;
	static {
		NORMAL_DATASET_MAX_LENGTH = (int) Math.pow(10, NORMAL_DATASET_LENGTH_LENGTH) - 1;
	}
		
	// 표준전문 데이터셋 개별부 코드
	public static String NORMAL_DATASET_IO_CODE = "IO";
		
	// 표준전문 데이터셋 대면 채널 공통부 코드
	public static String NORMAL_DATASET_AMGC_CODE = "MC";
		
	// 표준전문 데이터셋 비대면 채널 공통부 코드
	public static String NORMAL_DATASET_NFCH_CODE = "NC";
		
	// 표준전문 데이터셋 비대면 채널 공통부 코드
	public static String NORMAL_DATASET_DTST_CODE = "MS";
		
	// 표준전문 종료부 문자
	public static String NORMAL_END_AREA_CODE = "@@";
		
	// 표준전문 종료부 길이 (NORMAL_DATASET_CODE_LENGTH 와 같아야 함)
	public static int NORMAL_END_AREA_LENGTH = NORMAL_END_AREA_CODE.length();
		
	// 표준전문 길이필드 (STTL_LEN)
	public static int NORMAL_STTL_LEN_OFFSET = 0;
	public static int NORMAL_STTL_LEN_LENGTH = 6;
		
	// 표준전문 버전 구분값
	public static int NORMAL_STTL_VER_DSNC_OFFSET = 10;
	public static int NORMAL_STTL_VER_DSNC_LENGTH = 3;
		
	// 표준전문 Global ID
	public static int NORMAL_GID_WRTN_YMD_OFFSET = 13;
	public static int NORMAL_GID_WRTN_YMD_LENGTH = 8;
	public static int NORMAL_GID_CRET_SYS_NM_OFFSET = NORMAL_GID_WRTN_YMD_OFFSET + NORMAL_GID_WRTN_YMD_LENGTH;
	public static int NORMAL_GID_CRET_SYS_NM_LENGTH = 8;
	public static int NORMAL_GID_SRN_OFFSET = NORMAL_GID_CRET_SYS_NM_OFFSET + NORMAL_GID_CRET_SYS_NM_LENGTH;
	public static int NORMAL_GID_SRN_LENGTH = 14;
		
	// 표준전문 unique id
	public static int NORMAL_ANUN_ID_OFFSET = 51;
	public static int NORMAL_ANUN_ID_OFFSET_LENGTH = 34;
		
	// 표준전문 IP
	public static int NORMAL_STTL_IP_OFFSET = 85;
	public static int NORMAL_STTL_IP_LENGTH = 39;
		
	// 표준전문 시스템환경정보구분코드
	public static int NORMAL_SYS_EVNR_INFO_DCD_OFFSET = 136;
	public static int NORMAL_SYS_EVNR_INFO_DCD_LENGTH = 1;

	// 표준전문 요청/응답 FLAG
	public static int NORMAL_REQ_RES_CODE_OFFSET = 186;
	public static int NORMAL_REQ_RES_CODE_LENGTH = 1;
	
	// 요청시스템구분코드
	public static int NORMAL_RQST_SYS_DCD_OFFSET = 192;
	public static int NORMAL_RQST_SYS_DCD_LENGTH = 3;
	
	// 요청시스템업무구분코드
	public static int NORMAL_RQST_SYS_BSWR_DCD_OFFSET = 195;
	public static int NORMAL_RQST_SYS_BSWR_DCD_LENGTH = 3;
	
	// 표준전문 인터페이스 아이디
	public static int NORMAL_IF_OFFSET = 198;
	public static int NORMAL_IF_LENGTH = 12;
	
	// 요청수신서비스ID
	public static int NORMAL_RQST_RCV_SVC_ID_OFFSET = 230;
	public static int NORMAL_RQST_RCV_SVC_ID_LENGTH = 12;
	
	// 응답수신서비스ID
	public static int NORMAL_RSPN_RCV_SVC_ID_OFFSET = 242;
	public static int NORMAL_RSPN_RCV_SVC_ID_LENGTH = 12;
	
	// 오류응답서비스ID
	public static int NORMAL_ERR_RSPN_SVC_ID_OFFSET = 254;
	public static int NORMAL_ERR_RSPN_SVC_ID_LENGTH = 12;
	
	// 응답수신node ID
	public static int NORMAL_RCVNDID_OFFSET = 266;
	public static int NORMAL_RCVNDID_LENGTH = 8;
	
	// 응답처리결과구분코드
	public static int NORMAL_RSPN_PCRS_DCD_OFFSET = 346;
	public static int NORMAL_RSPN_PCRS_DCD_LENGTH = 1;
	
	// 출력전문유형구분코드
	public static int NORMAL_OTPT_TMGT_OFFSET = 347;
	public static int NORMAL_OTPT_TMGT_LENGTH = 1;
	
	// 오류발생시스템구분코드
	public static int NORMAL_EROC_SYS_DCD_OFFSET = 348;
	public static int NORMAL_EROC_SYS_DCD_LENGTH = 3;
	
	// 표준전문오류코드
	public static int NORMAL_STTL_ERCD_OFFSET = 351;
	public static int NORMAL_STTL_ERCD_LENGTH = 12;
	
	// 표준전문거래코드
	public static int NORMAL_STTL_XCD_OFFSET = NORMAL_SYS_AREA_LENGTH + 6;
	public static int NORMAL_STTL_XCD_LENGTH = 15;
	
	// 표준전문요청기능구분ID
	public static int NORMAL_STTL_RQST_FUNC_DSNC_OFFSET = NORMAL_SYS_AREA_LENGTH + 21;
	public static int NORMAL_STTL_RQST_FUNC_DSNC_LENGTH = 3;
	
	// 표준전문응답기능구분ID
	public static int NORMAL_STTL_RSPN_FUNC_DSNC_OFFSET = NORMAL_SYS_AREA_LENGTH + 24;
	public static int NORMAL_STTL_RSPN_FUNC_DSNC_LENGTH = 3;
	
	// 표준전문오류기능구분ID
	public static int NORMAL_STTL_EROR_FUNC_DSNC_OFFSET = NORMAL_SYS_AREA_LENGTH + 27;
	public static int NORMAL_STTL_EROR_FUNC_DSNC_LENGTH = 3;
	
	// 대외거래공통필드 - 대외업무구분
	public static int NORMAL_EXT_BIZCODE_OFFSET = NORMAL_SYS_AREA_LENGTH + 58;
	public static int NORMAL_EXT_BIZCODE_LENGTH = 4;
	
	// 대외거래공통필드 - 대외기관코드
	public static int NORMAL_EXT_ORGCODE_OFFSET = NORMAL_SYS_AREA_LENGTH + 62;
	public static int NORMAL_EXT_ORGCODE_LENGTH = 4;
	
	// 대외거래공통필드 - 대외거래고유ID
	public static int NORMAL_BIZKEY_OFFSET = NORMAL_SYS_AREA_LENGTH + 66;
	public static int NORMAL_BIZKEY_LENGTH = 48;
	
	// 대외연계인터페이스ID
	public static int NORMAL_EXT_LNK_INTF_ID_OFFSET = NORMAL_SYS_AREA_LENGTH + 114;
	public static int NORMAL_EXT_LNK_INTF_ID_LENGTH = 51;
	
	// 대외거래세션ID
	public static int NORMAL_EXT_TRN_SSN_ID_OFFSET = NORMAL_SYS_AREA_LENGTH + 165;
	public static int NORMAL_EXT_TRN_SSN_ID_LENGTH = 30;
	
	// Error Code
	// System Error
	public static String NORMAL_ERROR_SYSTEM = "9";
	// TIMEOUT
	public static String NORMAL_ERROR_TIMEOUT = "8";
	// 표준전문 공통부 복원 실패
	public static String NORMAL_ERROR_RECOVERY_FAIL = "4";
	
	// 메시지 공통부를 위해
	// 현재는 주메시지정보만 사용하고 주메시지내용도 200 bytes 로 제한
	public static int NORMAL_MSPART_LENGTH = 265;
	public static int NORMAL_MSPART_EROC_FLD_NM_LENGTH = 30;
	public static int NORMAL_MSPART_PNMG_CD_OFFSET = 41;
	public static int NORMAL_MSPART_PNMG_CD_LENGTH = 12;
	public static int NORMAL_MSPART_PNMG_CON_OFFSET = 55;
	public static int NORMAL_MSPART_PNMG_CON_LENGTH = 200;
	public static byte[] NORMAL_MSPART_DEFAULT = (
			NORMAL_DATASET_DTST_CODE
			+ String.format("%0" + NORMAL_DATASET_LENGTH_LENGTH + "d", NORMAL_MSPART_LENGTH)
			+ "0" + String.format("%1$-" + NORMAL_MSPART_EROC_FLD_NM_LENGTH + "s", "")
			+ "01" + String.format("%1$-" + NORMAL_MSPART_PNMG_CD_LENGTH + "s", "") + "01"
			+ String.format("%1$-" + NORMAL_MSPART_PNMG_CON_LENGTH + "s", "")
			+ "00" + "00" + "00" + "00" + "00"
			).getBytes();
	
	/**
	 * 표준전문 공통부 응답헤더 셋 (개별부 데이터 길이로 전체 길이까지 셋)
	 * 
	 * @param normalMsg		표준전문 공통부 데이터
	 * @param bizCode		대외업무코드
	 * @param orgCode		대외기관코드
	 * @param msgKeyValue	전문고유키값
	 * @param len			개별부 길이
	 * @return
	 */
	public static void makeResponseNormalHeader(StandardTelegram ibkNormalHeader, String ifid, String reqSvcID, String resSvcID, String resSvcIDErr,
			String normalTxCode, String bizCode, String orgCode, String msgKeyValue, String refIfId, int abnormalMsgLen) {
		
		try {
			// 길이 = 공통부길이 normalMsg.length
			//        + 데이터셋코드필드 2 + 데이터셋길이필드 6 + 데이터셋길이 abnormalMsg.length
			//        + 종료부 2
			// 길이필드값 = 전체길이 - 공통부길이필드 6
			int abMsgAreaLen = abnormalMsgLen;
			
			// 길이 세팅 관련
			// --- 여기서부터 ---
//			if (abnormalMsgLen > 0) {
//				abMsgAreaLen += IBKNormalMessageUtil.NORMAL_DATASET_CODE_LENGTH +
//						IBKNormalMessageUtil.NORMAL_DATASET_LENGTH_LENGTH;
//			}
//			
//			// 표준전문 길이가 최대길이보다 크면 최대길이로 셋팅 (퇴직연금 등에서 사용)
//			int sttlPrintLength = normalMsg.length + abMsgAreaLen + IBKNormalMessageUtil.NORMAL_END_AREA_LENGTH -
//					IBKNormalMessageUtil.NORMAL_STTL_LEN_LENGTH;
//			if (sttlPrintLength > NORMAL_DATASET_MAX_LENGTH) {
//				sttlPrintLength = NORMAL_DATASET_MAX_LENGTH;
//			}
//			
//			byte[] sttl_len = String.format("%0" + IBKNormalMessageUtil.NORMAL_STTL_LEN_LENGTH + "d",
//					sttlPrintLength).getBytes();
//			System.arraycopy(sttl_len, 0, normalMsg,
//					IBKNormalMessageUtil.NORMAL_STTL_LEN_OFFSET, IBKNormalMessageUtil.NORMAL_STTL_LEN_LENGTH);
			// --- 여기까지 ---
			makeResponseNormalHeader(ibkNormalHeader, ifid, reqSvcID, resSvcID, resSvcIDErr, normalTxCode, bizCode, orgCode, msgKeyValue, refIfId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void makeResponseNormalHeader (StandardTelegram ibkNormalHeader, String ifid, String reqSvcID, String resSvcID,
			String resSvcIDErr, String normalTxCode, String bizCode, String orgCode, String msgKeyValue, String ifidRef) throws Exception {
		
		try {
			SimpleDateFormat formatter =
		            new SimpleDateFormat ("yyyyMMddHHmmssSSS", java.util.Locale.KOREA);
			Date currentTime = new Date();
			
			// 표준전문압축여부
			ibkNormalHeader.getSttlSysCopt().setSttlCmrsYn("N");
			
			// 표준전문암호화구분코드
			ibkNormalHeader.getSttlSysCopt().setSttlEncpDcd("0");
			
			// 표준전문진행번호
			int sttlPgrsNo = ibkNormalHeader.getSttlSysCopt().getWhbnSttlPgrsNo();
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlPgrsNo(sttlPgrsNo + 1);
			
			// 송신시스템구분코드
			ibkNormalHeader.getSttlSysCopt().setTrnmSysDcd(sysFEPCode);
			
			// 송신노드ID
			ibkNormalHeader.getSttlSysCopt().setTrnmNdId("");
			
			// 송신일시
			ibkNormalHeader.getSttlSysCopt().setTrnmTs(formatter.format(currentTime));
			
			// 요청응답구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstRspnDcd("R");
			
			// 표준전문개별부버전일련번호
			ibkNormalHeader.getSttlSysCopt().setSttlIdprVrsnSrn(00000);
			
			// 인터페이스ID
			if (ifid != null) {
				ibkNormalHeader.getSttlSysCopt().setSttlIntfId(ifid);
			}
			
			// 응답수신서비스ID
			if (resSvcID != null) {
				ibkNormalHeader.getSttlSysCopt().setErorRspnSvcId(resSvcIDErr);
			}
			
			// 표준전문거래코드 응답서비스 ID 셋팅
			// 일단 임시로 응답기능구분ID가 있으면 셋한다.
			String repFuncID = ibkNormalHeader.getSttlTrnCopt().getSttlRspnFuncDsncId();
			if (repFuncID != null && repFuncID.length() > 0) {
				ibkNormalHeader.getSttlSysCopt().setRspnRcvSvcId(repFuncID);
			}
			
			// 오류응답서비스ID
			if (resSvcIDErr != null && resSvcIDErr.length() > 0) {
				ibkNormalHeader.getSttlSysCopt().setErorRspnSvcId(resSvcIDErr);
			}
			
			// 응답시스템구분코드
			ibkNormalHeader.getSttlSysCopt().setRspnSysDcd(sysFEPCode);
			
			// 표준전문응답일시
			ibkNormalHeader.getSttlSysCopt().setSttlRspnTs(formatter.format(currentTime));
			
			// 처리결과 구분코드
			ibkNormalHeader.getSttlSysCopt().setRspnPcrsDcd("0");
			
			// 출력전문유형구분코드
			ibkNormalHeader.getSttlSysCopt().setOtptTmgtDcd("0");
			
			// 오류발생시스템구분코드
			ibkNormalHeader.getSttlSysCopt().setErocSysDcd("");
			
			// 표준전문오류코드
			ibkNormalHeader.getSttlSysCopt().setSttlErcd("");
			
			// 대량입출력연속번호
			ibkNormalHeader.getSttlSysCopt().setLrqnInopCtntNo(00);
			
			// 표준전문거래코드
			if (normalTxCode != null) {
				ibkNormalHeader.getSttlTrnCopt().setSttlXcd(normalTxCode);
			} else {
				// 표준전문거래코드 기능구분 ID 셋팅
				// 일단 임시로 응답기능구분ID 가 있으면 셋한다.
				if (repFuncID != null && repFuncID.length() > 0) {
					ibkNormalHeader.getSttlTrnCopt().setSttlRspnFuncDsncId(repFuncID);
				}
			}
			
			// 연속거래구분코드
			ibkNormalHeader.getSttlTrnCopt().setCtntTrnDcd(0);
			
		} catch (Throwable t) {
			throw new IBKExceptionFEP(ErrorType.FEP_RECOVERY, t);
		}
	}
	
	public static StandardTelegram makeIBKNormalHeaderFor2ndRecovery(Interface intf, String bizCode, String orgCode,
			String msgKeyValue, String ifidRef) {
		
		StandardTelegram ibkNormalHeader = null;
		
		try {
			ibkNormalHeader = makeRequestNormalHeader(intf, bizCode, orgCode, msgKeyValue);
			
			// 개별부 유니크 아이디 셋팅
			if (msgKeyValue != null && msgKeyValue.length() > 0) {
				ibkNormalHeader.getSttlTrnCopt().setExtTrnUnqId(msgKeyValue);
			}
					
			// 상위 인터페이스 아이디들 셋팅
			if (ifidRef != null && ifidRef.length() > 0) {
				ibkNormalHeader.getSttlTrnCopt().setExtLnkIntfId(ifidRef);
			}
			
			// 응답 셋팅
			ibkNormalHeader.getSttlSysCopt().setRqstRspnDcd("R");
			
			// 응답 서비스 아이디 셋팅(디폴트에 이미 셋팅되어있으므로 딱히 안해도 될듯?
			// 타발 전문 생성 시 MMS 내용이 요청 서비스 아이디에 셋팅
//			ibkNormalHeader.getSttlSysCopt().setRspnRcvSvcId("");
//			ibkNormalHeader.getSttlSysCopt().setRqstRcvSvcId("");
//			ibkNormalHeader.getSttlTrnCopt().setSttlRspnFuncDsncId("");
//			ibkNormalHeader.getSttlTrnCopt().setSttlRqstFuncDsncId("");
			
		} catch (Exception e) {
			// 표준전문 공통부 2차 복원을 위한 셋팅 실패
			
//			if (e instanceof IBKNormalMessageException) {
//				throw e;
//			} else {
//				throw new IBKNormalMessageException("표준전문 공통부 2차 복원을 위한 셋팅 실패",
//						e);
//			}
		}
		
		return ibkNormalHeader;
	}
	
	// 타발 - 공통부 생성
	public static StandardTelegram makeRequestNormalHeader(Interface intf, String bizCode, String orgCode, String msgKeyValue) {
		StandardTelegram ibkNormalHeader = null;
					
		boolean sync    = Boolean.FALSE;
		String reqSvcID = null;
		
		try {
			reqSvcID = intf.getCommon().getAttribute().getOnline().getExternal().getSvcId();
		} catch (Exception e) {}
		String resSvcID     = null;
		String resSvcIDErr  = null;
		String normalTxCode = null;
		String nTxCode      = null;
		
		try {
			nTxCode = intf.getCommon().getAttribute().getOnline().getExternal().getExtTrnDcdCon();
		} catch (Exception e) {}
		
		if (reqSvcID != null && reqSvcID.trim().length() > 0) {
			if (nTxCode != null && nTxCode.trim().length() > 0) {
				normalTxCode = reqSvcID + nTxCode;
			} else {
				normalTxCode = reqSvcID + "000";
			}
		}
		
		String errorCode = null;
		String sessionID = null;
		ibkNormalHeader  = makeRequestNormalHeader(bizCode, orgCode, msgKeyValue,
				intf.getIntfId(), sync, reqSvcID, resSvcID, resSvcIDErr, normalTxCode, errorCode, sessionID);
			
		return ibkNormalHeader;
	}
	
	/**
	 * 표준전문 공통부 요청헤더 셋 (공통부 길이필드는 제외)
	 * 
	 * @param bizCode
	 * @param orgCode
	 * @param msgKeyValue
	 * @param ifid
	 * @param sync
	 * @param reqSvcID
	 * @param resSvcID
	 * @param normalTxCode
	 * @param errorCode
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	public static StandardTelegram makeRequestNormalHeader(String bizCode, String orgCode, String msgKeyValue,
			String ifid, boolean sync, String reqSvcID, String resSvcID, String resSvcIDErr,
			String normalTxCode, String errorCode, String sessionID) {
		
		StandardTelegram ibkNormalHeader = SttlFieldUtil.defaultFep();
		
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
			String sttlVer = "001";
						
			// 표준전문 024 부터 변경 (D23 -> 024)
			ibkNormalHeader.getSttlSysCopt().setSttlVerDsnc(sttlVer);

			String gidDate = dateYYYYMMDD;
			//String gidSysname = SystemUtil.getHostName();
			String gidSysname = "dfepapa1";
			String gidSrn = SystemUtil.getWasInstanceID() + dateHHmmssSSS + SystemUtil.getUniqueGIDSubNum();
			
			// 표준전문작성년월일
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlWrtnYmd(gidDate);
			// 표준전문생성시스템명
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlCretSysNm(gidSysname);
			// 표준전문일련번호
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlSrn(gidSrn);
			// 표준전문진행구분번호
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlPgrsDsncNo(0);
			// 전행표준전문진행번호
			ibkNormalHeader.getSttlSysCopt().setWhbnSttlPgrsNo(0);
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
			ibkNormalHeader.getSttlSysCopt().setTrnmSysDcd(sysFEPCode);
			
			// 송신일시
			ibkNormalHeader.getSttlSysCopt().setTrnmTs(dateYYYYMMDDHHmmssSSS);
			
			// 요청시스템구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstSysDcd(sysFEPCode);
			// 요청시스템업무구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstSysBswrDcd(sysFEPCode);
			// 표준전문인터페이스ID
			ibkNormalHeader.getSttlSysCopt().setSttlIntfId(ifid);
			
			// 동기응답대기구분코드
			if (!sync) {
				ibkNormalHeader.getSttlSysCopt().setSyncRspnWaitDcd("K");
			} else {
				ibkNormalHeader.getSttlSysCopt().setSyncRspnWaitDcd("S");
			}
			
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
				ibkNormalHeader.getSttlSysCopt().setErocSysDcd(sysFEPCode);
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
			ibkNormalHeader.getSttlSysCopt().setRqstChptDcd(sysFEPCode);
			// 요청채널유형세부구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstChptDtlsDcd(sysFEPCode);
			// 요청채널업무구분코드
			ibkNormalHeader.getSttlSysCopt().setRqstChbsDcd(sysFEPCode);			
			// 대량입출력거래글로벌ID
			ibkNormalHeader.getSttlSysCopt().setLrqnInopTrnGlblId(gidDate + gidSysname + gidSrn);
			
			////////////////////////////// 거래 공통부 //////////////////////////////
			
			// 거래요청업무구분코드
			ibkNormalHeader.getSttlTrnCopt().setTrrqBswrDcd(bizFEPCode);
			
			//표준전문거래코드
			if (normalTxCode != null) {
				ibkNormalHeader.getSttlTrnCopt().setSttlXcd(normalTxCode);
			} else if (ibkNormalHeader.getSttlSysCopt().getRqstRcvSvcId().trim().length() > 0) {
				ibkNormalHeader.getSttlTrnCopt().setSttlXcd(ibkNormalHeader.getSttlSysCopt().getRqstRcvSvcId() + "000");
			} else {
				ibkNormalHeader.getSttlTrnCopt().setSttlXcd("");
			}
			
			// 표준전문요청기능구분ID
			ibkNormalHeader.getSttlTrnCopt().setSttlRqstFuncDsncId(ibkNormalHeader.getSttlTrnCopt().getSttlXcd().substring(12));
			
			// 소속금융회사코드
			ibkNormalHeader.getSttlTrnCopt().setBlngFncmCd(intOrgCode);			
			// 대외거래업무구분코드
			ibkNormalHeader.getSttlTrnCopt().setExtTrnBswrDcd(bizCode);
			// 대외거래기관구분코드
			ibkNormalHeader.getSttlTrnCopt().setExtTrnInttDcd(orgCode);
			// 대외거래고유ID
			// 현재 에러로 막음
			if (msgKeyValue != null) {
				msgKeyValue = "";
			}
			
			// 대외거래세션ID
			if (sessionID != null) {
				ibkNormalHeader.getSttlTrnCopt().setExtTrnSsnId(sessionID);
			} else {
				ibkNormalHeader.getSttlTrnCopt().setExtTrnSsnId("");
			}
			
			////////////////////////////// 대면 채널 공통부 //////////////////////////////
			
			// 데이터셋구분코드
			ibkNormalHeader.getSttlAmgcCopt().setDtstDcd(DataSetCode.AMGC_COPT);
			// 데이터셋길이
			ibkNormalHeader.getSttlAmgcCopt().setDtstLen((NORMAL_AMGC_AREA_LENGTH - NORMAL_DATASET_CODE_LENGTH - NORMAL_DATASET_LENGTH_LENGTH));
			// 단말설치부점코드
			ibkNormalHeader.getSttlAmgcCopt().setTrmnInltBrcd("    ");
			// 단발번호
			ibkNormalHeader.getSttlAmgcCopt().setTmn("   ");
			// 신분증스캔일련번호
			ibkNormalHeader.getSttlAmgcCopt().setIdcrScanSrn("  ");
			// 텔러번호
			ibkNormalHeader.getSttlAmgcCopt().setTln("CHFEP000");
			// 계정갱신모드구분코드
			ibkNormalHeader.getSttlAmgcCopt().setAcitRnlModeDcd("1");			
			// 마감전후구분코드
			ibkNormalHeader.getSttlAmgcCopt().setBaclDcd("1");
			// 기산년월일
			ibkNormalHeader.getSttlAmgcCopt().setRcknYmd("        ");
			// 유통무통구분코드
			ibkNormalHeader.getSttlAmgcCopt().setYnbkDcd("2");
			// 현금대체구분코드
			ibkNormalHeader.getSttlAmgcCopt().setCaalDcd("2");
			// 출력용서비스ID
			ibkNormalHeader.getSttlAmgcCopt().setPrusSvcId("            ");
			// 출력용인터페이스ID
			ibkNormalHeader.getSttlAmgcCopt().setPrusIntfId("            ");
			// 통장일련번호
			ibkNormalHeader.getSttlAmgcCopt().setBnkbSrn(0);
			// 스마트조회일련번호
			ibkNormalHeader.getSttlAmgcCopt().setSmatInqSrn(0);
			// IC칩매체종류구분코드
			ibkNormalHeader.getSttlAmgcCopt().setIcChipMdiaKindDcd(" ");			
			// 부점코드
			ibkNormalHeader.getSttlAmgcCopt().setBrcd("    ");
			// 조작자직원번호
			ibkNormalHeader.getSttlAmgcCopt().setOptoEmn(" ");
			// 책임자승인구분코드
			ibkNormalHeader.getSttlAmgcCopt().setSvatDcd(" ");
			// 조작자승인구분코드
			ibkNormalHeader.getSttlAmgcCopt().setOpatDcd(" ");
			// 전달메시지승인구분코드
			ibkNormalHeader.getSttlAmgcCopt().setTrmgAthzDcd(" ");
			// 원화현금잔액
			ibkNormalHeader.getSttlAmgcCopt().setWonAltrDfamAmt(null);
			// 타점수납금액
			ibkNormalHeader.getSttlAmgcCopt().setObrcAmt(null);
			// 원화대체차액금액
			ibkNormalHeader.getSttlAmgcCopt().setWonAltrDfamAmt(null);
			// 외화대체불일치여부
			ibkNormalHeader.getSttlAmgcCopt().setFrctfDscrYn(" ");
			
			////////////////////////////// 비대면 채널 공통부 //////////////////////////////
			
			// 데이터셋구분코드
			ibkNormalHeader.getSttlNfchCopt().setDtstDcd(DataSetCode.NFCH_COPT);
			// 데이터셋길이
			ibkNormalHeader.getSttlNfchCopt().setDtstLen((NORMAL_NFCH_AREA_LENGTH - NORMAL_DATASET_CODE_LENGTH - NORMAL_DATASET_LENGTH_LENGTH));
			// 이용자식별번호
			ibkNormalHeader.getSttlNfchCopt().setUsrIdntNo("             ");
			// 하위이용자번호
			ibkNormalHeader.getSttlNfchCopt().setLrrnUsrNo("             ");
			// 전자금융고객ID
			ibkNormalHeader.getSttlNfchCopt().setEbkCusId("                    ");		
			// 이용자접속기기IP
			ibkNormalHeader.getSttlNfchCopt().setUsrCctnMctlIp("                                       ");			
			// 이용자접속MAC값
			ibkNormalHeader.getSttlNfchCopt().setUsrCctnMacVl("                 ");
			// 이용자접속전화번호
			ibkNormalHeader.getSttlNfchCopt().setUsrCctnTpn("                    ");
			// 이용자접속기기ID
			ibkNormalHeader.getSttlNfchCopt().setUsrCctnMctlId("                    ");
			// 이용자계좌카드번호
			ibkNormalHeader.getSttlNfchCopt().setUsrAcntCdn("                ");
			// 공인인증서고유식별번호
			ibkNormalHeader.getSttlNfchCopt().setAtctCqrcgNo("                         ");
			// 비밀번호검증구분코드
			ibkNormalHeader.getSttlNfchCopt().setPwdVrfcDcd(" ");
			// 비밀번호암호화여부
			ibkNormalHeader.getSttlNfchCopt().setPwdEncpYn(" ");
			// 보안매체구분코드
			ibkNormalHeader.getSttlNfchCopt().setSecuMdiaDcd(" ");
			// 보안매체번호
			ibkNormalHeader.getSttlNfchCopt().setSecuMdiaNo("            ");
			// 보안카드순번1
			ibkNormalHeader.getSttlNfchCopt().setSccdSqn1("   ");
			// 보안카드비밀번호1
			ibkNormalHeader.getSttlNfchCopt().setSccdPwd1("    ");
			// 보안카드순번2
			ibkNormalHeader.getSttlNfchCopt().setSccdSqn2("   ");
			// 보안카드비밀번호2
			ibkNormalHeader.getSttlNfchCopt().setSccdPwd2("    ");
			// 이체비밀번호
			ibkNormalHeader.getSttlNfchCopt().setTranPwd(" ");
			// 계좌비밀번호
			ibkNormalHeader.getSttlNfchCopt().setAcntPwd(" ");
			// OTP검증메시지코드
			ibkNormalHeader.getSttlNfchCopt().setOtpVrfcMsgcd("            ");
			// 부점코드
			ibkNormalHeader.getSttlNfchCopt().setBrcd("    ");
			// 텔러번호
			ibkNormalHeader.getSttlNfchCopt().setTln("        ");
			// 서버출력요청건수
			ibkNormalHeader.getSttlNfchCopt().setSrvrOtptRqstNbi(0);
			// 전문메시지코드
			ibkNormalHeader.getSttlNfchCopt().setTlgrMsgcd("       ");
			// 업체구분코드
			ibkNormalHeader.getSttlNfchCopt().setEntpDcd("     ");
			// 서버데이터전달부내용
			ibkNormalHeader.getSttlNfchCopt().setSrvrDataDlprCon(" ");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ibkNormalHeader;
	}
	
	public StandardTelegram makeRequestNormalHeaderDefault() {
		StandardTelegram standardTelegram = new StandardTelegram();
		
		return standardTelegram;
	}
	
	// StandardTelegram + UserData
	public static IBKMessage makeNormalMsg(StandardTelegram standardTelegram, IBKMessage ibkMessage) {
		
		// 헤더부 + 개별부 결합
		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		
		data.putAll(ibkMessage.getHeader());
		data.putAll(ibkMessage.getBody());
		
		UserData userData = new UserData();
		userData.setData(data);
		
		standardTelegram.setUserData(userData);
		
		// 최종 결합 데이터
		ibkMessage.setStandardTelegram(standardTelegram);
		
		return ibkMessage;
	}
}

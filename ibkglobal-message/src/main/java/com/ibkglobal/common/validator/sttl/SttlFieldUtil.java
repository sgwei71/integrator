package com.ibkglobal.common.validator.sttl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.common.normal.copt.SttlAmgcCopt;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt;
import com.ibkglobal.message.common.normal.copt.SttlNfchCopt;
import com.ibkglobal.message.common.normal.copt.SttlOpatCopt;
import com.ibkglobal.message.common.normal.copt.SttlOtfxCopt;
import com.ibkglobal.message.common.normal.copt.SttlSvatCopt;
import com.ibkglobal.message.common.normal.copt.SttlSysCopt;
import com.ibkglobal.message.common.normal.copt.SttlTrnCopt;

public class SttlFieldUtil {
	
	// 시스템 공통부 Valid 기본 값
	public static String[] sttlCmrsYn        = {"Y", "N"};     
	public static String[] sttlEncpDcd       = {"0", "1", "2", "3"};
	public static String[] sttlLnggDcd       = {"KR", "EN", "ZH", "JA", "VI", "NE", "RU", "ID"};
	public static String   sttlVerDsnc       = "001";
	public static String[] whbnSttlCretSysNm = {"D", "T", "P", "L", "A"};
	public static String[] sysEnvrInfoDcd    = {"D", "T", "P", "L", "A"};
	public static String[] sttlMctmUseYn     = {"Y", "N"};
	public static String[] rqstRspnDcd       = {"S", "R", "K"};
	public static String[] rqstSysDcd        = {"MCA", "EAI", "FEP"};
	public static String[] syncRspnWaitDcd   = {"S", "A", "K"};
	public static String[] etahTrnYn         = {"Y", "N"};
	public static String[] inptTmgtDcd       = {"0", "1", "2", "3", "8", "9"};
	public static String[] rspnPcrsDcd       = {"0", "1", "8", "9"};
	public static String[] otptTmgtDcd       = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	public static String[] ssoUseYn          = {"Y", "N"};
	
	// 거래 공통부 Valid 기본 값
	public static String[] trnChnlDcd        = {"OLT", "CTC", "BAT", "SAF"};
	public static String[] ctntTrnDcd        = {"0", "1", "2", "3"};
	
	/**
	 * 전문 Default 값 설정(필요 부분만 생성)
	 * @return
	 */
	public static StandardTelegram defaultBaseInit() {
		
		StandardTelegram standardTelegram = new StandardTelegram();
		
		standardTelegram.setSttlSysCopt(new SttlSysCopt());   // 시스템 공통부
		standardTelegram.setSttlTrnCopt(new SttlTrnCopt());   // 거래 공통부
		standardTelegram.setUserData(new UserData());         // 개별부
		
		return standardTelegram;
	}
	
	/**
	 * 전문 Default 값 설정(전체 생성)
	 * @return
	 */
	public static StandardTelegram defaultInit() {
		
		StandardTelegram standardTelegram = new StandardTelegram();
		
		standardTelegram.setSttlSysCopt(new SttlSysCopt());   // 시스템 공통부
		standardTelegram.setSttlTrnCopt(new SttlTrnCopt());   // 거래 공통부
		standardTelegram.setSttlAmgcCopt(new SttlAmgcCopt()); // 대면채널 공통부
		standardTelegram.setSttlNfchCopt(new SttlNfchCopt()); // 비대면채널 공통부
		standardTelegram.setSttlSvatCopt(new SttlSvatCopt()); // 책임자승인 공통부
		standardTelegram.setSttlMsgCopt(new SttlMsgCopt());   // 메시지 공통부
		standardTelegram.setSttlOpatCopt(new SttlOpatCopt()); // 조작자승인 공통부
		standardTelegram.setSttlOtfxCopt(new SttlOtfxCopt()); // 출력양식 공통부
		standardTelegram.setUserData(new UserData());         // 개별부
		
		return standardTelegram;
	}
	
	/**
	 * 전문 복원 Default 헤더 생성
	 * @return
	 */
	public static StandardTelegram defaultFep() {
		
		StandardTelegram standardTelegram = defaultInit();
		
		List<Object> sttlList = new ArrayList<>();
		
		sttlList.add(standardTelegram.getSttlSysCopt());
		sttlList.add(standardTelegram.getSttlTrnCopt());
		
		sttlList.forEach(data -> {
			Field[] fields = data.getClass().getDeclaredFields();
			
			for (Field field : fields) {				
				SttlField sttlField = field.getAnnotation(SttlField.class);
				
				field.setAccessible(true);
				
				Object defaultValue = null;
				
				// 데이터 변환
				try {
					if (sttlField != null && !sttlField.defaultValue().equals("")) {
						if (field.getType().equals(String.class)) {
							defaultValue = sttlField.defaultValue();
						} else if (field.getType().equals(Integer.class)) {
							defaultValue = Integer.parseInt(sttlField.defaultValue());
						} else if (field.getType().equals(BigDecimal.class)) {
							
						}
					}
				} catch (Exception e) {
					System.out.println("필드 변환 오류");
				}
				
				// 필드 변환
				try {
					field.set(data, defaultValue);
				} catch (Exception e) {
					System.out.println("필드 Set 오류");
				}
			}
		});		
		
		return standardTelegram;
	}
	
	/**
	 * 필드 Value 날짜 타입 체크
	 * @param format
	 * @param value
	 * @return
	 */
	public static boolean dateCheck(String format, Object value) {
		boolean result = true;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		
		try {
			simpleDateFormat.parse(value.toString());
		} catch(Exception e) {
			result = false;
		}
		
		return result;
	}
	
	/**
	 * 필드 Valid 범위 체크
	 * @param startRange
	 * @param endRange
	 * @param value
	 * @return
	 */
	public static boolean rangeCheck(int startRange, int endRange, Object value) {
		boolean result = true;
		
		try {			
			Integer convertValue;
			
			// 변환
			if (value instanceof Integer) {		
				convertValue = ((Integer) value).intValue();
			} else {
				convertValue = Integer.parseInt(value.toString());
			}
			
			// 범위안에 없거나 0보다 작을 때
			if (convertValue < 0 || !(convertValue >= startRange && convertValue <= endRange)) {
				result = false;
			}			
		} catch (Exception e) {
			// 변환 오류시 오류 리턴
			result = false;
		}
		
		return result;
	}
	
	/**
	 * 전문 필드 Value 체크
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public static boolean fieldValueValid(String fieldName, Object fieldValue) {
		
		boolean result = true;
		
		switch (fieldName) {
			// 시스템 공통부 Valid
			case "STTL_LEN" :
				// 길이 대기
				break;
			case "STTL_CMRS_YN" :
				result = Arrays.asList(sttlCmrsYn).contains(fieldValue.toString());
				break;
			case "STTL_ENCP_DCD" :								
				result = Arrays.asList(sttlEncpDcd).contains(fieldValue.toString());
				break;
			case "STTL_LNGG_DCD" :
				result = Arrays.asList(sttlLnggDcd).contains(fieldValue.toString());
				break;
			case "STTL_VER_DSNC" :
				result = fieldValue.toString().equals(sttlVerDsnc);
				break;
			case "WHBN_STTL_WRTN_YMD" :
				result = dateCheck("yyyyMMdd", fieldValue.toString());
				break;	
			case "WHBN_STTL_CRET_SYS_NM" :
				String firstValue = fieldValue.toString().substring(0, 1);
				result = Arrays.asList(whbnSttlCretSysNm).contains(firstValue);
				break;
			case "WHBN_STTL_SRN" :
				// 공백 오류
				break;
			case "WHBN_STTL_PGRS_DSNC_NO" :
				result = rangeCheck(0, 9999, fieldValue.toString());
				break;
			case "WHBN_STTL_PGRS_NO" :
				result = rangeCheck(0, 9999, fieldValue.toString());
				break;
			case "STTL_INTF_ANUN_ID" :
				// 공백 오류
				break;
			case "STTL_IP" :
				if (fieldValue.toString().length() > 15) {
					result = false;
				}
				break;
			case "STTL_MAC_ADR_VL" :
				if (fieldValue.toString().length() != 12) {
					result = false;
				}
				break;
			case "SYS_ENVR_INFO_DCD" :
				result = Arrays.asList(sysEnvrInfoDcd).contains(fieldValue.toString());
				break;
			case "FRST_RQST_TS" :
				result = dateCheck("yyyyMMddHHmmssSSS", fieldValue.toString());
				break;
			case "STTL_MCTM_USE_YN" :
				result = Arrays.asList(sttlMctmUseYn).contains(fieldValue.toString());
				break;
			case "STTL_MCTM_SEC_VL" :
				if (!(fieldValue instanceof Integer))
					result = false;
				break;
			case "TRNM_SYS_DCD" :
				break;
			case "TRNM_ND_ID" :
				break;
			case "TRNM_TS" :
				result = dateCheck("yyyyMMddHHmmssSSS", fieldValue.toString());
				break;
			case "RQST_RSPN_DCD" :
				result = Arrays.asList(rqstRspnDcd).contains(fieldValue.toString());
				break;
			case "STTL_IDPR_VRSN_SRN" :
				break;
			case "RQST_SYS_DCD" :
				result = !Arrays.asList(rqstSysDcd).contains(fieldValue.toString());
				break;				
			case "RQST_SYS_BSWR_DCD" :
				break;
			case "STTL_INTF_ID" :
				break;
			case "SYNC_RSPN_WAIT_DCD" :
				result = Arrays.asList(syncRspnWaitDcd).contains(fieldValue.toString());
				break;
			case "ETAH_TRN_YN" :
				result = Arrays.asList(etahTrnYn).contains(fieldValue.toString());
				break;
			case "INPT_TMGT_DCD" :
				result = Arrays.asList(inptTmgtDcd).contains(fieldValue.toString());
				break;
			case "STTL_RQST_TS" :
				result = dateCheck("yyyyMMddHHmmssSSS", fieldValue.toString());
				break;	
			case "RQST_RCV_SVC_ID" :
				break;
			case "RSPN_RCV_SVC_ID" :
				break;
			case "EROR_RSPN_SVC_ID" :
				break;
			case "RSPN_RCV_ND_ID" :
				break;
			case "BID_RSPN_RCV_ND_IP" :
				break;
			case "BID_RSPN_RCV_PORT_NO" :
				break;
			case "INTF_ND_ID" :
				break;
			case "RSPN_SYS_DCD" :
				
				break;
			case "STTL_RSPN_TS" :
				result = dateCheck("yyyyMMddHHmmssSSS", fieldValue.toString());
				break;
			case "RSPN_PCRS_DCD" :
				result = Arrays.asList(rspnPcrsDcd).contains(fieldValue.toString());
				break;
			case "OTPT_TMGT_DCD" :
				result = Arrays.asList(otptTmgtDcd).contains(fieldValue.toString());
				break;
			case "EROC_SYS_DCD" :
				break;
			case "STTL_ERCD" :
				break;
			case "RQST_CHPT_DCD" :
				break;
			case "RQST_CHPT_DTLS_DCD" :
				break;
			case "RQST_CHBS_DCD" :
				break;
			case "RQST_CHNL_SSN_ID" :
				break;				
			case "SSO_USE_YN" :
				//result = Arrays.asList(ssoUseYn).contains(fieldValue.toString());
				break;
			case "LRQN_INOP_TRN_GLBL_ID" :
				
				break;
			case "LRQN_INOP_CTNT_NO" :
				if (!(fieldValue instanceof Integer))
					result = false;
				break;
			case "SYS_COPT_FLOP" :
				break;
				
			// 거래공통부 Valid
			case "NTCD" :
				break;			
			case "BNCD" :
				break;
			case "TRN_CHNL_DCD" :
				result = Arrays.asList(trnChnlDcd).contains(fieldValue.toString());
				break;			
			case "TRRQ_BSWR_DCD" :
				break;
			case "STTL_XCD" :
				break;				
			case "STTL_RQST_FUNC_DSNC_ID" :
				break;
			case "STTL_RSPN_FUNC_DSNC_ID" :
				break;
			case "STTL_EROR_FUNC_DSNC_ID" :
				break;
			case "INPT_SCRE_NO" :
				break;					
			case "AHD_IQTR_YN" :
				break;			
			case "CNCL_DCD" :
				break;
			case "CTNT_TRN_DCD" :
				result = Arrays.asList(SttlFieldUtil.ctntTrnDcd).contains(fieldValue.toString());
				break;				
			case "INPT_TRN_SRN" :
				break;
			case "BLNG_FNCM_CD" :
				break;
			case "EXT_TRN_BSWR_DCD" :
				break;
			case "EXT_TRN_INTT_DCD" :
				break;					
			case "EXT_TRN_UNQ_ID" :
				break;				
			case "EXT_LNK_INTF_ID" :
				break;
			case "EXT_TRN_SSN_ID" :
				break;
			case "EXT_TRN_RUTN_ID" :
				break;
			case "TRN_COPT_FLOP" :
				break;					
			default :
				break;
		}
		
		return result;
	}
	
	/**
	 * 전문에 대한 세부 데이터 검증
	 * @param rqstRspnDcd
	 * @param standardTelegram
	 * @return
	 */
	public static List<String> fieldValueDetailValid(String rqstRspnDcd, StandardTelegram standardTelegram) {
		List<String> errorList = new ArrayList<>();
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
		String currentTime = formatter.format(new Date());
		
		String tempData;
		
		// 전행표준전문작성년월일
		if (!currentTime.equals(standardTelegram.getSttlSysCopt().getWhbnSttlWrtnYmd())) {
			errorList.add("WHBN_STTL_WRTN_YMD : Value Error");
		}
		
		// 시스템환경정보구분코드(표준전문생성시스템명 첫번째 자리값?)
		tempData = standardTelegram.getSttlSysCopt().getWhbnSttlCretSysNm().substring(0, 1);
		if (!tempData.equals(standardTelegram.getSttlSysCopt().getSysEnvrInfoDcd())) {
			errorList.add("SYS_ENVR_INFO_DCD : Value Error");
		}		
		
		// 최초요청일시
		tempData = standardTelegram.getSttlSysCopt().getFrstRqstTs().substring(0, 8);
		if (!currentTime.equals(tempData)) {
			errorList.add("FRST_RQST_TS : Value Error");
		}
		
		// 송신일시
		tempData = standardTelegram.getSttlSysCopt().getTrnmTs().substring(0, 8);
		if (!currentTime.equals(tempData)) {
			errorList.add("TRNM_TS : Value Error");
		}
		
		// 표준전문요청일시
		tempData = standardTelegram.getSttlSysCopt().getSttlRqstTs().substring(0, 8);
		if (!currentTime.equals(tempData)) {
			errorList.add("STTL_RQST_TS : Value Error");
		}
		
		switch (rqstRspnDcd) {
			case "S" :
				requestValid(errorList, standardTelegram);
				break;
			case "R" :
				replyValid(errorList, standardTelegram);
				break;
			default :
				break;
		}
		
		return errorList;
	}
	
	/**
	 * 요청 Etc Valid
	 * @param errorList
	 * @param standardTelegram
	 */
	public static void requestValid(List<String> errorList, StandardTelegram standardTelegram) {
		
	}
	
	/**
	 * 응답 Etc Valid
	 * @param errorList
	 * @param standardTelegram
	 */
	public static void replyValid(List<String> errorList, StandardTelegram standardTelegram) {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
		String currentTime = formatter.format(new Date());
		
		String tempData;
		
		// 응답시스템구분코드
		tempData = standardTelegram.getSttlSysCopt().getRspnSysDcd();
		if (standardTelegram.getSttlSysCopt().getRqstRspnDcd().equals("R") && (tempData == null || tempData.equals(""))) {
			errorList.add("RSPN_SYS_DCD : Value Error");
		}
				
		// 표준전문응답일시
		tempData = standardTelegram.getSttlSysCopt().getSttlRspnTs().substring(0, 8);
		if (!currentTime.equals(tempData)) {
			errorList.add("STTL_RSPN_TS : Value Error");
		}
				
		// 응답처리결과구분코드
		tempData = standardTelegram.getSttlSysCopt().getRspnPcrsDcd();
		if (standardTelegram.getSttlSysCopt().getRqstRspnDcd().equals("R") && (tempData == null || tempData.equals(""))) {
			errorList.add("RSPN_PCRS_DCD : Value Error");
		}
				
		// 오류발생시스템구분코드
		tempData = standardTelegram.getSttlSysCopt().getErocSysDcd();
		if (standardTelegram.getSttlSysCopt().getRqstRspnDcd().equals("S") && !(tempData == null || tempData.equals(""))) {
			errorList.add("EROC_SYS_DCD : Value Error");
		} else if (!standardTelegram.getSttlSysCopt().getRspnPcrsDcd().equals("0") && (tempData == null || tempData.equals(""))) {
			errorList.add("EROC_SYS_DCD : Value Error");
		}
	}
	
	/**
	 * JSR-303 Group Type 지정
	 * @param rqstRspnDcd
	 * @return
	 */
	public static Class<?> getType(String rqstRspnDcd) {
		Class<?> classType = null;
		
		switch (rqstRspnDcd) {
			case "S" :
				classType = Request.class;
				break;
			case "R" :
				classType = Reply.class;
				break;
			default :
				break;
		}
		
		return classType;
	}
	
	/**
	 * 오류 메시지 Set
	 * @param bizCode
	 * @param spot
	 * @param errorMsg
	 * @param standardTelegram
	 */
	public static void errorMsgSet(String sysCode, String spot, String errorMsg, StandardTelegram standardTelegram) {
		standardTelegram.getSttlSysCopt().setRspnPcrsDcd("1");    // 응답처리결과구분코드
		standardTelegram.getSttlSysCopt().setErocSysDcd(spot);    // 오류 발생위치
		
		String errorCode = "E" + sysCode + spot + "00001";         // 메시지 유형(E) + 시스템 구분 + 업무구분 + 일련번호 
		
		standardTelegram.getSttlSysCopt().setSttlErcd(errorCode); // 표준전문오류코드
		standardTelegram.getSttlSysCopt().setSysCoptFlop(errorMsg);
	}
}

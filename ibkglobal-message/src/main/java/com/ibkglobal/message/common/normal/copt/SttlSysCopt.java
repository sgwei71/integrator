package com.ibkglobal.message.common.normal.copt;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibkglobal.common.validator.sttl.Reply;
import com.ibkglobal.common.validator.sttl.Request;
import com.ibkglobal.common.validator.sttl.SttlField;

import lombok.Data;

/**
 * 표준 전문 :: 시스템 공통부 Model
 * 
 * @author Lee Hyungjoo
 * @date 2018. 5. 29.
 *
 */
@Data
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SttlSysCopt implements Serializable {

	@SttlField(fieldName = "STTL_LEN", length = 6, groups = { Request.class, Reply.class })
	private Integer sttlLen; // 표준전문길이

	@SttlField(fieldName = "STTL_CMRS_YN", length = 1, groups = { Request.class, Reply.class }, defaultValue = "N")
	private String sttlCmrsYn; // 표준전문압축여부

	@SttlField(fieldName = "STTL_ENCP_DCD", length = 1, groups = { Request.class, Reply.class }, defaultValue = "0")
	private String sttlEncpDcd; // 표준전문암호화구분코드

	@SttlField(fieldName = "STTL_LNGG_DCD", length = 2, groups = { Request.class, Reply.class }, defaultValue = "KO")
	private String sttlLnggDcd; // 표준전문언어구분코드

	@SttlField(fieldName = "STTL_VER_DSNC", length = 3, groups = { Request.class, Reply.class })
	private String sttlVerDsnc; // 표준전문버전구분값

	@SttlField(fieldName = "WHBN_STTL_WRTN_YMD", length = 8, groups = { Request.class, Reply.class })
	private String whbnSttlWrtnYmd; // 전행표준전문작성년월일

	@SttlField(fieldName = "WHBN_STTL_CRET_SYS_NM", length = 8, groups = { Request.class, Reply.class })
	private String whbnSttlCretSysNm; // 전행표준전문생성시스템명

	@SttlField(fieldName = "WHBN_STTL_SRN", length = 14, groups = { Request.class, Reply.class })
	private String whbnSttlSrn; // 전행표준전문일련번호

	@SttlField(fieldName = "WHBN_STTL_PGRS_DSNC_NO", length = 4, groups = { Request.class,
			Reply.class }, valueCheck = false, defaultValue = "0000")
	private Integer whbnSttlPgrsDsncNo; // 전행표준전문진행구분번호

	@SttlField(fieldName = "WHBN_STTL_PGRS_NO", length = 4, groups = { Request.class,
			Reply.class }, defaultValue = "0000")
	private Integer whbnSttlPgrsNo; // 전행표준전문진행번호

	@SttlField(fieldName = "STTL_INTF_ANUN_ID", length = 34, groups = { Request.class, Reply.class })
	private String sttlIntfAnunId; // 표준전문인터페이스UNIQUEID

	@SttlField(fieldName = "STTL_IP", length = 39, groups = { Request.class, Reply.class })
	private String sttlIp; // 표준전문IP

	@SttlField(fieldName = "STTL_MAC_ADR_VL", length = 12, groups = { Request.class, Reply.class })
	private String sttlMacAdrVl; // 표준전문MAC주소값

	@SttlField(fieldName = "SYS_ENVR_INFO_DCD", length = 1, groups = { Request.class, Reply.class })
	private String sysEnvrInfoDcd; // 시스템환경정보구분코드

	@SttlField(fieldName = "FRST_RQST_TS", length = 17, groups = { Request.class, Reply.class })
	private String frstRqstTs; // 최초요청일시

	@SttlField(fieldName = "STTL_MCTM_USE_YN", length = 1, groups = { Request.class, Reply.class }, defaultValue = "N")
	private String sttlMctmUseYn; // 표준전문유지시간사용여부

	@SttlField(fieldName = "STTL_MCTM_SEC_VL", length = 3, groups = { Request.class,
			Reply.class }, valueCheck = false, defaultValue = "000")
	private Integer sttlMctmSecVl; // 표준전문유지시간초값

	@SttlField(fieldName = "TRNM_SYS_DCD", length = 3, groups = { Request.class, Reply.class })
	private String trnmSysDcd; // 송신시스템구분코드

	@SttlField(fieldName = "TRNM_ND_ID", length = 8, groups = { Request.class, Reply.class })
	private String trnmNdId; // 송신노드ID

	@SttlField(fieldName = "TRNM_TS", length = 17, groups = { Request.class, Reply.class })
	private String trnmTs; // 송신일시

	@SttlField(fieldName = "RQST_RSPN_DCD", length = 1, groups = { Request.class, Reply.class }, defaultValue = "S")
	private String rqstRspnDcd; // 요청응답구분코드

	@SttlField(fieldName = "STTL_IDPR_VRSN_SRN", length = 5, groups = { Request.class,
			Reply.class }, valueCheck = false, defaultValue = "00000")
	private Integer sttlIdprVrsnSrn; // 표준전문개별부버전일련번호

	@SttlField(fieldName = "RQST_SYS_DCD", length = 3, groups = { Request.class, Reply.class })
	private String rqstSysDcd; // 요청시스템구분코드

	@SttlField(fieldName = "RQST_SYS_BSWR_DCD", length = 3, groups = { Request.class, Reply.class })
	private String rqstSysBswrDcd; // 요청시스템업무구분코드

	@SttlField(fieldName = "STTL_INTF_ID", length = 12, groups = { Request.class, Reply.class })
	private String sttlIntfId; // 표준전문인터페이스ID

	@SttlField(fieldName = "SYNC_RSPN_WAIT_DCD", length = 1, groups = { Request.class, Reply.class })
	private String syncRspnWaitDcd; // 동기응답대기구분코드

	@SttlField(fieldName = "ETAH_TRN_YN", length = 1, groups = { Request.class, Reply.class }, defaultValue = "N")
	private String etahTrnYn; // XA거래여부

	@SttlField(fieldName = "INPT_TMGT_DCD", length = 1, groups = { Request.class, Reply.class }, defaultValue = "0")
	private String inptTmgtDcd; // 입력전문유형구분코드

	@SttlField(fieldName = "STTL_RQST_TS", length = 17, groups = { Request.class, Reply.class })
	private String sttlRqstTs; // 표준전문요청일시

	@SttlField(fieldName = "RQST_RCV_SVC_ID", length = 12, groups = { Request.class, Reply.class }, valueCheck = false)
	private String rqstRcvSvcId; // 요청수신서비스ID

	@SttlField(fieldName = "RSPN_RCV_SVC_ID", length = 12, groups = { Request.class, Reply.class }, valueCheck = false)
	private String rspnRcvSvcId; // 응답수신서비스ID

	@SttlField(fieldName = "EROR_RSPN_SVC_ID", length = 12, groups = { Request.class, Reply.class }, valueCheck = false)
	private String erorRspnSvcId; // 오류응답서비스ID

	@SttlField(fieldName = "RSPN_RCV_ND_ID", length = 8, groups = { Request.class, Reply.class }, valueCheck = false)
	private String rspnRcvNdId; // 응답수신노드ID

	@SttlField(fieldName = "BID_RSPN_RCV_ND_IP", length = 39, groups = { Request.class,
			Reply.class }, valueCheck = false)
	private String bidRspnRcvNdIp; // 비드응답수신노드IP

	@SttlField(fieldName = "BID_RSPN_RCV_PORT_NO", length = 5, groups = { Request.class,
			Reply.class }, valueCheck = false)
	private Integer bidRspnRcvPortNo; // 비드응답수신포트번호

	@SttlField(fieldName = "INTF_ND_ID", length = 8, groups = { Request.class, Reply.class }, valueCheck = false)
	private String intfNdId; // 인터페이스노드ID

	@SttlField(fieldName = "RSPN_SYS_DCD", length = 3, groups = Reply.class)
	private String rspnSysDcd; // 응답시스템구분코드

	@SttlField(fieldName = "STTL_RSPN_TS", length = 17, groups = Reply.class)
	private String sttlRspnTs; // 표준전문응답일시

	@SttlField(fieldName = "RSPN_PCRS_DCD", length = 1, groups = Reply.class)
	private String rspnPcrsDcd; // 응답처리결과구분코드

	@SttlField(fieldName = "OTPT_TMGT_DCD", length = 1, groups = Reply.class)
	private String otptTmgtDcd; // 출력전문유형구분코드

	@SttlField(fieldName = "EROC_SYS_DCD", length = 3, groups = Reply.class, valueCheck = false)
	private String erocSysDcd; // 오류발생시스템구분코드

	@SttlField(fieldName = "STTL_ERCD", length = 12, groups = Reply.class, valueCheck = false)
	private String sttlErcd; // 표준전문오류코드

	@SttlField(fieldName = "RQST_CHPT_DCD", length = 3, groups = { Request.class, Reply.class })
	private String rqstChptDcd; // 요청채널유형구분코드

	@SttlField(fieldName = "RQST_CHPT_DTLS_DCD", length = 3, groups = { Request.class, Reply.class })
	private String rqstChptDtlsDcd; // 요청채널유형세부구분코드

	@SttlField(fieldName = "RQST_CHBS_DCD", length = 3, groups = { Request.class, Reply.class })
	private String rqstChbsDcd; // 요청채널업무구분코드

	@SttlField(fieldName = "RQST_CHNL_SSN_ID", length = 23, groups = { Request.class, Reply.class }, valueCheck = false)
	private String rqstChnlSsnId; // 요청채널세션ID

	@SttlField(fieldName = "SSO_USE_YN", length = 1, groups = { Request.class, Reply.class }, valueCheck = false)
	private String ssoUseYn; // SSO사용여부

	@SttlField(fieldName = "LRQN_INOP_TRN_GLBL_ID", length = 30, groups = { Request.class, Reply.class })
	private String lrqnInopTrnGlblId; // 대량입출력거래글로벌ID

	@SttlField(fieldName = "LRQN_INOP_CTNT_NO", length = 2, groups = { Request.class,
			Reply.class }, defaultValue = "00")
	private Integer lrqnInopCtntNo; // 대량입출력연속번호

	@SttlField(fieldName = "SYS_COPT_FLOP", length = 22)
	private String sysCoptFlop; // 시스템공통부예비필드

}

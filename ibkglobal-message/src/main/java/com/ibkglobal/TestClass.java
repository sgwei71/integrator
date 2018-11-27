package com.ibkglobal;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.copt.SttlSysCopt;
import com.ibkglobal.message.common.normal.copt.SttlTrnCopt;
import com.ibkglobal.message.converter.iso.IsoToUserData;
import com.ibkglobal.message.converter.iso.UserDataToIso;
import com.ibkglobal.message.converter.telegram.CommonConverter;
import com.ibkglobal.message.converter.telegram.FlatToTelegram;
import com.ibkglobal.message.converter.telegram.TelegramToFlat;

public class TestClass {
	
	public static void main(String args[]) throws Exception {
		
		SimpleDateFormat formatterHHmmssSSS =
	            new SimpleDateFormat ("HHmmssSSS", java.util.Locale.KOREA);
		
		Date currentTime = new Date();
		
		String dateHHmmssSSS = formatterHHmmssSSS.format(currentTime);
		
		System.out.println("시간 : " + dateHHmmssSSS);
		
		String testData = "{\"sttlSysCopt\":{\"sttlLen\":\"123456\",\"sttlCmrsYn\":\"N\",\"sttlEncpDcd\":\"0\",\"sttlLnggDcd\":\"KR\",\"sttlVerDsnc\":\"001\",\"whbnSttlWrtnYmd\":\"20181004\",\"whbnSttlCretSysNm\":\"D0719071\",\"whbnSttlSrn\":\"8060463400001\",\"whbnSttlPgrsDsncNo\":\"0001\",\"whbnSttlPgrsNo\":\"0001\",\"sttlIntfAnunId\":\"20181004D071907180604634000010001\",\"sttlIp\":\"10.104.162.82\",\"sttlMacAdrVl\":\"3CD92B5699B8\",\"sysEnvrInfoDcd\":\"D\",\"frstRqstTs\":\"20181004180604634\",\"sttlMctmUseYn\":\"N\",\"trnmSysDcd\":\"GMC\",\"trnmNdId\":\"TERMINAL\",\"trnmTs\":\"20181004180604634\",\"rqstRspnDcd\":\"S\",\"rqstSysDcd\":\"GIT\",\"rqstSysBswrDcd\":\"COM\",\"sttlIntfId\":\"GITO00000750\",\"syncRspnWaitDcd\":\"S\",\"etahTrnYn\":\"N\",\"inptTmgtDcd\":\"0\",\"sttlRqstTs\":\"20181004180604634\",\"rqstRcvSvcId\":\"GCBCOM166010\",\"rqstChptDcd\":\"GIT\",\"rqstChptDtlsDcd\":\"ITR\",\"rqstChbsDcd\":\"COM\",\"lrqnInopTrnGlblId\":\"20181004D07190718060463400001\",\"lrqnInopCtntNo\":\"00\"},\"sttlTrnCopt\":{\"ntcd\":\"KR\",\"bncd\":\"00\",\"trnChnlDcd\":\"OLT\",\"trrqBswrDcd\":\"GCB\",\"sttlXcd\":\"GCBCOM166010800\",\"sttlRqstFuncDsncId\":\"800\",\"inptScreNo\":\"16801\",\"ahdIqtrYn\":\"0\",\"cnclDcd\":\"1\",\"ctntTrnDcd\":\"0\",\"blngFncmCd\":\"003\"},\"sttlAmgcCopt\":{\"dtstDcd\":\"MC\",\"dtstLen\":\"123456\",\"trmnInltBrcd\":\"0719\",\"tmn\":\"001\",\"bnkbSrn\":\"\",\"smatInqSrn\":\"\",\"icChipMdiaKindDcd\":\"\",\"brcd\":\"0719\",\"optoEmn\":\"F14858\",\"svatDcd\":\"0\",\"opatDcd\":\"0\",\"trmgAthzDcd\":\"0\"},\"endSignal\":\"@@\"}";
		
		//String testData = "{\"sttlSysCopt\":{\"sttlLen\":\"123456\",\"sttlCmrsYn\":\"N\",\"sttlEncpDcd\":\"0\",\"sttlLnggDcd\":\"KR\",\"sttlVerDsnc\":\"001\",\"whbnSttlWrtnYmd\":\"20181004\",\"whbnSttlCretSysNm\":\"D0719071\",\"whbnSttlSrn\":\"8060463400001\",\"whbnSttlPgrsDsncNo\":\"0001\",\"whbnSttlPgrsNo\":\"0001\",\"sttlIntfAnunId\":\"20181004D071907180604634000010001\",\"sttlIp\":\"10.104.162.82\",\"sttlMacAdrVl\":\"3CD92B5699B8\",\"sysEnvrInfoDcd\":\"D\",\"frstRqstTs\":\"20181004180604634\",\"sttlMctmUseYn\":\"N\",\"trnmSysDcd\":\"GMC\",\"trnmNdId\":\"TERMINAL\",\"trnmTs\":\"20181004180604634\",\"rqstRspnDcd\":\"S\",\"rqstSysDcd\":\"GIT\",\"rqstSysBswrDcd\":\"COM\",\"sttlIntfId\":\"GITO00000750\",\"syncRspnWaitDcd\":\"S\",\"etahTrnYn\":\"N\",\"inptTmgtDcd\":\"0\",\"sttlRqstTs\":\"20181004180604634\",\"rqstRcvSvcId\":\"GCBCOM166010\",\"rqstChptDcd\":\"GIT\",\"rqstChptDtlsDcd\":\"ITR\",\"rqstChbsDcd\":\"COM\",\"lrqnInopTrnGlblId\":\"20181004D07190718060463400001\",\"lrqnInopCtntNo\":\"00\"},\"sttlTrnCopt\":{\"trnChnlDcd\":\"OLT\",\"trrqBswrDcd\":\"GCB\",\"sttlXcd\":\"GCBCOM166010800\",\"sttlRqstFuncDsncId\":\"800\",\"inptScreNo\":\"16801\",\"ahdIqtrYn\":\"0\",\"cnclDcd\":\"1\",\"ctntTrnDcd\":\"0\",\"blngFncmCd\":\"003\"},\"sttlAmgcCopt\":{\"dtstDcd\":\"MC\",\"dtstLen\":\"123456\",\"trmnInltBrcd\":\"0719\",\"tmn\":\"001\",\"bnkbSrn\":\"\",\"smatInqSrn\":\"\",\"icChipMdiaKindDcd\":\"\",\"brcd\":\"0719\",\"optoEmn\":\"F14858\",\"svatDcd\":\"0\",\"opatDcd\":\"0\",\"trmgAthzDcd\":\"0\"},\"endSignal\":\"@@\"}";
		
		StandardTelegram standardTelegram = new ObjectMapper().readValue(testData, StandardTelegram.class);

//		System.out.println("표준전문 : " + standardTelegram);
//		
//		byte[] data = TelegramToFlat.telegramToFlat(standardTelegram, null);
//		
//		System.out.println("flat 결과 : " + new String(data));
//		System.out.println("flat 길이 : " + data.length);
//		
//		StandardTelegram standardTelegram2 = FlatToTelegram.flatToTelegram(data, null);
//		
//		System.out.println("결과 데이터 : " + standardTelegram2);
		
		//String testError = "002100N0KO02420180903dcbkapa1ID1409027660161419000320180903APA11409023744120000021419172.18.190.71                          3440B5A8F876D20180903140902766N999EAI0000000020180903154230315R00000CBKLONCBKO00032643SN020180903154230292CRDOBG000009                                                                       00000        EAI2018090315423031590EAIEEAIEAI04001ITRBATLON                        20180903dcbkapa1ID14090276601600                      BATITR               800               N100000000000001000                                                                                                                                                                                                MC000124    000  0000        11        22                        0000100N          000-99999999999999-99999999999999-99999999999999 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                00000                                                                                                                MS0006590                              01EEAIEAI0400101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                        0101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                                                                                                                                                                                                                                00000000@@";
		//String testError = "002100N0KO02420180903dcbkapa1ID1409027660161419000320180903APA11409023744120000021419172.18.190.71                          3440B5A8F876D20180903140902766N999EAI0000000020180903154230315R00000CBKLONCBKO00032643SN020180903154230292CRDOBG000009                                                                       00000        EAI2018090315423031590EAIEEAIEAI04001ITRBATLON                        20180903dcbkapa1ID14090276601600                      BATITR               800               N100000000000001000                                                                                                                                                                                                MC000124    000  0000        11        22                        0000100N          000-99999999999999-99999999999999-99999999999999 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                00000                                                                                                                @@";
		
		String testError = "002100N0KO02420180903dcbkapa1ID1409027660161419000320180903APA11409023744120000021419172.18.190.71                          3440B5A8F876D20180903140902766N999EAI0000000020180903154230315R00000CBKLONCBKO00032643SN020180903154230292CRDOBG000009                                                                       00000        EAI2018090315423031590EAIEEAIEAI04001ITRBATLON                        20180903dcbkapa1ID14090276601600                      BATITR               800               N100000000000001000                                                                                                                                                                                                MC000124    000  0000        11        22                        0000100N          000-99999999999999-99999999999999-99999999999999 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                00000                                                                                                                MS0006590                              01EEAIEAI0400101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                        0101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                                                                                                                                                                                                                                00000000@@";
		
		StandardTelegram standardTelegram3 = FlatToTelegram.flatToTelegram(testError.getBytes("MS949"), null);
		
		System.out.println("결과 데이터3 : " + standardTelegram3);
		
		byte[] data = TelegramToFlat.telegramToFlat(standardTelegram3, null);
		
		System.out.println("결과 데이터4 : " + new String(data, "MS949"));
		
		byte[] delimiterData = "Test|Test2|Test3".getBytes();
		
		System.out.println(Arrays.toString(delimiterData));
		
 	}
}

package com.ibkglobal.integrator;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.HeaderCacheFEP;
import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.io.MessageCacheFEP;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.validator.model.ValidResult;
import com.ibkglobal.integrator.manager.model.RouteInfo;
import com.ibkglobal.integrator.tc.ApiTest;
import com.ibkglobal.integrator.tc.ConverterMappingTest;
import com.ibkglobal.integrator.tc.ConverterParsingTest;
import com.ibkglobal.integrator.tc.EndpointCreateTest;
import com.ibkglobal.integrator.tc.RouteCreateTest;
import com.ibkglobal.integrator.tc.RouteTest;
import com.ibkglobal.integrator.tc.SchemaFepTest;
import com.ibkglobal.integrator.tc.SchemaMcaTest;
import com.ibkglobal.integrator.tc.ValidTest;
import com.ibkglobal.message.common.normal.StandardTelegram;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IbkglobalIntegratorApplicationTests {
	
	@Autowired
	EndpointCreateTest endpointCreateTest;     // 엔드포인트 생성
	
	@Autowired
	SchemaMcaTest schemaMcaTest;               // MCA 전문 스키마
	
	@Autowired
	SchemaFepTest schemaFepTest;               // FEP 전문 스키마
	
	@Autowired
	ConverterParsingTest converterParsingTest; // 변환 및 파싱 관련
	
	@Autowired
	ConverterMappingTest converterMappingTest; // 필드 매핑 관련
	
	@Autowired
	ValidTest validTest;                       // 전문 Valid
	
	@Autowired
	RouteCreateTest routeCreateTest;           // 카멜 라우터 정보 생성
	
	@Autowired
	RouteTest routeTest;                       // 카멜 관련 서비스
	
	@Autowired
	ApiTest apiTest;                           // Api
	
	@Test
	public void test1() {
		TlgrMapping mapping = new TlgrMapping();
	}
	

	@Test
	public void endpointCreate() throws Exception {
		
		System.out.println("다이렉트 엔드포인트 : " + endpointCreateTest.endpointCreateDirect());
		System.out.println("TCP 엔드포인트(consumer) : "   + endpointCreateTest.endpointCreateTcp("consumer"));
		System.out.println("TCP 엔드포인트(producer) : "   + endpointCreateTest.endpointCreateTcp("producer"));
		System.out.println("HTTP 엔드포인트(consumer) : "  + endpointCreateTest.endpointCreateHttp("consumer"));
		System.out.println("HTTP 엔드포인트(producer) : "  + endpointCreateTest.endpointCreateHttp("producer"));
		System.out.println("QUEUE 엔드포인트 : " + endpointCreateTest.endpointCreateQueue());
	}
	
	@Test
	public void schemaMca() {
		
		Interface  intf = schemaMcaTest.getInterface("ITRO00000035");	
		IoCacheMCA ioSourceInbound = schemaMcaTest.getSourceIo("ITRO00000035", "IN");
		IoCacheMCA ioTargetInbound = schemaMcaTest.getTargetIo("ITRO00000035", "IN");
		IoCacheMCA ioSourceOutbound = schemaMcaTest.getSourceIo("ITRO00000035", "OUT");
		IoCacheMCA ioTargetOutbound = schemaMcaTest.getTargetIo("ITRO00000035", "OUT");
		List<TlgrMapping> mappingInbound = schemaMcaTest.getMapping("ITRO00000035", "IN");
		List<TlgrMapping> mappingOutbound = schemaMcaTest.getMapping("ITRO00000035", "IN");
		
		System.out.println("MCA 인터페이스      : " + intf);
		System.out.println("MCA ioSource 인바운드 : " + ioSourceInbound);
		System.out.println("MCA ioTarget 인바운드 : " + ioTargetInbound);
		System.out.println("MCA ioSource 아웃바운드 : " + ioSourceOutbound);
		System.out.println("MCA ioTarget 아웃바운드 : " + ioTargetOutbound);
		System.out.println("MCA 매핑 인바운드    : " + mappingInbound);
		System.out.println("MCA 매핑 아웃바운드 : " + mappingOutbound);
	}
	
	@Test
	public void schemaFep() {
		
		Interface intf = schemaFepTest.getInterface("FEPO00000494");
		String intfKey = schemaFepTest.getInterfaceKey("IGAT_IGAT_E0200_300000");
		MessageCacheFEP message = schemaFepTest.getMessage("IGAT_IGAT_E0200_300000");
		HeaderCacheFEP  headerSource = schemaFepTest.getSourceHeader("FEPO00000494");
		LinkedList<IoCacheFEP> headerEntitySource = schemaFepTest.getSourceHeaderEntity("FEPO00000494");
		IoCacheFEP bodyEntitySource = schemaFepTest.getSourceBodyEntity("FEPO00000494");
		
		System.out.println("FEP 인터페이스 : " + intf);
		System.out.println("FEP 인터페이스 키 : " + intfKey);
		System.out.println("FEP 메시지 : " + message);
		System.out.println("FEP 헤더 : " + headerSource);
		System.out.println("FEP 헤더 엔티티 : " + headerEntitySource);
		System.out.println("FEP 바디 엔티티 : " + bodyEntitySource);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void mapping() throws Exception {
		
		List<TlgrMapping> mappingList = schemaMcaTest.getMapping("ITRO00000035", "IN");
		List<Tlgr> targetList = schemaMcaTest.getTargetIo("ITRO00000035", "OUT").getInBound().getFieldList();	
		
		String userJson = "{\"TEST_CD\" : \"cd\", \"TEST_ARRAY_ROWCOUNT\" : 1, \"TEST_ARRAY\" : [{\"CHILD_TEST_CD\" : \"dc\", \"CHILD_TEST_NM\" : \"nm\"}]}";
		
		LinkedHashMap<String, Object> sourceData = Converter.mapper.readValue(userJson, LinkedHashMap.class);		
		LinkedHashMap<String, Object> result = converterMappingTest.mapping(mappingList, sourceData, targetList, "IN", true);
		
		System.out.println("소스 -> 타겟으로 매핑 결과 : " + result);
	}
	
	@Test
	public void parsingConvert() throws Exception {
		
		// JSON - 표준전문 테스트용
		String jsonData = "{\"sttlSysCopt\":{\"sttlLen\":\"123456\",\"sttlCmrsYn\":\"N\",\"sttlEncpDcd\":\"0\",\"sttlLnggDcd\":\"KR\",\"sttlVerDsnc\":\"001\",\"whbnSttlWrtnYmd\":\"20181010\",\"whbnSttlCretSysNm\":\"D0719071\",\"whbnSttlSrn\":\"8060463400001\",\"whbnSttlPgrsDsncNo\":\"0001\",\"whbnSttlPgrsNo\":\"0001\",\"sttlIntfAnunId\":\"20181011D071907180604634000010001\",\"sttlIp\":\"10.104.162.82\",\"sttlMacAdrVl\":\"3CD92B5699B8\",\"sysEnvrInfoDcd\":\"D\",\"frstRqstTs\":\"20181011180604634\",\"sttlMctmUseYn\":\"N\",\"trnmSysDcd\":\"GMC\",\"trnmNdId\":\"TERMINAL\",\"trnmTs\":\"20181011180604634\",\"rqstRspnDcd\":\"S\",\"rqstSysDcd\":\"GIT\",\"rqstSysBswrDcd\":\"COM\",\"sttlIntfId\":\"OBSO00000926\",\"syncRspnWaitDcd\":\"S\",\"etahTrnYn\":\"N\",\"inptTmgtDcd\":\"0\",\"sttlRqstTs\":\"20181011180604634\",\"rqstRcvSvcId\":\"GCBCOM166010\",\"rqstChptDcd\":\"GIT\",\"rqstChptDtlsDcd\":\"ITR\",\"rqstChbsDcd\":\"COM\",\"lrqnInopTrnGlblId\":\"20181011D07190718060463400001\",\"lrqnInopCtntNo\":\"00\"},\"sttlTrnCopt\":{\"ntcd\":\"KR\",\"bncd\":\"00\",\"trnChnlDcd\":\"OLT\",\"trrqBswrDcd\":\"GCB\",\"sttlXcd\":\"GCBCOM166010800\",\"sttlRqstFuncDsncId\":\"800\",\"inptScreNo\":\"16801\",\"ahdIqtrYn\":\"0\",\"cnclDcd\":\"1\",\"ctntTrnDcd\":\"0\",\"blngFncmCd\":\"003\"},\"sttlAmgcCopt\":{\"dtstDcd\":\"MC\",\"dtstLen\":\"123456\",\"trmnInltBrcd\":\"0719\",\"tmn\":\"001\",\"bnkbSrn\":\"\",\"smatInqSrn\":\"\",\"icChipMdiaKindDcd\":\"\",\"brcd\":\"0719\",\"optoEmn\":\"F14858\",\"svatDcd\":\"0\",\"opatDcd\":\"0\",\"trmgAthzDcd\":\"0\"},\"userData\":{\"dtstDcd\":\"IO\",\"dtstLen\":\"\",\"data\":{\"apsrYmd\":\"20180104\",\"crncCd\":\"\",\"inrKindDcd\":\"\"}},\"endSignal\":\"@@\"}";
		StandardTelegram standardTelegram = converterParsingTest.jsonToTelegram(jsonData);		
		String jsonResult                 = converterParsingTest.telegramToJson(standardTelegram);
		
		// FEP 테스트용
		List<Tlgr> headerSchemaList = schemaFepTest.getSourceHeaderEntity("FEPO00000494").get(0).getData().getFieldList(); // 헤더 엔티티 정보
		List<Tlgr> bodySchemaList = schemaFepTest.getSourceBodyEntity("FEPO00000494").getData().getFieldList();		       // 바디 엔티티 정보
		byte[] fepFlatSample = "000011116666660123456789012345678901234567".getBytes();
		LinkedHashMap<String, Object> userData  = converterParsingTest.flatToUserData(fepFlatSample, headerSchemaList, bodySchemaList);		
		byte[] fepFlat = converterParsingTest.userDataToFlat(userData, headerSchemaList, bodySchemaList);
		
		// EAI 테스트용
		String eaiSample = "002100N0KO02420180903dcbkapa1ID1409027660161419000320180903APA11409023744120000021419172.18.190.71                          3440B5A8F876D20180903140902766N999EAI0000000020180903154230315R00000CBKLONCBKO00032643SN020180903154230292CRDOBG000009                                                                       00000        EAI2018090315423031590EAIEEAIEAI04001ITRBATLON                        20180903dcbkapa1ID14090276601600                      BATITR               800               N100000000000001000                                                                                                                                                                                                MC000124    000  0000        11        22                        0000100N          000-99999999999999-99999999999999-99999999999999 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                00000                                                                                                                MS0006590                              01EEAIEAI0400101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                        0101수신 시스템 연결 오류[http://dcrdapa1:31301/crd/service/execute]                                                                                                                                                                                                                                                                                                                                                00000000@@";
		StandardTelegram eaiStandardTeleram = converterParsingTest.eaiFlatToTelegram(eaiSample.getBytes("MS949"));
		byte[] eaiFlat = converterParsingTest.telegramToEaiFlat(eaiStandardTeleram);
		
		// 결과
		System.out.println("JSON -> 표준전문 변환 결과 : " + standardTelegram);		
		System.out.println("표준전문 -> JSON 변환 결과 : " + jsonResult);		
		
		System.out.println("FEP 플랫전문 -> 표준전문 UserData : " + userData);
		System.out.println("표준전문 UserData -> FEP 플랫전문 : " + new String(fepFlat));
		
		System.out.println("대내 EAI 플랫전문 -> 표준전문 변환 결과 : " + eaiStandardTeleram);		
		System.out.println("표준전문 -> 대내 EAI 플랫전문 변환 결과 : " + new String(eaiFlat, "MS949"));
	}
	
	@Test
	public void validator() throws Exception {
		
		String jsonDataT = "{\"sttlSysCopt\":{\"sttlLen\":\"123456\",\"sttlCmrsYn\":\"N\",\"sttlEncpDcd\":\"0\",\"sttlLnggDcd\":\"KR\",\"sttlVerDsnc\":\"001\",\"whbnSttlWrtnYmd\":\"20181012\",\"whbnSttlCretSysNm\":\"D0719071\",\"whbnSttlSrn\":\"8060463400001\",\"whbnSttlPgrsDsncNo\":\"0001\",\"whbnSttlPgrsNo\":\"0001\",\"sttlIntfAnunId\":\"20181012D071907180604634000010001\",\"sttlIp\":\"10.104.162.82\",\"sttlMacAdrVl\":\"3CD92B5699B8\",\"sysEnvrInfoDcd\":\"D\",\"frstRqstTs\":\"20181012180604634\",\"sttlMctmUseYn\":\"N\",\"trnmSysDcd\":\"GMC\",\"trnmNdId\":\"TERMINAL\",\"trnmTs\":\"20181012180604634\",\"rqstRspnDcd\":\"S\",\"rqstSysDcd\":\"GIT\",\"rqstSysBswrDcd\":\"COM\",\"sttlIntfId\":\"OBSO00000926\",\"syncRspnWaitDcd\":\"S\",\"etahTrnYn\":\"N\",\"inptTmgtDcd\":\"0\",\"sttlRqstTs\":\"20181012180604634\",\"rqstRcvSvcId\":\"GCBCOM166010\",\"rqstChptDcd\":\"GIT\",\"rqstChptDtlsDcd\":\"ITR\",\"rqstChbsDcd\":\"COM\",\"lrqnInopTrnGlblId\":\"20181012D07190718060463400001\",\"lrqnInopCtntNo\":\"00\"},\"sttlTrnCopt\":{\"ntcd\":\"KR\",\"bncd\":\"00\",\"trnChnlDcd\":\"OLT\",\"trrqBswrDcd\":\"GCB\",\"sttlXcd\":\"GCBCOM166010800\",\"sttlRqstFuncDsncId\":\"800\",\"inptScreNo\":\"16801\",\"ahdIqtrYn\":\"0\",\"cnclDcd\":\"1\",\"ctntTrnDcd\":\"0\",\"blngFncmCd\":\"003\"},\"sttlAmgcCopt\":{\"dtstDcd\":\"MC\",\"dtstLen\":\"123456\",\"trmnInltBrcd\":\"0719\",\"tmn\":\"001\",\"bnkbSrn\":\"\",\"smatInqSrn\":\"\",\"icChipMdiaKindDcd\":\"\",\"brcd\":\"0719\",\"optoEmn\":\"F14858\",\"svatDcd\":\"0\",\"opatDcd\":\"0\",\"trmgAthzDcd\":\"0\"},\"userData\":{\"dtstDcd\":\"IO\",\"dtstLen\":\"\",\"data\":{\"apsrYmd\":\"20180104\",\"crncCd\":\"\",\"inrKindDcd\":\"\"}},\"endSignal\":\"@@\"}";
		String jsonDataF = "{\"sttlSysCopt\":{\"sttlLen\":\"123456\",\"sttlCmrsYn\":\"N\",\"sttlEncpDcd\":\"0\",\"sttlLnggDcd\":\"KR\",\"sttlVerDsnc\":\"001\",\"whbnSttlWrtnYmd\":\"20181010\",\"whbnSttlCretSysNm\":\"D0719071\",\"whbnSttlSrn\":\"8060463400001\",\"whbnSttlPgrsDsncNo\":\"0001\",\"whbnSttlPgrsNo\":\"0001\",\"sttlIntfAnunId\":\"20181011D071907180604634000010001\",\"sttlIp\":\"10.104.162.82\",\"sttlMacAdrVl\":\"3CD92B5699B8\",\"sysEnvrInfoDcd\":\"D\",\"frstRqstTs\":\"20181011180604634\",\"sttlMctmUseYn\":\"N\",\"trnmSysDcd\":\"GMC\",\"trnmNdId\":\"TERMINAL\",\"trnmTs\":\"20181011180604634\",\"rqstRspnDcd\":\"S\",\"rqstSysDcd\":\"GIT\",\"rqstSysBswrDcd\":\"COM\",\"sttlIntfId\":\"OBSO00000926\",\"syncRspnWaitDcd\":\"S\",\"etahTrnYn\":\"N\",\"inptTmgtDcd\":\"0\",\"sttlRqstTs\":\"20181011180604634\",\"rqstRcvSvcId\":\"GCBCOM166010\",\"rqstChptDcd\":\"GIT\",\"rqstChptDtlsDcd\":\"ITR\",\"rqstChbsDcd\":\"COM\",\"lrqnInopTrnGlblId\":\"20181011D07190718060463400001\",\"lrqnInopCtntNo\":\"00\"},\"sttlTrnCopt\":{\"ntcd\":\"KR\",\"bncd\":\"00\",\"trnChnlDcd\":\"OLT\",\"trrqBswrDcd\":\"GCB\",\"sttlXcd\":\"GCBCOM166010800\",\"sttlRqstFuncDsncId\":\"800\",\"inptScreNo\":\"16801\",\"ahdIqtrYn\":\"0\",\"cnclDcd\":\"1\",\"ctntTrnDcd\":\"0\",\"blngFncmCd\":\"003\"},\"sttlAmgcCopt\":{\"dtstDcd\":\"MC\",\"dtstLen\":\"123456\",\"trmnInltBrcd\":\"0719\",\"tmn\":\"001\",\"bnkbSrn\":\"\",\"smatInqSrn\":\"\",\"icChipMdiaKindDcd\":\"\",\"brcd\":\"0719\",\"optoEmn\":\"F14858\",\"svatDcd\":\"0\",\"opatDcd\":\"0\",\"trmgAthzDcd\":\"0\"},\"userData\":{\"dtstDcd\":\"IO\",\"dtstLen\":\"\",\"data\":{\"apsrYmd\":\"20180104\",\"crncCd\":\"\",\"inrKindDcd\":\"\"}},\"endSignal\":\"@@\"}";
		
		StandardTelegram standardTelegramT = Converter.mapper.readValue(jsonDataT, StandardTelegram.class);
		StandardTelegram standardTelegramF = Converter.mapper.readValue(jsonDataF, StandardTelegram.class);
		
		ValidResult validResultT = validTest.validator(standardTelegramT);
		ValidResult validResultF = validTest.validator(standardTelegramF);
		
		System.out.println("정상 일 때 : " + validResultT);
		System.out.println("아닐 때 : " + validResultF);
	}
	
	@Test
	public void routeCreate() throws Exception {
		
		RouteInfo routeInfo = routeCreateTest.createRouteInfo();
		
		System.out.println("인스턴스 생성 정보 : " + routeInfo);
	}
	
	//@Test
	public void RouteTest() {
		//routeTest.routeAllStart();
		//routeTest.routeAllStop();
		//routeTest.routeStart("");
		//routeTest.routeStop("");
		routeTest.routeAdd(routeCreateTest.createRouteInfo());
		//routeTest.routeRemove("");
	}

}
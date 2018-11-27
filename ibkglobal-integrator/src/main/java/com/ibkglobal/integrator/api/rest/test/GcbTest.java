package com.ibkglobal.integrator.api.rest.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integrator/1.0/manager")
public class GcbTest {
	
	@PostMapping("/gcb/test")
	public String resultTest(@RequestBody String data) {
		System.out.println("데이터 수신");
		
		String result = "{\"sttlSysCopt\":{\"sttlLen\":\"123456\",\"sttlCmrsYn\":\"N\",\"sttlEncpDcd\":\"0\",\"sttlLnggDcd\":\"KR\",\"sttlVerDsnc\":\"001\",\"whbnSttlWrtnYmd\":\"20181002\",\"whbnSttlCretSysNm\":\"D0719071\",\"whbnSttlSrn\":\"8060463400001\",\"whbnSttlPgrsDsncNo\":\"0001\",\"whbnSttlPgrsNo\":\"0001\",\"sttlIntfAnunId\":\"20181002D071907180604634000010001\",\"sttlIp\":\"10.104.162.82\",\"sttlMacAdrVl\":\"3CD92B5699B8\",\"sysEnvrInfoDcd\":\"D\",\"frstRqstTs\":\"20181002180604634\",\"sttlMctmUseYn\":\"N\",\"trnmSysDcd\":\"GMC\",\"trnmNdId\":\"TERMINAL\",\"trnmTs\":\"20181002180604634\",\"rqstRspnDcd\":\"S\",\"rqstSysDcd\":\"GIT\",\"rqstSysBswrDcd\":\"COM\",\"sttlIntfId\":\"FEPO00000738\",\"syncRspnWaitDcd\":\"S\",\"etahTrnYn\":\"N\",\"inptTmgtDcd\":\"0\",\"sttlRqstTs\":\"20181002180604634\",\"rqstRcvSvcId\":\"GCBCOM166010\",\"rqstChptDcd\":\"GIT\",\"rqstChptDtlsDcd\":\"ITR\",\"rqstChbsDcd\":\"COM\",\"lrqnInopTrnGlblId\":\"20181002D07190718060463400001\",\"lrqnInopCtntNo\":\"00\"},\"sttlTrnCopt\":{\"ntcd\":\"KR\",\"bncd\":\"00\",\"trnChnlDcd\":\"OLT\",\"trrqBswrDcd\":\"GCB\",\"sttlXcd\":\"GCBCOM166010800\",\"sttlRqstFuncDsncId\":\"800\",\"inptScreNo\":\"16801\",\"ahdIqtrYn\":\"0\",\"cnclDcd\":\"1\",\"ctntTrnDcd\":\"0\",\"blngFncmCd\":\"003\"},\"sttlAmgcCopt\":{\"dtstDcd\":\"MC\",\"dtstLen\":\"123456\",\"trmnInltBrcd\":\"0719\",\"tmn\":\"001\",\"bnkbSrn\":\"\",\"smatInqSrn\":\"\",\"icChipMdiaKindDcd\":\"\",\"brcd\":\"0719\",\"optoEmn\":\"F14858\",\"svatDcd\":\"0\",\"opatDcd\":\"0\",\"trmgAthzDcd\":\"0\"},\"userData\":{\"dtstDcd\":\"IO\",\"dtstLen\":\"\",\"data\":{\"intfPtrnTlcn\":\"test\",\"msgPtrnTlcn\":\"ATM102\",\"tlgrSnrcDsncTlcn\":\"RS\",\"elpsTrymdTlcn\":\"test\",\"elpsTrsqNoTlcn\":\"test\",\"elpsTrnUqnTlcn\":\"test\",\"hostTrymdTlcn\":\"test\",\"hostRfnoTlcn\":\"test\"}},\"endSignal\":\"@@\"}";
		
		return result;
	}
}

package com.ibkglobal.message.struct.io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.com.ComCode;
import com.ibk.ibkglobal.data.io.IoCacheAPI;
import com.ibk.ibkglobal.data.io.model.Io;
import com.ibk.ibkglobal.data.io.model.IoCommon;
import com.ibk.ibkglobal.data.io.model.IoExternal;
import com.ibk.ibkglobal.data.io.model.IoOnline;
import com.ibk.ibkglobal.data.io.model.ProcessType;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.model.IoInfo;
import com.ibkglobal.message.struct.loader.model.IoLoader;

import lombok.Getter;
import lombok.Setter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class IoAPI extends IoDefault<IoCacheAPI> {
	
	Cache sourceCache;
	Cache targetCache;
	
	@Getter @Setter
	private String jsonFileUrl;
	
	private FileReader fileReader;
	
	@Getter @Setter
	private Converter converter;
	
	private Logger logger = LoggerFactory.getLogger( IoAPI.class);
	public IoAPI(String jsonFileUrl) {
		this.jsonFileUrl=jsonFileUrl;
	}
	public void initLoad(IoLoader ioLoader, FileReader fileReader) {
		this.fileReader = fileReader;
		init(ioLoader, null);
	}
	@Override
	public void init(IoLoader ioLoader, Map<String, IoCacheAPI> ioCacheMap) {
		
		setLoader(ioLoader); //로더 초기화
		
//		super.init(ioLoader, ioCacheMap);
				
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());		
		ioLoader.getIoInfo().forEach(data -> {
			IoCacheAPI loadIO = loadIO(data);
			put(loadIO.getInopId(), loadIO);
		});
//		ioCacheMap.values().forEach(data -> {
//			if (getLoader().getIoInfo()
//					       .stream()
//					       .anyMatch(p -> p.getClazz().equals(data.getInopId())))
//				put(data.getInopId(), data);
//		});
	}
	private IoCacheAPI loadIO(IoInfo ioInfo) {

		ioInfo.getComment();

		String clazz	= 	ioInfo.getClazz();
		String jlid		= 	ioInfo.getJlid();
		String path = FilePath.API_IO + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".xml";
		IoCacheAPI loadIO = null;
		try {
			//xml 2 json 변경
			File file = new File(ResourceUtils.getURL(jsonFileUrl+path).getFile());
			logger.info("io file --> {}",file);
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "MS949"));
				String line = null;
				StringBuffer sb = new StringBuffer();
				while (	(line = bufferedReader.readLine()) !=null) {
					sb.append(line);
				}
				JSONObject xmlJsonObject = XML.toJSONObject(sb.toString());
				 loadIO = loadIO(xmlJsonObject);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bufferedReader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadIO;
	}
	private IoCacheAPI  loadIO(JSONObject xmlJsonObject) throws JSONException {
		JSONObject ioObject = xmlJsonObject.getJSONObject("io");		
		IoCacheAPI ioCacheAPI = new IoCacheAPI();
		ioCacheAPI.setInopId(ioObject.getString("id"));
		//1. common set
		IoCommon common = new IoCommon();
		//1.1. work
		JSONObject commonJsonObject = ioObject.getJSONObject("common");
		ComCode work = new ComCode(commonJsonObject.getJSONObject("work").getString("code"),commonJsonObject.getJSONObject("work").getString("name"));
		common.setBswr(work);
		//1.2 description set
		common.setInopDescCon(commonJsonObject.getString("description"));
		//1.3 writer
		common.setMkrEmpNo(commonJsonObject.getString("writer"));
		//1.4 system set
		ComCode system = new ComCode(commonJsonObject.getJSONObject("system").getString("code"),commonJsonObject.getJSONObject("system").getString("name"));
		common.setSystem(system);
		//1.5 date set
		common.setWrtnTs(commonJsonObject.getString("datetime"));
		ioCacheAPI.setCommon(common);
		
		Io inbound = makeIO(ioObject, "inbound");
		Io outbound = makeIO(ioObject, "outbound");
		ioCacheAPI.setInBound(inbound);
		ioCacheAPI.setOutBound(outbound);
		
//		logger.error(inbound.toString());
//		logger.error(outbound.toString());
//		logger.error(ioCacheAPI.toString());
		
		//4. process_type set
		JSONObject processTypeJsonObject = ioObject.getJSONObject("process_type");
		
		ProcessType processType = new ProcessType();
		IoOnline online = new IoOnline();
		
		//4.1 service id
		JSONObject onlineJsonObject=processTypeJsonObject.getJSONObject("online");
		online.setServiceInId(onlineJsonObject.getJSONObject("service_in").getString("id"));
		//4.2 external
		online.setExternal(new IoExternal());
		//4.3 sync async
		ComCode mtvCd = new ComCode(onlineJsonObject.getJSONObject("syn-asyn").getInt("code")+"",onlineJsonObject.getJSONObject("syn-asyn").getString("name"));
		online.setMtvCd(mtvCd);
		//4.4 bizhub
		ComCode tcslInfoTrknCd = new ComCode(onlineJsonObject.getJSONObject("bizhub").getInt("code")+"",onlineJsonObject.getJSONObject("bizhub").getString("name"));
		online.setTcslInfoTrknCd(tcslInfoTrknCd);//비즈허브 접촉정보 거래
		
		processType.setOnline(online);
		ioCacheAPI.setProcessType(processType);
		
		logger.info(ioCacheAPI.toString());
		
		ioCacheAPI.setProcessType(processType);
		logger.debug(ioCacheAPI.toString());
		return ioCacheAPI;
	}
	private Io makeIO(JSONObject ioObject, String flag) throws JSONException {
 
		String inOutXml =ioObject.getString(flag);
		JSONObject inOutJsonObject = XML.toJSONObject(inOutXml);
		Io io = new Io();
		JSONObject schemaJsonObject = inOutJsonObject.getJSONObject("xs:schema");
		Object complextObj =schemaJsonObject.get("xs:complexType"); 
		
		if(complextObj instanceof JSONObject) {//1 Level인경우
			logger.debug("[IBK INTEGRATOR]__________JSON OBJECT");
			JSONObject complexJsonObject = (JSONObject)complextObj;
			JSONObject sequenceJsonObject =complexJsonObject.getJSONObject("xs:sequence");
			String name = complexJsonObject.getString("name");
			io.setUtInopNm(name);
			io.setUtInopId(name);
//			if (name.indexOf("_") > 0){
//				io.setUtInopId(name.substring(0,name.indexOf("_")));
//			}
			Object elementObj = sequenceJsonObject.get("xs:element");
			
			List<Tlgr> tlgrs = makeTlgrs(elementObj);
			
			io.setFieldList(tlgrs);
			
		}else if(complextObj instanceof JSONArray) {//2 Level이상이 있는 경우
			logger.debug("[IBK INTEGRATOR]__________JSON ARRAY");
			Map<String, List<Tlgr>> m_tempMap = new HashMap<String, List<Tlgr>>();
			JSONArray complexJsonArray = (JSONArray)complextObj;
			//1. complex 타입이 여러개가 된다.
			int size = complexJsonArray.length();
			//2. 첫번째 complex 타입이 즉 중심이 됨
			String firstComplexName = "";
			for (int i = 0; i < size; i++) {
				
				JSONObject complexJsonObject =complexJsonArray.getJSONObject(i);// GROUP, ARRAY에 따라 레퍼런스 되는 객체의 수만큼 생길 것임
				JSONObject sequenceJsonObject =complexJsonObject.getJSONObject("xs:sequence");
				String name = complexJsonObject.getString("name");
				if(i==0)	firstComplexName  = name;
				io.setUtInopNm(name);
				io.setUtInopId(name);
				Object elementObj = sequenceJsonObject.get("xs:element");
				List<Tlgr> tlgrs = makeTlgrs(elementObj);
				m_tempMap.put(name, tlgrs);
				logger.debug(m_tempMap.toString());
			}
			//2Level만을 고려함
			m_tempMap.forEach((k,v)->{
				v.forEach((tlgr) ->{
					String dataTypeNm = tlgr.getDataTypeNm();
					if(dataTypeNm.trim().equals("ARRAY") || dataTypeNm.trim().equals("GROUP")){
						List<Tlgr> list = m_tempMap.get(tlgr.getEnsnFldNm());
						tlgr.setFieldList(list);
					}
				});
			});
			io.setFieldList(m_tempMap.get(firstComplexName));
		}
		
		logger.debug(io.toString());
		return io;
	}
	private List<Tlgr> makeTlgrs(Object elementObj) throws JSONException {
		List<Tlgr> tlgrs = new ArrayList<Tlgr>();
		int seq = 1;
		if(elementObj instanceof JSONObject) { //1건
			JSONObject elementJsonObj = (JSONObject)elementObj;
			Tlgr tlgr = new Tlgr();
			makeTlgr(seq, elementJsonObj, tlgr);
			tlgrs.add(tlgr);
		}else if(elementObj instanceof JSONArray) {//다건
			JSONArray elementJsonArray = (JSONArray)elementObj;
			for (int i = 0; i < elementJsonArray.length(); i++) {
				JSONObject jsonObject = elementJsonArray.getJSONObject(i);
				Tlgr tlgr = new Tlgr();
				makeTlgr(seq, jsonObject, tlgr);
				seq++;
				tlgrs.add(tlgr);
			}
		}
		return tlgrs;
	}
	private void makeTlgr(int seq, JSONObject elementJsonObj, Tlgr tlgr) throws JSONException {
		//max occurs 연결
		String maxoccurs=getStringFromJSONObject(elementJsonObj, "maxOccurs", "NORMAL");
		if(maxoccurs.trim().equals("NORMAL"))	//1. 일반
			makeNormalTlgr(seq, elementJsonObj, tlgr);
		else if(maxoccurs.trim().equals("unbound")) {	//2. ARRAY
			makeArrayTlgr( seq,  elementJsonObj,  tlgr);
		}else if(maxoccurs.trim().equals("1")) { //3. GROUP
			makeGroupTlgr( seq,  elementJsonObj,  tlgr);
		}
	}
	private void makeGroupTlgr(int seq, JSONObject elementJsonObj, Tlgr tlgr) throws JSONException {
		makeArrayTlgr( seq,  elementJsonObj,  tlgr);
	}
	private void makeArrayTlgr(int seq, JSONObject elementJsonObj, Tlgr tlgr) throws JSONException {

		tlgr.setFldNm(elementJsonObj.getJSONObject("xs:annotation").getString("xs:documentation"));
		tlgr.setEnsnFldNm(com.ibkglobal.common.util.StringUtils.convert2CamelCase(elementJsonObj.getString("name"))); //2019.01.02 FIX camel Case
		tlgr.setTlgrSqc(new Integer(seq));
		tlgr.setDataTypeNm("ARRAY");
		JSONObject appInfoJsonObj = elementJsonObj.getJSONObject("xs:annotation").getJSONObject("xs:appinfo");
		tlgr.setAllDataLenCon(appInfoJsonObj.getJSONObject("p:adjustLength").getString("lengthFieldURL"));
		tlgr.setLvlNo(		Integer.toString(appInfoJsonObj.getJSONObject("p:level").getInt(("value")	)	)	);
		tlgr.setInptMndrYn(appInfoJsonObj.getJSONObject("p:mandatory").getString("value")); 
		tlgr.setTlgrDescCon( elementJsonObj.getJSONObject("xs:annotation").getString("xs:documentation"));

	}
	private void makeNormalTlgr(int seq, JSONObject elementJsonObj, Tlgr tlgr) throws JSONException {
		
		tlgr.setFldNm(elementJsonObj.getJSONObject("xs:annotation").getString("xs:documentation"));
		tlgr.setEnsnFldNm(com.ibkglobal.common.util.StringUtils.convert2CamelCase(elementJsonObj.getString("name"))); //2019.01.02 FIX camel Case
		tlgr.setTlgrSqc(new Integer(seq));
		JSONObject appInfoJsonObj = elementJsonObj.getJSONObject("xs:annotation").getJSONObject("xs:appinfo");
		tlgr.setLvlNo(		Integer.toString(appInfoJsonObj.getJSONObject("p:level").getInt(("value")	)	)	);
		tlgr.setInptMndrYn(appInfoJsonObj.getJSONObject("p:mandatory").getString("value"));
		tlgr.setTlgrDescCon( elementJsonObj.getJSONObject("xs:annotation").getString("xs:documentation"));
		JSONObject restrictionJsonObj = elementJsonObj.getJSONObject("xs:simpleType").getJSONObject("xs:restriction");

		String base = getStringFromJSONObject(restrictionJsonObj, "base");
		if(base.contains(":")) {
			base = base.substring(	base.indexOf(":")+1		);
		}
		JSONObject lengthJsonObj= null;
		int length =0;
		switch (base) {
		case "STRING":
			lengthJsonObj=extractJsonObject(restrictionJsonObj,"xs:maxLength") ;
			length = lengthJsonObj.getInt("value");
			tlgr.setAllDataLenCon(Integer.toString(length));
			Object obj = appInfoJsonObj.get("p:encryption");
			if(obj instanceof JSONObject) {
				tlgr.setEncpYn(((JSONObject)obj).getString("value"));
			}else {
				tlgr.setEncpYn("N");
			}
//			tlgr.setEncpYn(appInfoJsonObj.getJSONObject("p:encryption").getString("value"));
			tlgr.setDcfrDataLen(0);
			break;
		case "DECIMAL":
			lengthJsonObj=extractJsonObject(restrictionJsonObj,"xs:totalDigits") ;
			length = lengthJsonObj.getInt("value");
			tlgr.setAllDataLenCon(Integer.toString(length));
			lengthJsonObj=extractJsonObject(restrictionJsonObj,"xs:fractionDigits") ;
			length = lengthJsonObj.getInt("value");
			tlgr.setDcfrDataLen(new Integer(length));
			break;
		case "LONG":
			lengthJsonObj=extractJsonObject(restrictionJsonObj,"xs:totalDigits") ;
			length = lengthJsonObj.getInt("value");
			tlgr.setAllDataLenCon(Integer.toString(length));
			tlgr.setDcfrDataLen(0);
		 break;
		default:
			break;
		}
		tlgr.setDataTypeNm(base);
		
	}
	private String getStringFromJSONObject(JSONObject restrictionJsonObj, String name) {
		return  getStringFromJSONObject(restrictionJsonObj,name, null);
	}
	private String getStringFromJSONObject(JSONObject restrictionJsonObj, String name, String defaultValue){
		String retVal = "";
		if(defaultValue !=null) retVal=defaultValue;
		try {
			Object obj =  restrictionJsonObj.get(name);
			if( obj instanceof String) {
				retVal =  (String)obj;
			}else if(obj instanceof Long){
				retVal =  ((Long)obj).toString();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//logger.debug(e.getMessage());
		}
		return retVal;
	}
	private JSONObject extractJsonObject(JSONObject jsonObject, String name)  {
		try {
			return  jsonObject.getJSONObject(name);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Init
	 * @param source
	 * @param target
	 */
	public void init(Cache source, Cache target) {
		// 캐시 설정
		this.sourceCache = source;
		this.targetCache = target;
	}
	
	@Override
	public IoCacheAPI get(String key) {
		
		if ("file".equals(type)) {
			return getDataList().get(key);
		} 
		
		Element value = sourceCache.get(key);
		
		if (value != null) {
			return (IoCacheAPI) value.getObjectValue();
		}
		
		return (IoCacheAPI) targetCache.get(key).getObjectValue();
	}

	public static void main(String args[]) {
		System.out.println("xs:STRING".substring("xs:STRING".indexOf(":")+1));
		int i = 1 ;
		
		Object obj = (Object)i;
		System.out.println(obj.getClass());
		if( obj instanceof Integer) {
			System.out.println("----------------------------");
		}
		
	}

}

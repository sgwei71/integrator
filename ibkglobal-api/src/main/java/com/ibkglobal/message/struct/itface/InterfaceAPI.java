package com.ibkglobal.message.struct.itface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.com.ComCode;
import com.ibk.ibkglobal.data.intf.Attribute;
import com.ibk.ibkglobal.data.intf.Common;
import com.ibk.ibkglobal.data.intf.EaiType;
import com.ibk.ibkglobal.data.intf.External;
import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.intf.InterfaceIo;
import com.ibk.ibkglobal.data.intf.InterfaceType;
import com.ibk.ibkglobal.data.intf.Internal;
import com.ibk.ibkglobal.data.intf.Method;
import com.ibk.ibkglobal.data.intf.Online;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.model.InterfaceInfo;
import com.ibkglobal.message.struct.loader.model.InterfaceLoader;

import lombok.Getter;
import lombok.Setter;

public class InterfaceAPI extends InterfaceDefault {
	
	@Getter
	@Setter
	private String jsonFileUrl;

	private Logger logger = LoggerFactory.getLogger(InterfaceAPI.class);
	
	public InterfaceAPI(String jsonFileUrl) {
		this.jsonFileUrl	=	 jsonFileUrl;
	}
	public void initLoad(InterfaceLoader interfaceLoader, FileReader fileReader) {
//		this.fileReader = fileReader;
		init(interfaceLoader, null);
	}
	@Override
	public void init(InterfaceLoader interfaceLoader, Map<String, Interface> interfaceMap) {
		
		// 로더 초기화
		setLoader(interfaceLoader);

		setDataList(new ConcurrentHashMap<>());
		interfaceLoader.getInterfaceInfo().forEach(loader -> {
			loadInterface(loader);
		});				
	}
	private void loadInterface(InterfaceInfo loader) {
		String clazz = loader.getClazz();
		String jlid = loader.getJlid();
		String path = FilePath.API_INTERFACE + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".xml";
		try {
			//xml 2 json 변경
			File file = new File(ResourceUtils.getURL(jsonFileUrl+path).getFile());
			logger.info("interface file --> {}",file);
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "MS949"));
				String line = null;
				StringBuffer sb = new StringBuffer();
				while (	(line = bufferedReader.readLine()) !=null) {
					sb.append(line);
				}
				JSONObject xmlObject = XML.toJSONObject(sb.toString());
				Interface transformInterface = transformInterface(xmlObject);
				
				put(transformInterface.getIntfId(), transformInterface);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bufferedReader.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Interface  transformInterface(JSONObject source) throws Exception {
		logger.info("□□□□□□□□□□ transformInterface Start □□□□□□□□□□");
		JSONObject interfaceJsonObject = source.getJSONObject("interface");
		Interface intf = new Interface();
		intf.setIntfId(interfaceJsonObject.getString("id"));
		intf.setIntfVrsnVl(new BigDecimal(interfaceJsonObject.getInt("version")));
		intf.setIntfIdnNm(interfaceJsonObject.getString("name"));
		intf.setIntfNm(interfaceJsonObject.getString("name"));
		
		//1. Common
		JSONObject commonJsonObject = interfaceJsonObject.getJSONObject("common");
		Common common = new Common();
		common.setIntfDescCon(commonJsonObject.getString("description"));
		//1.1 ComCode
		JSONObject systemJsonObject = commonJsonObject.getJSONObject("system");
		ComCode system = new ComCode();
		system.setCode(systemJsonObject.getString("code"));
		system.setName(systemJsonObject.getString("name"));
		common.setSystem(system);
		//1.2 comCode
		JSONObject workJsonObject = commonJsonObject.getJSONObject("work");
		ComCode bswr = new ComCode();
		bswr.setCode(workJsonObject.getString("code"));
		bswr.setName(workJsonObject.getString("name"));
		common.setBswr(bswr);
		
		common.setWrtnTs(commonJsonObject.getString("datetime"));
		common.setMkrEmpNo(commonJsonObject.getString("writer"));
		
		//1.3 attribute
		Attribute attribute = new Attribute();
		JSONObject attributeJsonObject = commonJsonObject.getJSONObject("attribute");
		//1.3.1 process
		JSONObject processJsonObject = attributeJsonObject.getJSONObject("process");
		ComCode process = new ComCode();
		process.setCode(processJsonObject.getInt("code")+"");
		process.setName(processJsonObject.getString("name"));
		attribute.setProcess(process);
		
		//1.3.2 mapping
		JSONObject mappingJsonObject = attributeJsonObject.getJSONObject("mapping");
		ComCode mapping = new ComCode();
		process.setCode(mappingJsonObject.getString("code"));
		process.setName(mappingJsonObject.getString("name"));
		attribute.setMpngYn(mapping);
		
		//1.3.3 online
		JSONObject onlineJsonObject = extractJason(attributeJsonObject,"online");
		
		//1.3.3.1 internal
		Online online = new Online();
		
		JSONObject internalJsonObject = onlineJsonObject.getJSONObject("internal");
		JSONObject methodJsonObject = internalJsonObject.getJSONObject("method");
		Internal internal = new Internal();
		Method method = new Method();
		method.setCode(methodJsonObject.getInt("code")+"");
		method.setName(methodJsonObject.getString("name"));
		internal.setMethod(method);
		online.setInternal(internal);
		
		//1.3.3.2 external API에서는 생략함.(별도 확인)
//		JSONObject externalJsonObject = onlineJsonObject.getJSONObject("external");
		External external = new External();
		online.setExternal(external);
		attribute.setOnline(online);
		
		//1.4 infra
		ComCode infra = new ComCode();
		JSONObject infraJsonObject = attributeJsonObject.getJSONObject("infra");
		infra.setCode(infraJsonObject.getInt("code")+"");
		infra.setName(infraJsonObject.getString("name"));
		attribute.setInfra(infra);
		common.setAttribute(attribute);
		intf.setCommon(common);
		
		//2. InterfaceType
		InterfaceType interfaceType = new InterfaceType();
		
		//2.1 Source
		JSONObject interfaceTypeJsonObject = interfaceJsonObject.getJSONObject("interface_type");
		InterfaceIo src = extractInterfaceIo(interfaceTypeJsonObject, "source");
		interfaceType.setSource(src);
		
		//2.2 Target
		InterfaceIo target = extractInterfaceIo(interfaceTypeJsonObject,"target");

		interfaceType.setTarget(target);
		
		intf.setInterfaceType(interfaceType);

		logger.info("□□□□□□□□□□ Transformation Data For API {}",intf.toString());
		return intf;
	}
	private InterfaceIo extractInterfaceIo(JSONObject interfaceTypeJsonObject,String flag) throws JSONException {
		JSONObject ioTypeJsonObject =interfaceTypeJsonObject.getJSONObject(flag);
		
		InterfaceIo interfaceIo = new InterfaceIo();
		interfaceIo.setInopId(ioTypeJsonObject.getString("ioid"));
		interfaceIo.setInopIdnNm("");
		ComCode system = new ComCode();
		JSONObject systemJsonObject =ioTypeJsonObject.getJSONObject("system");
		system.setCode(systemJsonObject.get("code").toString());
		system.setName(systemJsonObject.getString("name"));
		interfaceIo.setSystem(system);
		
		ComCode work = new ComCode();
		JSONObject workJsonObject =ioTypeJsonObject.getJSONObject("work");
		work.setCode(workJsonObject.getString("code"));
//		work.setName(workJsonObject.getString("name"));
		interfaceIo.setBswr(work);
		
		JSONObject processTypeJsonObject =ioTypeJsonObject.getJSONObject("process_type");
		JSONObject onlineJsonObject = extractJason(processTypeJsonObject,"online");
		EaiType processType = new EaiType();
		String svcId = "";
		Online online = new Online();
		if(flag.equals("source")) {

			if(onlineJsonObject != null ) {
				svcId = onlineJsonObject.getJSONObject("internal").getJSONObject("request_receive").getString("id");
			}
			External external = new External();
			external.setExtBswr(new ComCode());
			external.setExtPrwaCd(new Method());
			external.setHtdspCd(new ComCode());
			external.setSvcId(svcId);
			online.setExternal(external);
		}else if(flag.equals("target")) {
			if(onlineJsonObject != null ) {
				svcId = onlineJsonObject.getJSONObject("internal").getJSONObject("request_receive").getString("id");
			}
			Internal internal = new Internal();
			internal.setSvcId(svcId);
			internal.setMethod(new Method());
			internal.setSvcId(svcId);
			online.setInternal(internal);
		}

		processType.setOnline(online);
		interfaceIo.setProcessType(processType);
		interfaceIo.setMkrEmpNo(ioTypeJsonObject.getString("person"));
		return interfaceIo;
	}
	private JSONObject extractJason(JSONObject jsonObject, String key) throws JSONException {
		try {
			return jsonObject.getJSONObject(key);
		} catch (Exception e) {
			return null;
		}
	}
}

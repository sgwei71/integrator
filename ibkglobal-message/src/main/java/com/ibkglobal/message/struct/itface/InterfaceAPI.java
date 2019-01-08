package com.ibkglobal.message.struct.itface;

import java.io.BufferedReader;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.model.InterfaceLoader;

public class InterfaceAPI extends InterfaceDefault {
	
	@Value("${integrator.config.json-file-url}")
	String jsonFileUrl;
	
	private FileReader fileReader;
	
	public void initLoad(InterfaceLoader interfaceLoader, FileReader fileReader) {
		this.fileReader = fileReader;
		init(interfaceLoader, null);
	}
	@Override
	public void init(InterfaceLoader interfaceLoader, Map<String, Interface> interfaceMap) {
		
		// 로더 초기화
		setLoader(interfaceLoader);
		
		setDataList(new ConcurrentHashMap<>());
		interfaceLoader.getInterfaceInfo().forEach(loader -> {
			String clazz = loader.getClazz();
			String jlid = loader.getJlid();
//			                                                                                                         json\message\api\interface\ITRO00060088.xml
//			json/message/api/interface/ITRO00060088.xml
			String path = FilePath.API_INTERFACE + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".xml";
			try {
				//xml 2 json 변경
				File file = new File(ResourceUtils.getURL("C:/Users/growman/git/integrator/ibkglobal-integrator/src/main/resources/"+path).getFile());
				BufferedReader bufferedReader = null;
				try {
					bufferedReader = new BufferedReader(new java.io.FileReader(file));
					String line = null;
					StringBuffer sb = new StringBuffer();
					while (	(line = bufferedReader.readLine()) !=null) {
						sb.append(line);
						System.out.println(line);
					}
					System.out.println(sb.toString());
					
				} catch (Exception e) {
				} finally {
					bufferedReader.close();
				}
//				JSONObject xmlObject = XML.toJSONObject(string)
//				Interface interface1 = fileReader.readFile(path, Interface.class);
//				
//				
//				System.out.println(interface1.toString());
//				put(interface1.getIntfId(), interface1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});				
	}
}

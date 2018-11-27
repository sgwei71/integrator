package com.ibkglobal.message.struct.itface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.intf.IntfKey;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.model.InterfaceLoader;

import lombok.Getter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class InterfaceFEP extends InterfaceDefault {
	
	private FileReader fileReader;
	
	@Getter
	protected Map<String, String> idnMap = new ConcurrentHashMap<>();	
	protected Cache idnCache;
	
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
			String path = FilePath.FEP_INTERFACE + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".json";
			try {				
				Interface interface1 = fileReader.readFile(path, Interface.class);
				put(interface1.getIntfId(), interface1);
			} catch (Exception e) {}
		});				
	}
	
	public void init(Cache cache, Cache idnCache) {
		setCache(cache);
		this.idnCache = idnCache;
	}
	
	@Override
	public void put(String intfId, Interface tdata) {
		getDataList().put(intfId, tdata);
		
		String externalKey = getExternalKey(tdata);
		if (!StringUtils.isEmpty(externalKey)) {			
			idnMap.entrySet().stream()
				.filter(tempEntity -> intfId.equals(tempEntity.getValue()))
				.forEach(entity -> {
					idnMap.remove(entity.getKey());
				}
			);
			idnMap.put(externalKey, intfId);
		}
	}
	
	@Override
	public Interface get(String key) {
		try {
			if ("file".equals(type)) {
				if (getDataList().get(key) != null) {
					return getDataList().get(key);
				}
				return getDataList().get(idnMap.get(key));
			} else {
				Object value = getCache().get(key).getObjectValue();
				if (value != null) {
					return (Interface) value;
				}
				String interfaceId = (String) idnCache.get(key).getObjectValue();
				return (Interface) getCache().get(interfaceId).getObjectValue();
			}
		} catch (Exception e) {}
		return null;
	}	
	
	/**
	 * Get Interface ID
	 * @param externalKey
	 * @return
	 */
	public String getInterfaceID(String externalKey) {
		if ("file".equals(type)) {
			return idnMap.get(externalKey);
		}
		
		Element element = idnCache.get(externalKey);		
		if (element != null) {
			IntfKey intfKey = (IntfKey)element.getObjectValue();			
			return intfKey.getIntfId();
		} else {
			return null;
		}
	}
	
	/**
	 * Get External Key
	 * @param tdata
	 * @return
	 */
	private String getExternalKey(Interface tdata) {
		String externalKey = null;
		
		// 업무코드
		String workCode = tdata.getCommon().getAttribute().getOnline().getExternal().getExtBswr().getCode();
		
		// 기관코드
		String systemCode = tdata.getCommon().getAttribute().getOnline().getExternal().getOtisCd().getCode();
		if ( !StringUtils.isEmpty(systemCode) && systemCode.contains(";") )	systemCode = "COMM";
		
		// 종별코드
		String msgCode = tdata.getCommon().getAttribute().getOnline().getExternal().getExtTlgrIttcdCon();
		
		String txCode = null;
		String etcCode = null;		
		try {
			// 거래구분코드
			txCode = tdata.getCommon().getAttribute().getOnline().getExternal().getExtTrnDcdCon();
			
			// 대외기타구분코드
			etcCode = tdata.getCommon().getAttribute().getOnline().getExternal().getExtEtcDcdCon();
		} catch (NullPointerException npe) {
			
		}
			
		// 송수신코드(타발/당발(E/H))
		String srCode = tdata.getCommon().getAttribute().getOnline().getExternal().getSnrcCd();
		if ( !StringUtils.isEmpty(srCode)) {
			externalKey = workCode + "_" + systemCode + "_" + srCode + msgCode;
			if (!StringUtils.isEmpty(txCode) && txCode.length() > 0) {
				externalKey += "_" + txCode;
			}
			if (!StringUtils.isEmpty(etcCode) && etcCode.length() > 0) {
				externalKey += "_" + etcCode;
			}
		}
		return externalKey;
	}
}

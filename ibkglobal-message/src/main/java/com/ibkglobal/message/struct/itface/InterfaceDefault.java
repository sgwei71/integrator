package com.ibkglobal.message.struct.itface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.message.struct.loader.MessageRepository;
import com.ibkglobal.message.struct.loader.model.InterfaceLoader;

public class InterfaceDefault extends MessageRepository<InterfaceLoader, Interface> {
	
	@Override
	public void init(InterfaceLoader interfaceLoader, Map<String, Interface> interfaceMap) {	
		// 로더 초기화
		setLoader(interfaceLoader);
		
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());
		interfaceMap.values().forEach(data -> {
			if (getLoader().getInterfaceInfo()
			               .stream()
			               .anyMatch(p -> p.getClazz().equals(data.getIntfId())))
				put(data.getIntfId(), data);
		});
	}	
	
	@Override
	public Interface get(String key) {
		if ("file".equals(type)) {
			return getDataList().get(key);
		}
		return (Interface) getCache().get(key).getObjectValue();
	}

	@Override
	public void put(String intfId, Interface tdata) {
		getDataList().put(intfId, tdata);
	}

	@Override
	public void remove(String key) {
		getDataList().remove(key);
	}
}

package com.ibkglobal.message.struct.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.mapp.Mapping;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.common.file.FileType;
import com.ibkglobal.common.repository.CacheFactory;
import com.ibkglobal.common.repository.CacheType;
import com.ibkglobal.message.struct.io.IoEAI;
import com.ibkglobal.message.struct.itface.InterfaceEAI;
import com.ibkglobal.message.struct.mapping.MappingEAI;

import lombok.Getter;
import lombok.Setter;

@Component
public class ResourceEAI extends ResourceDefault {

	@Getter @Setter
	InterfaceEAI interfaceEAI;
	@Getter @Setter
	IoEAI        ioEAI;
	@Getter @Setter
	MappingEAI   mappingEAI;
	
	@Autowired
	CacheFactory cacheFactory;
	
	@Autowired
	FileReader fileReader;
	
	@Override
	public void init(String type) throws Exception {
		this.type = type;
		
		interfaceEAI = new InterfaceEAI();
		ioEAI        = new IoEAI();
		mappingEAI   = new MappingEAI();
		
		load();
	}

	@Override
	public void load() throws Exception {		
		interfaceEAI.setType(type);
		ioEAI.setType(type);
		mappingEAI.setType(type);
		
		if ("file".equals(type)) {
//			interfaceEAI.init(fileReader.readFileToObject(FileType.EAI_INTERFACE_LOADER), fileReader.readFileToMap(FileType.EAI_INTERFACE));
//			ioEAI.init(fileReader.readFileToObject(FileType.EAI_IO_LOADER), fileReader.readFileToMap(FileType.EAI_IO));
//			mappingEAI.init(fileReader.readFileToObject(FileType.EAI_MAPPING_LOADER), fileReader.readFileToMap(FileType.EAI_MAPPING));
		} else if ("cache".equals(type)) {
			interfaceEAI.init(cacheFactory.getCacheRepository(CacheType.IBK_EAI_INTERFACE), cacheFactory.getCacheRepository(CacheType.IBK_EAI_INTERFACE_KEY));
			ioEAI.init(cacheFactory.getCacheRepository(CacheType.IBK_SOURCE_IO), cacheFactory.getCacheRepository(CacheType.IBK_TARGET_IO));
			mappingEAI.init(cacheFactory.getCacheRepository(CacheType.IBK_MAPPING_IN), cacheFactory.getCacheRepository(CacheType.IBK_MAPPING_OUT));
		}
	}
	
	/**
	 * 국외에서 국내 EAI로 전문 거래 시 필요한 키
	 * @param id
	 * @return
	 */
	public String getEaiKey(String id) {
		return interfaceEAI.getEaiKey(id);
	}
	
	/**
	 * Get Interface
	 * @param id
	 * @return
	 */
	public Interface getInterface(String id) {
		return interfaceEAI.get(id);
	}
	
	/**
	 * Get Source Io
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheMCA getSourceIo(String id, String type) {
		Interface intf    = interfaceEAI.get(id);
		Mapping   mapping = mappingEAI.get(intf.getIntfId(), type);		
		return ioEAI.get(mapping.getSorcInopId());
	}
	
	/**
	 * Get Target Io
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheMCA getTargetIo(String id, String type) {
		Interface intf    = interfaceEAI.get(id);
		Mapping   mapping = mappingEAI.get(intf.getIntfId(), type);
		return ioEAI.get(mapping.getTgtInopId());
	}
	
	/**
	 * Get Mapping List
	 * @param id
	 * @param type
	 * @return
	 */
	public List<TlgrMapping> getMappingList(String id, String type) {
		Interface intf    = interfaceEAI.get(id);
		return mappingEAI.get(intf.getIntfId(), type).getMappingList();
	}
}

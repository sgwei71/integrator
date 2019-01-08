package com.ibkglobal.message.struct.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.IoCacheAPI;
import com.ibk.ibkglobal.data.mapp.Mapping;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.common.file.FileType;
import com.ibkglobal.common.repository.CacheFactory;
import com.ibkglobal.common.repository.CacheType;
import com.ibkglobal.message.struct.io.IoAPI;
import com.ibkglobal.message.struct.itface.InterfaceAPI;
import com.ibkglobal.message.struct.mapping.MappingAPI;

import lombok.Getter;
import lombok.Setter;

@Component
public class ResourceAPI extends ResourceDefault {

	@Getter @Setter
	InterfaceAPI interfaceAPI;
	@Getter @Setter
	IoAPI        ioAPI;
	@Getter @Setter
	MappingAPI   mappingAPI;
	
	@Autowired
	CacheFactory cacheFactory;
	
	@Autowired
	FileReader fileReader;
	
	@Autowired
	Converter converter;
	
	@Value("${integrator.config.json-file-url}")
	String jsonFileUrl;
	
	@Override
	public void init(String type) throws Exception {
		this.type = type;
		
		interfaceAPI = new InterfaceAPI(jsonFileUrl);
		ioAPI        = new IoAPI(jsonFileUrl);
		ioAPI.setConverter(converter);
		mappingAPI   = new MappingAPI();
		//TODO 파일로만 일단 해놓음. 캐시도 추가 해야 함
		interfaceAPI.initLoad(fileReader.readFileToObject(FileType.API_INTERFACE_LOADER), fileReader);
		ioAPI.initLoad(fileReader.readFileToObject(FileType.API_IO_LOADER), fileReader);
		//load();
	}

	@Override
	public void load() throws Exception {		
		interfaceAPI.setType(type);
		ioAPI.setType(type);
		mappingAPI.setType(type);
		
		if ("file".equals(type)) {
			interfaceAPI.init(fileReader.readFileToObject(FileType.API_INTERFACE_LOADER), fileReader.readFileToMap(FileType.API_INTERFACE));
			ioAPI.init(fileReader.readFileToObject(FileType.API_IO_LOADER), fileReader.readFileToMap(FileType.API_IO));
			mappingAPI.init(fileReader.readFileToObject(FileType.API_MAPPING_LOADER), fileReader.readFileToMap(FileType.API_MAPPING));
		} else if ("cache".equals(type)) {
			interfaceAPI.init(cacheFactory.getCacheRepository(CacheType.IBK_API_INTERFACE));
			ioAPI.init(cacheFactory.getCacheRepository(CacheType.IBK_SOURCE_IO), cacheFactory.getCacheRepository(CacheType.IBK_TARGET_IO));
			mappingAPI.init(cacheFactory.getCacheRepository(CacheType.IBK_MAPPING_IN), cacheFactory.getCacheRepository(CacheType.IBK_MAPPING_OUT));
		}
	}
	
	/**
	 * Get Interface
	 * @param id
	 * @return
	 */
	public Interface getInterface(String id) {
		return interfaceAPI.get(id);
	}
	
	/**
	 * Get Source Io
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheAPI getSourceIo(String id, String type) {
		Interface intf    = interfaceAPI.get(id);
		Mapping   mapping = mappingAPI.get(intf.getIntfId(), type);		
		return ioAPI.get(mapping.getSorcInopId());
	}
	
	/**
	 * Get Target Io
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheAPI getTargetIo(String id, String type) {
		Interface intf    = interfaceAPI.get(id);
		Mapping   mapping = mappingAPI.get(intf.getIntfId(), type);
		return ioAPI.get(mapping.getTgtInopId());
	}
	
	/**
	 * Get Mapping List
	 * @param id
	 * @param type
	 * @return
	 */
	public List<TlgrMapping> getMappingList(String id, String type) {
		Interface intf    = interfaceAPI.get(id);
		return mappingAPI.get(intf.getIntfId(), type).getMappingList();
	}
}

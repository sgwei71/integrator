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
import com.ibkglobal.message.struct.io.IoMCA;
import com.ibkglobal.message.struct.itface.InterfaceMCA;
import com.ibkglobal.message.struct.mapping.MappingMCA;

import lombok.Getter;
import lombok.Setter;

@Component
public class ResourceMCA extends ResourceDefault {

	@Getter @Setter
	InterfaceMCA interfaceMCA;
	@Getter @Setter
	IoMCA        ioMCA;
	@Getter @Setter
	MappingMCA   mappingMCA;
	
	@Autowired
	CacheFactory cacheFactory;
	
	@Autowired
	FileReader fileReader;
	
	@Override
	public void init(String type) throws Exception {
		this.type = type;
		
		interfaceMCA = new InterfaceMCA();
		ioMCA        = new IoMCA();
		mappingMCA   = new MappingMCA();
		
		load();
	}

	@Override
	public void load() throws Exception {		
		interfaceMCA.setType(type);
		ioMCA.setType(type);
		mappingMCA.setType(type);
		
		if ("file".equals(type)) {
			interfaceMCA.init(fileReader.readFileToObject(FileType.MCA_INTERFACE_LOADER), fileReader.readFileToMap(FileType.MCA_INTERFACE));
			ioMCA.init(fileReader.readFileToObject(FileType.MCA_IO_LOADER), fileReader.readFileToMap(FileType.MCA_IO));
			mappingMCA.init(fileReader.readFileToObject(FileType.MCA_MAPPING_LOADER), fileReader.readFileToMap(FileType.MCA_MAPPING));
		} else if ("cache".equals(type)) {
			interfaceMCA.init(cacheFactory.getCacheRepository(CacheType.IBK_MCA_INTERFACE));
			ioMCA.init(cacheFactory.getCacheRepository(CacheType.IBK_SOURCE_IO), cacheFactory.getCacheRepository(CacheType.IBK_TARGET_IO));
			mappingMCA.init(cacheFactory.getCacheRepository(CacheType.IBK_MAPPING_IN), cacheFactory.getCacheRepository(CacheType.IBK_MAPPING_OUT));
		}
	}
	
	/**
	 * Get Interface
	 * @param id
	 * @return
	 */
	public Interface getInterface(String id) {
		return interfaceMCA.get(id);
	}
	
	/**
	 * Get Source Io
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheMCA getSourceIo(String id, String type) {
		Interface intf    = interfaceMCA.get(id);
		Mapping   mapping = mappingMCA.get(intf.getIntfId(), type);		
		return ioMCA.get(mapping.getSorcInopId());
	}
	
	/**
	 * Get Target Io
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheMCA getTargetIo(String id, String type) {
		Interface intf    = interfaceMCA.get(id);
		Mapping   mapping = mappingMCA.get(intf.getIntfId(), type);
		return ioMCA.get(mapping.getTgtInopId());
	}
	
	/**
	 * Get Mapping List
	 * @param id
	 * @param type
	 * @return
	 */
	public List<TlgrMapping> getMappingList(String id, String type) {
		Interface intf    = interfaceMCA.get(id);
		return mappingMCA.get(intf.getIntfId(), type).getMappingList();
	}
}

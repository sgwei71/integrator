package com.ibkglobal.message.release.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.mapp.Mapping;
import com.ibkglobal.common.file.FileSave;
import com.ibkglobal.common.repository.CacheType;
import com.ibkglobal.common.repository.service.CacheService;
import com.ibkglobal.message.struct.loader.model.InterfaceInfo;
import com.ibkglobal.message.struct.loader.model.InterfaceLoader;
import com.ibkglobal.message.struct.loader.model.IoInfo;
import com.ibkglobal.message.struct.loader.model.IoLoader;
import com.ibkglobal.message.struct.loader.model.MappingInfo;
import com.ibkglobal.message.struct.loader.model.MappingLoader;
import com.ibkglobal.message.struct.resource.ResourceMCA;

@Service
public class ReleaseService {
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	FileSave fileSave;
	
	@Autowired
	ResourceMCA resourceMCA;
	
	public void releaseAll() {
		releaseInterface();
		releaseIo();
		releaseMapping();
	}
	
	/**
	 * 인터페이스 배포
	 */
	public void releaseInterface() {
		List<Interface> interfaceList = cacheService.findAll(CacheType.IBK_MCA_INTERFACE);
		
		InterfaceLoader interfaceLoader = resourceMCA.getInterfaceMCA().getLoader();
		interfaceLoader.setInterfaceInfo(new ArrayList<>());
		
		Map<String, Interface> interfaceMap = resourceMCA.getInterfaceMCA().getDataList();
		
		interfaceList.forEach(f -> {			
			// 로더
			interfaceLoader.getInterfaceInfo().add(new InterfaceInfo(f.getIntfId(), "", "", "", f.getIntfId()));
			//fileSave.objectToFile(FileType.MCA_INTERFACE, f, f.getIntfId());
			
			// 데이터
			interfaceMap.put(f.getIntfId(), f);
		});
		
		// 로더 신규 배포
		//fileSave.objectToFile(FileType.MCA_INTERFACE_LOADER, interfaceLoader, "");
	}
	
	/**
	 * IO 배포
	 */
	public void releaseIo() {
		List<IoCacheMCA> ioList = cacheService.findAll(CacheType.IBK_SOURCE_IO);
		
		IoLoader ioLoader = resourceMCA.getIoMCA().getLoader();
		ioLoader.setIoInfo(new ArrayList<>());
		
		Map<String, IoCacheMCA> ioMap = resourceMCA.getIoMCA().getDataList();
		
		ioList.forEach(f -> {
			// 로더
			ioLoader.getIoInfo().add(new IoInfo(f.getInopId(), "", "", "", f.getInopId()));
			//fileSave.objectToFile(FileType.MCA_IO, f, f.getInopId());
			
			// 데이터
			ioMap.put(f.getInopId(), f);
		});
				
		// 로더 신규 배포
		//fileSave.objectToFile(FileType.MCA_IO_LOADER, ioLoader, "");
	}
	
	/**
	 * 매핑 배포
	 */
	public void releaseMapping() {
		List<Mapping> mappingList = cacheService.findAll(CacheType.IBK_MAPPING_IN);
		
		MappingLoader mappingLoader = resourceMCA.getMappingMCA().getLoader();		
		mappingLoader.setMapperInfo(new ArrayList<>());
		
		Map<String, Mapping> mappingMap = resourceMCA.getMappingMCA().getDataList();
	
		mappingList.forEach(f -> {
			// 로더
			String clazz = f.getIntfId() + (f.getInndOtbnCd().equals("I") == true ? "_INBOUND" : "_OUTBOUND");
			mappingLoader.getMapperInfo().add(new MappingInfo(clazz, "", "", "", f.getIntfId()));
			//fileSave.objectToFile(FileType.MCA_MAPPING, f, clazz);
			
			// 데이터
			mappingMap.put(clazz, f);
		});
		
		// 로더 신규 배포
		//fileSave.objectToFile(FileType.MCA_MAPPING_LOADER, mappingLoader, "");
	}
}

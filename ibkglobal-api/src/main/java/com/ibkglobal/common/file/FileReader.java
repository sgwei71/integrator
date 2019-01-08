package com.ibkglobal.common.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.mapp.Mapping;
//import com.ibk.kolumbus.broker.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.message.struct.loader.model.HeaderLoader;
import com.ibkglobal.message.struct.loader.model.InterfaceLoader;
import com.ibkglobal.message.struct.loader.model.IoLoader;
import com.ibkglobal.message.struct.loader.model.MappingLoader;
import com.ibkglobal.message.struct.loader.model.MessageLoader;

@Configuration
public class FileReader {
	
	@Value("${integrator.config.json-file-url}")
	String jsonFileUrl;
	
	@Autowired
	Converter converter;
	
	/**
	 * 경로에 있는 파일 원하는 형태로 객체화
	 * @param filePath
	 * @param valueType
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public <T> T readFileToObject(String filePath, Class<?> classType) throws Exception {
		
		if (StringUtils.isEmpty(filePath))
			return null;
		
		File file = null;
		
		try {			
			file = new File(ResourceUtils.getURL(jsonFileUrl + filePath).getFile());		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (file == null) {
			return null;
		}
		
		T result = (T) Converter.mapper.readValue(file, classType);
		
		return result;
	}
	
	/**
	 * 경로에 있는 파일 객체화
	 * @param loaderType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T readFileToObject(FileType loaderType) throws Exception {
		
		if (loaderType == null)
			return null;
		
		String filePath = null;
		Class<?> valueType = null;
		
		switch (loaderType) {
			case MCA_INTERFACE_LOADER :
				filePath  = FilePath.MCA_INTERFACE_LOADER;
				valueType = InterfaceLoader.class;
				break;				
			case MCA_IO_LOADER :
				filePath  = FilePath.MCA_IO_LOADER;
				valueType = IoLoader.class;
				break;				
			case MCA_MAPPING_LOADER :
				filePath  = FilePath.MCA_MAPPING_LOADER;
				valueType = MappingLoader.class;
				break;

			case API_INTERFACE_LOADER :
				filePath  = FilePath.API_INTERFACE_LOADER;
				valueType = InterfaceLoader.class;
				break;				
			case API_IO_LOADER :
				filePath  = FilePath.API_IO_LOADER;
				valueType = IoLoader.class;
				break;				
			case API_MAPPING_LOADER :
				filePath  = FilePath.API_MAPPING_LOADER;
				valueType = MappingLoader.class;
				break;
				
			case FEP_INTERFACE_LOADER :
				filePath  = FilePath.FEP_INTERFACE_LOADER;
				valueType = InterfaceLoader.class;
				break;
			case FEP_MAPPING_LOADER :
				filePath  = FilePath.FEP_MAPPING_LOADER;
				valueType = MappingLoader.class;
				break;
			case FEP_MESSAGE_LOADER :
				filePath  = FilePath.FEP_MESSAGE_LOADER;
				valueType = MessageLoader.class;
				break;
			case FEP_HEADER_LOADER :
				filePath  = FilePath.FEP_HEADER_LOADER;
				valueType = HeaderLoader.class;
				break;
			case FEP_IO_LOADER :
				filePath  = FilePath.FEP_IO_LOADER;
				valueType = IoLoader.class;
				break;
				
			case EAI_INTERFACE_LOADER :
				filePath  = FilePath.EAI_INTERFACE_LOADER;
				valueType = InterfaceLoader.class;
				break;				
			case EAI_IO_LOADER :
				filePath  = FilePath.EAI_IO_LOADER;
				valueType = IoLoader.class;
				break;				
			case EAI_MAPPING_LOADER :
				filePath  = FilePath.EAI_MAPPING_LOADER;
				valueType = MappingLoader.class;
				break;
			default :
				break;
		}
		
		File file = null;
		
		try {			
			file = new File(ResourceUtils.getURL(jsonFileUrl + filePath).getFile());		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (file == null) {
			return null;
		}
		
		T result = (T) Converter.mapper.readValue(file, valueType);
		
		return result;
	}
	
	/**
	 * 지정한 클래스에 맞는 형태로 리턴
	 * @param fileType
	 * @param classType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> readFileToMap(FileType fileType, Class<?> classType) throws Exception {
		
		if (classType == null)
			return null;
		
		String filePath = null;
		Class<?> valueType = null;
		
		switch (fileType) {
			case ROUTE :
				filePath  = FilePath.ROUTE;
				valueType = classType;
				break;
			default :
				break;
		}
		
		Map<String, T> readData = new HashMap<>();
		
		// 폴더 안의 파일들을 사용할 때 사용(재귀로 계속 돌림)
		if (fileType == FileType.ROUTE) {
			try {
				readFile(readData, ResourceUtils.getURL(jsonFileUrl + filePath).getFile(), valueType);
				return readData;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} 
		
		// 단일 파일들만 읽어올 때 사용
		File folder = null;
		
		try {			
			folder = new File(ResourceUtils.getURL(jsonFileUrl + filePath).getFile());		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File[] files = folder.listFiles();
		
		for (File file : files) {	
			if (file.isFile()) {
				readData.put(file.getName(), (T) Converter.mapper.readValue(file, valueType));
			}
		}
		
		return readData;
	}
	
	/**
	 * 
	 * Resource 경로에 있는 파일 Read 후 객체화
	 * 
	 * @param fileType
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> readFileToMap(FileType fileType) throws Exception {
		
		if (fileType == null)
			return null;
		
		String filePath = null;
		Class<?> valueType = null;
		
		switch (fileType) {
			case ROUTE :
				filePath  = FilePath.ROUTE;
				//valueType = RouteCreateInfo.class;
				break;
			
			case MCA_INTERFACE :
				filePath  = FilePath.MCA_INTERFACE;
				valueType = Interface.class;
				break;
			case MCA_IO :
				filePath  = FilePath.MCA_IO;
				valueType = IoCacheMCA.class;
				break;
			case MCA_MAPPING :
				filePath  = FilePath.MCA_MAPPING;
				valueType = Mapping.class;
				break;
			default :
				break;
		}
		
		Map<String, T> readData = new HashMap<>();
		
		// 폴더 안의 파일들을 사용할 때 사용(재귀로 계속 돌림)
		if (fileType == FileType.ROUTE) {
			try {
				readFile(readData, ResourceUtils.getURL(jsonFileUrl + filePath).getFile(), valueType);
				return readData;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} 
		
		// 단일 파일들만 읽어올 때 사용
		File folder = null;
		
		try {			
			folder = new File(ResourceUtils.getURL(jsonFileUrl + filePath).getFile());		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File[] files = folder.listFiles();
		
		for (File file : files) {	
			if (file.isFile()) {
				readData.put(file.getName(), (T) Converter.mapper.readValue(file, valueType));
			}
		}
		
		return readData;
	}
	
	
	
	/**
	 * 폴더 안의 전체 파일들을 읽어오고 싶을 때 사용
	 * @param result
	 * @param filePath
	 * @param valueType
	 */
	@SuppressWarnings("unchecked")
	public <T> void readFile(Map<String, T> result, String filePath, Class<?> valueType) throws Exception {
		File folder = null;
		
		try {			
			folder = new File(filePath);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File[] files = folder.listFiles();
		
		for (File file : files) {	
			if (file.isDirectory()) {
				readFile(result, file.getPath(), valueType);
			}
		    else if (file.isFile()) {
				result.put(file.getName(), (T) Converter.mapper.readValue(file, valueType));
			}
		}
	}
	
	/**
	 * 
	 * Resource 경로에 있는 파일 Read 후 객체화
	 * 
	 * @param path
	 * @param valueType
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public <T> T readFile(String path, Class<?> valueType) throws Exception {
		File file = new File(ResourceUtils.getURL(jsonFileUrl + path).getFile());
		return (T) Converter.mapper.readValue(file, valueType);
	}
}

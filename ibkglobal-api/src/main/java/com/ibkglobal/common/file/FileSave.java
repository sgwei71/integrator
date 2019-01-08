package com.ibkglobal.common.file;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.ibkglobal.common.convert.Converter;

@Configuration
public class FileSave {
	
	@Value("${integrator.config.json-file-url}")
	String jsonFileUrl;
	
	public void objectToFile(FileType loaderType, Object value, String fileName) throws Exception {
		
		String defaultPath = null;
		String filePath    = null;
		
		try {
			defaultPath = ResourceUtils.getURL(jsonFileUrl).getPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		switch (loaderType) {
			case MCA_INTERFACE_LOADER :
				filePath  = FilePath.MCA_INTERFACE_LOADER;
				break;		
			case MCA_INTERFACE :
				filePath  = FilePath.MCA_INTERFACE + "/" + fileName + ".json";
				break;
			case MCA_IO_LOADER :
				filePath  = FilePath.MCA_IO_LOADER;
				break;				
			case MCA_IO :
				filePath  = FilePath.MCA_IO  + "/" + fileName + ".json";
				break;
			case MCA_MAPPING_LOADER :
				filePath  = FilePath.MCA_MAPPING_LOADER;
				break;
			case MCA_MAPPING :
				filePath  = FilePath.MCA_MAPPING  + "/" + fileName + ".json";
				break;
				
			case FEP_INTERFACE_LOADER :
				filePath  = FilePath.FEP_INTERFACE_LOADER;
				break;
			case FEP_MAPPING_LOADER :
				filePath  = FilePath.FEP_MAPPING_LOADER;
				break;
			case FEP_MESSAGE_LOADER :
				filePath  = FilePath.FEP_MESSAGE_LOADER;
				break;
			case FEP_HEADER_LOADER :
				filePath  = FilePath.FEP_HEADER_LOADER;
				break;
			case FEP_IO_LOADER :
				filePath  = FilePath.FEP_IO_LOADER;
				break;
			default :
				break;
		}
		
		// 최종경로 지정
		String url = defaultPath + filePath;
		
		Converter.mapper.writeValue(new File(url), value);
	}
}

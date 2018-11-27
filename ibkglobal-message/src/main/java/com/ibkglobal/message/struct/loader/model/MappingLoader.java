package com.ibkglobal.message.struct.loader.model;

import java.util.List;

import lombok.Data;

@Data
public class MappingLoader {
	
	private String            modified;
	private List<MappingInfo> mapperInfo;
}

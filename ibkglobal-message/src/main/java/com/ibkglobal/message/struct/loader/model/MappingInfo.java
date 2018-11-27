package com.ibkglobal.message.struct.loader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappingInfo {
	
	private String clazz;
	private String comment;
	private String jlid;
	private String trid;
	private String xcid;
}

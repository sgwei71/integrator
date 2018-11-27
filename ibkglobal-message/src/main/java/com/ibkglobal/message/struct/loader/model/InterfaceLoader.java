package com.ibkglobal.message.struct.loader.model;

import java.util.List;

import lombok.Data;

@Data
public class InterfaceLoader {
	
	private String              modified;
	private List<InterfaceInfo> interfaceInfo;
}

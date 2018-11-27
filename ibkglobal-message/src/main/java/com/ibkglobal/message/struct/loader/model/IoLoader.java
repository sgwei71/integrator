package com.ibkglobal.message.struct.loader.model;

import java.util.List;

import lombok.Data;

@Data
public class IoLoader {
	
	private String         modified;
	private List<IoInfo>   ioInfo;
}

package com.ibkglobal.message.struct.loader.model;

import java.util.List;

import lombok.Data;

@Data
public class HeaderLoader {

	private String				modified;
	private List<HeaderInfo>	headerInfo;
}
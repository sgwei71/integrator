package com.ibkglobal.integrator.engine.builder.model;

import lombok.Data;

@Data
public class RestHeader {

	private String headerName;
	private String headerDescription;
	private boolean headerRequired;
}

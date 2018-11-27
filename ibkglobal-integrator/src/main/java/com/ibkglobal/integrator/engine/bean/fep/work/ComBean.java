package com.ibkglobal.integrator.engine.bean.fep.work;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.message.struct.resource.ResourceFEP;

public abstract class ComBean {
	
	@Autowired
	CamelConfig camelConfig;
	
	@Autowired
	ResourceFEP resourceFEP;
}

package com.ibkglobal.integrator.engine.bean.common.batch.work;

import com.ibkglobal.integrator.exception.CommonException;

public interface BatchWork {
	
	public void process() throws CommonException;
}

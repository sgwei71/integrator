package com.ibkglobal.integrator.engine.bean.common.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.engine.bean.common.batch.model.BatchModel;
import com.ibkglobal.integrator.engine.bean.common.batch.work.BatchWork;
import com.ibkglobal.integrator.engine.bean.common.batch.work.FileToFile;

@Component
public class BatchFactory {
	
	@Autowired
	CamelConfig camelConfig;
	
	public BatchWork getWork(BatchModel batchModel) {
		
		BatchWork batchWork = null;
		
		switch (batchModel.getWorkType()) {
		case "4" :
			batchWork = new FileToFile(camelConfig, batchModel);
			break;
		default :
			break;
		}
		
		return batchWork;
	}
}

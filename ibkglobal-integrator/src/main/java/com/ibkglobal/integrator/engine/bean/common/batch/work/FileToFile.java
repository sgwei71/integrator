package com.ibkglobal.integrator.engine.bean.common.batch.work;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.FileEndpoint;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.engine.bean.common.batch.model.BatchModel;
import com.ibkglobal.integrator.engine.bean.common.batch.model.BatchModel.FileModel;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;

/**
 * FILE To FILE (EAI)
 *
 */
public class FileToFile implements BatchWork {
	
	private CamelConfig camelConfig;
	private BatchModel  batchModel;
	
	public FileToFile(CamelConfig camelConfig, BatchModel batchModel) {
		this.camelConfig = camelConfig;
		this.batchModel  = batchModel;
	}

	@Override
	public void process() throws CommonException {
		FileEndpoint sEndpoint = null;
		FileEndpoint eEndpoint = null;
		
		try {
			// Source Model
			FileModel sourceModel = ((FileModel)batchModel.getSource());
			// Target Model
			FileModel targetModel = ((FileModel)batchModel.getTarget());
			
			sEndpoint = camelConfig.getCamelContext().getEndpoint(sourceModel.getPath(), FileEndpoint.class);	
			sEndpoint.setFileName(sourceModel.getFileName());
			
			eEndpoint = camelConfig.getCamelContext().getEndpoint(targetModel.getPath(), FileEndpoint.class);
			
			sEndpoint.start();
			eEndpoint.start();
			
			Exchange source = camelConfig.getConsumerTemplate().receive(sEndpoint, batchModel.getTimeOut());
			
			if (source != null) {
				File sourceFile = source.getIn().getBody(File.class);
				camelConfig.getProducerTemplate().sendBodyAndHeader(eEndpoint, sourceFile, Exchange.FILE_NAME, targetModel.getFileName());
			} else {
				throw new IBKExceptionEAI(ErrorType.EAI_FITE_TO_FILE, "File No Search : " + sourceModel.getFileName());
			}
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.EAI_FITE_TO_FILE, "File To File Error : " + e.getMessage());
		} finally {
			try { if (sEndpoint != null) { sEndpoint.stop(); } } catch (Exception e) {}
			try { if (eEndpoint != null) { eEndpoint.stop(); } } catch (Exception e) {}
		}
	}
}
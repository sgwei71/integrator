package com.ibkglobal.integrator.engine.bean.common.batch.work;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.remote.FtpEndpoint;
import org.apache.camel.component.file.remote.RemoteFile;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;

/**
 * FTP To FILE (EAI)
 *
 */
public class FtpToFile {

	@Autowired
	CamelConfig camelConfig;
	
	/**
	 * Process
	 * @param Exchange exchange
	 * @throws CommonException
	 */
	public void process(Exchange exchange) throws CommonException {
		
		FtpEndpoint<?> sEndpoint = null;
		FileEndpoint eEndpoint = null;
		try {
			sEndpoint = camelConfig.getCamelContext()
					.getEndpoint("ftp://ftp_admin@10.104.162.84:21?password=1234", FtpEndpoint.class);
			sEndpoint.setFtpClient(new FTPClient());
			sEndpoint.setDisconnect(true);
			sEndpoint.setFileName("hello2.txt");
			
			eEndpoint = camelConfig.getCamelContext().getEndpoint("file:src/test/java/com/ibk/kolumbus/file/target", FileEndpoint.class);
			
			sEndpoint.start();
			eEndpoint.start();
			
			RemoteFile<?> sourceFile = camelConfig.getConsumerTemplate().receiveBody(sEndpoint, 3000, RemoteFile.class);
			if (sourceFile != null) {
				camelConfig.getProducerTemplate().sendBodyAndHeader(eEndpoint, sourceFile, Exchange.FILE_NAME, sourceFile.getFileName());
			} else {				
				System.out.println("========================");
				System.out.println(" EAI FTP To FILE 작업 파일 없음!!! ");
				System.out.println("========================");
			}			
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.EAI_FTP_TO_FTP, e.getMessage(), e);
		} finally {
			try { if (sEndpoint != null) { sEndpoint.stop(); } } catch (Exception e) {}
			try { if (eEndpoint != null) { eEndpoint.stop(); } } catch (Exception e) {}
		}
	}
}

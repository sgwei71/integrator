package com.ibkglobal.integrator.engine.bean.eai.work;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.remote.FtpEndpoint;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;

/**
 * FILE To FTP (EAI)
 *
 */
public class FileToFtpEAI {
	
	@Autowired
	CamelConfig camelConfig;

	/**
	 * Process
	 * @param Exchange exchange
	 * @throws CommonException
	 */
	public void process(Exchange exchange) throws CommonException {
		
		FileEndpoint sEndpoint = null;
		FtpEndpoint<?> eEndpoint = null;
		try {
			sEndpoint = camelConfig.getCamelContext().getEndpoint("file:src/test/java/com/ibk/kolumbus/file/source", FileEndpoint.class);
			sEndpoint.setFileName("hello.txt");
			sEndpoint.setPreMove("done/${file:name}");
			
			eEndpoint = camelConfig.getCamelContext()
					.getEndpoint("ftp://ftp_admin@10.104.162.84:21000/test?password=1234", FtpEndpoint.class);
			eEndpoint.setFtpClient(new FTPClient());
			eEndpoint.setDisconnect(true);
			
			sEndpoint.start();
			eEndpoint.start();
			
			Exchange source = camelConfig.getConsumerTemplate().receive(sEndpoint, 3000);
			if (source != null) {
				File sourceFile = source.getIn().getBody(File.class);
				camelConfig.getProducerTemplate().sendBodyAndHeader(eEndpoint, sourceFile, Exchange.FILE_NAME, sourceFile.getName());
			} else {
				System.out.println("========================");
				System.out.println(" EAI FILE To FTP 작업 파일 없음!!! ");
				System.out.println("========================");
			}
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.EAI_FITE_TO_FILE, e.getMessage(), e);
		} finally {
			try { if (sEndpoint != null) { sEndpoint.stop(); } } catch (Exception e) {}
			try { if (eEndpoint != null) { eEndpoint.stop(); } } catch (Exception e) {}
		}
	}
}

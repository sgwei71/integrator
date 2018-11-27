package com.ibkglobal.integrator.engine.bean.eai.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.engine.bean.common.batch.BatchFactory;
import com.ibkglobal.integrator.engine.bean.common.batch.model.BatchModel;
import com.ibkglobal.integrator.engine.bean.common.batch.model.BatchModel.FileModel;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.ism.IsmWorkInfo;
import com.ibkglobal.message.struct.resource.ResourceEAI;

public class EAIBatchBean {

	@Autowired
	ResourceEAI resourceEai;

	@Autowired
	BatchFactory batchFactory;
	
	private static String tmpIp = "172.18.116.22";

	public void execute(Exchange exchange) throws IBKExceptionEAI {
		Message message = exchange.getIn();

		IBKMessage ibkMessage = message.getBody(IBKMessage.class);

		Interface intf = null;

		try {
			intf = resourceEai.getInterface(ibkMessage.getInterfaceId());
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.MESSAGE, "메시지를 찾을 수 없습니다. : " + ibkMessage.getInterfaceId());
		}

		try {
			BatchModel batchModel = modelCreate(ibkMessage.getIsmWorkInfo(), intf);

			// 배치 업무 실행
			batchFactory.getWork(batchModel).process();
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.EAI_BATCH, "배치 오류 : " + e.getMessage());
		}
	}

	public BatchModel modelCreate(IsmWorkInfo ismWorkInfo, Interface intf) {
		BatchModel batchModel = new BatchModel();

		batchModel.setWorkType(intf.getCommon().getAttribute().getProcess().getCode());

		// 배치 모델 타입 생성
		switch (batchModel.getWorkType()) {
		case "4":
//			String sourcePath     = tmpIp + ismWorkInfo.getSendFileName();
//			String targetPath     = tmpIp + ismWorkInfo.getRecvFileName();
//			String sourceFileName = StringUtils.isEmpty(ismWorkInfo.getSendFileName())
//			? intf.getInterfaceType().getSource().getProcessType().getEai().getBatch().getFile2file().getRule()
//			: ismWorkInfo.getSendFileName();
//			String targetFileName = StringUtils.isEmpty(ismWorkInfo.getRecvFileName()) 
//			? intf.getInterfaceType().getTarget().getProcessType().getEai().getBatch().getFile2file().getRule()
//			: ismWorkInfo.getSendFileName();
			
			String[] source = ismWorkInfo.getSendFileName().split("/");
			String[] target = ismWorkInfo.getRecvFileName().split("/");
			
			String sourceFileName = source[source.length - 1];
			String targetFileName = target[target.length - 1];
			
			// IP 임시 하드코딩
			String sourcePath = tmpIp + ismWorkInfo.getSendFileName().replace(sourceFileName, "");
			String targetPath = tmpIp + ismWorkInfo.getRecvFileName().replace(targetFileName, "");

			// File To File
			batchModel.setSource(new FileModel(EndpointCode.FILE + sourcePath, sourceFileName));
			batchModel.setTarget(new FileModel(EndpointCode.FILE + targetPath, targetFileName));
			
			break;
		default:
			break;
		}

		return batchModel;
	}
}

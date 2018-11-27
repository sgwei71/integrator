package com.ibkglobal.integrator.engine.builder.route.fep.custom;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class FEPInboundE extends RouteCreateDefault {

	@Override
	// FEP 타발 업무
	public void create() {
		
		from("direct:SIM0.SIM0.EROUTE")		
		// 타발 수신
		.setHeader(ConstantCode.HEADER_ID, Builder.constant("KFKF_KFTC_ECommonHeader"))
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				String data = "11111111111111111111120180987654321200KRIB";
				
				exchange.getIn().setBody(data.getBytes());
			}
		})
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.ProcessPreFEP.class, "preProcess")
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.MappingFEP.class, "doMapping")
		.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_RECOVERY_YN, Builder.constant("Y"))
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.ProcessAfterFEP.class, "afterProcess")
		.setHeader(ConstantCode.COMPOSING_HEADER, Builder.constant("http"))
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.ComposingFEP.class, "composing")
		
		.to("direct:SIM0.SIM0.HC8090")
		// 타발 송신
		.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, Builder.constant("Y"))
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.ProcessPreFEP.class, "preProcess")
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.MappingFEP.class, "doMapping")
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.ProcessAfterFEP.class, "afterProcess")
		.bean(com.ibkglobal.integrator.engine.bean.fep.common.ComposingFEP.class, "composing")
		;
	}
}

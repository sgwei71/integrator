package com.ibkglobal.integrator.engine.builder;

import org.springframework.stereotype.Component;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreate;
import com.ibkglobal.integrator.engine.builder.route.api.ApiRestAdapterInboundTemplate;
import com.ibkglobal.integrator.engine.builder.route.api.ApiDefaultAdapterInboundTemplate;
import com.ibkglobal.integrator.engine.builder.route.api.ApiDefaultAdapterOutboundTemplate;
import com.ibkglobal.integrator.engine.builder.route.api.ApiDefaultLogicTemplate;
import com.ibkglobal.integrator.engine.builder.route.api.RestDefaultAdapterIn;
import com.ibkglobal.integrator.engine.builder.route.api.RestDefaultAdapterOut;
import com.ibkglobal.integrator.engine.builder.route.api.RestInbound;
import com.ibkglobal.integrator.engine.builder.route.eai.EAIBatchAdapterIn;
import com.ibkglobal.integrator.engine.builder.route.eai.EAIDefaultAdapterIn;
import com.ibkglobal.integrator.engine.builder.route.eai.EAIDefaultAdapterOut;
import com.ibkglobal.integrator.engine.builder.route.eai.EAIInbound;
import com.ibkglobal.integrator.engine.builder.route.fep.FEPDefaultAdapterIn;
import com.ibkglobal.integrator.engine.builder.route.fep.FEPDefaultAdapterOut;
import com.ibkglobal.integrator.engine.builder.route.fep.FEPLocalAdapterOut;
import com.ibkglobal.integrator.engine.builder.route.mca.MCADefaultAdapterOut;
import com.ibkglobal.integrator.engine.builder.route.mca.MCAHealthCheck;
import com.ibkglobal.integrator.engine.builder.route.mca.MCAInbound;
import com.ibkglobal.integrator.engine.builder.route.mca.MCALocalAdapterOut;
import com.ibkglobal.integrator.engine.builder.route.mca.bid.MCABidAdapter;
import com.ibkglobal.integrator.engine.builder.route.mca.bid.MCABidProcess;

@Component
public class RouteCreateFactory {

	public RouteCreate getCreate(RouteCreateInfo builderInfo) {

		// MCA
		if (builderInfo.getRouteType() == RouteType.MCA_DEFAULT_ADAPTER_IN) {
			return new ApiDefaultAdapterInboundTemplate(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.MCA_DEFAULT_ADAPTER_OUT) {
			return new MCADefaultAdapterOut(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.MCA_INBOUND) {
			return new MCAInbound(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.MCA_BID_PROCESS) {
			return new MCABidProcess(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.MCA_HEALTH_CHECK) {
			return new MCAHealthCheck(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.MCA_BID_ADAPTER) {
			return new MCABidAdapter(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.MCA_LOCAL_ADAPTER_OUT) {
			return new MCALocalAdapterOut(builderInfo);
		}

		// FEP
		else if (builderInfo.getRouteType() == RouteType.FEP_DEFAULT_ADAPTER_IN) {
			return new FEPDefaultAdapterIn(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.FEP_DEFAULT_ADAPTER_OUT) {
			return new FEPDefaultAdapterOut(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.FEP_LOCAL_ADAPTER_OUT) {
			return new FEPLocalAdapterOut(builderInfo);
		}

		// EAI
		else if (builderInfo.getRouteType() == RouteType.EAI_DEFAULT_ADAPTER_IN) {
			return new EAIDefaultAdapterIn(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.EAI_DEFAULT_ADAPTER_OUT) {
			return new EAIDefaultAdapterOut(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.EAI_INBOUND) {
			return new EAIInbound(builderInfo);
		} else if (builderInfo.getRouteType() == RouteType.EAI_BATCH_ADAPTER_IN) {
			return new EAIBatchAdapterIn(builderInfo);
		}
		//API
		else if(builderInfo.getRouteType() == RouteType.API_REST_ADAPTER_IN) {
			return new ApiRestAdapterInboundTemplate(builderInfo);
		}else if(builderInfo.getRouteType() == RouteType.API_DEFAULT_ADAPTER_IN) {
			return new ApiDefaultAdapterInboundTemplate(builderInfo);
		}else if(builderInfo.getRouteType() == RouteType.API_INBOUND) {
			return new ApiDefaultLogicTemplate(builderInfo);
		}else if(builderInfo.getRouteType() == RouteType.API_DEFAULT_ADAPTER_OUT) {
			return new ApiDefaultAdapterOutboundTemplate(builderInfo);
		}

		// CUSTOM
		else if (builderInfo.getRouteType() == RouteType.CUSTOM) {
			try {
				RouteCreate routeCreate = (RouteCreate) Class.forName(builderInfo.getClassName()).newInstance();
				routeCreate.setRouteCreateInfo(builderInfo);
				routeCreate.create();

				return routeCreate;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}

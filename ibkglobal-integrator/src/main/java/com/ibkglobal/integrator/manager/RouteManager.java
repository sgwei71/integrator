package com.ibkglobal.integrator.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.camel.Route;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.manager.instance.InstanceAdmin;
import com.ibkglobal.integrator.manager.instance.InstanceRouteType;
import com.ibkglobal.integrator.manager.model.RouteInfo;
import com.ibkglobal.integrator.manager.model.RouteStat;
import com.ibkglobal.integrator.util.ConsumerUtil;

import lombok.Getter;
import lombok.Setter;

public class RouteManager {

	@Getter
	CamelConfig camelConfig;

	RouteProperties routeProperties;

	@Getter
	@Setter
	private InstanceAdmin instanceAdmin;

	public RouteManager(CamelConfig camelConfig, RouteProperties routeProperties, InstanceAdmin instanceAdmin) {
		this.camelConfig = camelConfig;
		this.routeProperties = routeProperties;
		this.instanceAdmin = instanceAdmin;
	}

	public void init() throws Exception {
		// 업무별 라우터 정보 초기화
		instanceAdmin.init(routeProperties);
		// 카멜 정보 초기화
		camelConfig.init(routeProperties);
		// 라우터 정보 CamelContext 등록
		initBuilder();
	}

	public void initBuilder() {
		instanceAdmin.getAllRouteInfo().stream().forEach(routeInfo -> {
			try {
				if (routeInfo.getRouteCreate().get() instanceof Collection) {
					camelConfig.getCamelContext().addRouteDefinitions(routeInfo.getRouteCreate().get());
				} else {
					camelConfig.getCamelContext().addRouteDefinition(routeInfo.getRouteCreate().get());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void start(String routeId) {
		try {
			camelConfig.getCamelContext().startRoute(routeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop(String routeId) {
		try {
			camelConfig.getCamelContext().stopRoute(routeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean remove(String routeId) {
		try {
			camelConfig.getCamelContext().stopRoute(routeId);
			System.out.println("--- 종료 확인 ---");
			boolean result = camelConfig.getCamelContext().removeRoute(routeId);
			System.out.println("--- 삭제 확인 ---");

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public void allStart() {
		try {
			camelConfig.getCamelContext().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void allStop() {
		try {
			camelConfig.getCamelContext().stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addBuilder(RouteInfo routeInfo) {
		instanceAdmin.addRouteInfo(routeInfo);

		try {
			if (routeInfo.getRouteCreate().get() instanceof Collection) {
				camelConfig.getCamelContext().addRouteDefinitions(routeInfo.getRouteCreate().get());
			} else {
				camelConfig.getCamelContext().addRouteDefinition(routeInfo.getRouteCreate().get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeBuilder(RouteInfo routeInfo) {
		instanceAdmin.deleteRouteInfo(routeInfo);

		try {
			if (routeInfo.getRouteCreate().get() instanceof Collection) {
				camelConfig.getCamelContext().removeRouteDefinitions(routeInfo.getRouteCreate().get());
			} else {
				camelConfig.getCamelContext().removeRouteDefinition(routeInfo.getRouteCreate().get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get RouteInfo
	 * 
	 * @param String
	 *            routeId
	 * @return RouteInfo
	 */
	public RouteInfo getRouteInfo(String routeId) {
		return instanceAdmin.getRouteInfo(routeId);
	}

	/**
	 * Get All RouteInfo
	 * 
	 * @return
	 */
	public List<RouteInfo> getAllRouteInfo() {
		return instanceAdmin.getAllRouteInfo();
	}

	public List<RouteInfo> getAllRouteInfo(InstanceRouteType instanceRouteType) {
		return instanceAdmin.getAllRouteInfo(instanceRouteType);
	}

	public List<RouteStat> routeStatusList() {
		List<RouteStat> infos = new ArrayList<>();

		List<RouteDefinition> rds = camelConfig.getCamelContext().getRouteDefinitions();

		rds.forEach(rd -> {

			RouteStat info = new RouteStat();

			info.setRouteId(rd.getId());
			info.setGroup(rd.getGroup());
			info.setDescription(rd.getDescriptionText());

			StringBuffer input = new StringBuffer();
			rd.getInputs().forEach(fd -> {
				if (!StringUtils.isEmpty(input.toString())) {
					input.append(",");
				}
				input.append(fd.getUri());
			});
			info.setFrom(input.toString());

			info.setStarted(rd.getStatus(camelConfig.getCamelContext()).isStarted());
			info.setStopped(rd.getStatus(camelConfig.getCamelContext()).isStopped());

			String[] outputs = new String[rd.getOutputs().size()];
			int i = 0;
			for (ProcessorDefinition<?> pd : rd.getOutputs()) {
				outputs[i++] = pd.toString();
			}
			info.setOutputs(outputs);

			Route route = camelConfig.getCamelContext().getRoute(rd.getId());
			if (route != null) {
				ConsumerUtil.consumerInfoSet(info, route.getConsumer());
			}

			infos.add(info);
		});

		return infos;
	}
}

package com.ibkglobal.integrator.api.rest.reportmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibkglobal.integrator.api.com.exception.ApiException;
import com.ibkglobal.integrator.api.com.model.ResultResponse;
import com.ibkglobal.integrator.api.rest.reportmanager.service.ReportRouterService;

@RestController
@RequestMapping("/integrator/1.0/reportmanager")
public class ReportRouterController {
	
	@Autowired
	ReportRouterService reportRouteService;
	
	/**
	 * Get Report Adapter
	 * @return ResultResponse
	 * @throws Exception
	 */
	@GetMapping("/routerList")
	public ResultResponse getReportAdapter() throws Exception {
		
		try {
			return new ResultResponse(reportRouteService.getReportRouteList());
		} catch (Exception ex) {
			throw new ApiException(ex.getMessage());
		}
	}
	
	/**
	 * Get Report Adapter Detail
	 * @param id
	 * @return ResultResponse
	 * @throws Exception
	 */
	@GetMapping("/reportroutedetail/{id}")
	public ResultResponse getReportAdapterDetail(@PathVariable String id) throws Exception {	
		
		try {
			return new ResultResponse(reportRouteService.getReportRouteDetail(id));
		} catch (Exception ex) {
			throw new ApiException(ex.getMessage());
		}
		
//		if (result == null) {
//			return new ResultResponse(false, "DATA가 없습니다.");
//		} else {
//			return new ResultResponse(result);
//		}
	}
}

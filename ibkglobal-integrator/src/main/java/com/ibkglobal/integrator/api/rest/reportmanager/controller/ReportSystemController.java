package com.ibkglobal.integrator.api.rest.reportmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibkglobal.integrator.api.com.exception.ApiException;
import com.ibkglobal.integrator.api.com.model.ResultResponse;
import com.ibkglobal.integrator.api.rest.reportmanager.service.ReportSystemService;

@RestController
@RequestMapping("/integrator/1.0/reportmanager")
public class ReportSystemController {
	
	@Autowired
	ReportSystemService reportSystemService;
	
	@GetMapping("/reportsystem")
	public ResultResponse getReportSystem() throws Exception {
		try {
			return new ResultResponse(reportSystemService.getReportSystem());
		} catch (Exception ex) {
			throw new ApiException(ex.getMessage());
		}
	}
}

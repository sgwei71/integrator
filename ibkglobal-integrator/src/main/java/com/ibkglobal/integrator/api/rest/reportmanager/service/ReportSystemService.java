package com.ibkglobal.integrator.api.rest.reportmanager.service;

import org.springframework.stereotype.Service;

import com.ibkglobal.integrator.api.rest.SystemUtil;
import com.ibkglobal.integrator.api.rest.reportmanager.model.ReportSystem;

@Service
public class ReportSystemService {
	
	public ReportSystem getReportSystem() throws Exception {		
		return SystemUtil.getReportSystem();
	}
}

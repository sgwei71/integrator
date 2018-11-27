package com.ibkglobal.integrator.api.rest.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibkglobal.integrator.api.com.exception.ApiException;
import com.ibkglobal.integrator.api.com.model.ResultResponse;
import com.ibkglobal.integrator.api.rest.manager.service.ManagerAdapterService;

@RestController
@RequestMapping("/integrator/1.0/manager")
public class ManagerAdapterController {
	
	@Autowired
	ManagerAdapterService managerAdapterService;
	
	@PostMapping("/reportAdapter/test")
	public String resultTest(@RequestBody String data) {
		System.out.println("데이터 수신");
		return "change Data";
	}
	
	@GetMapping("/reportAdapter/start/{routeId}")
	public ResultResponse adapterStart(@PathVariable String routeId) throws Exception {
		try {
			return new ResultResponse(managerAdapterService.adapterStart(routeId));
		} catch (Exception ex) {
			throw new ApiException(ex.getMessage());
		}
	}
	
	@GetMapping("/reportAdapter/stop/{routeId}")
	public ResultResponse adapterStop(@PathVariable String routeId) throws Exception {
		try {
			return new ResultResponse(managerAdapterService.adapterStop(routeId));
		} catch (Exception ex) {
			throw new ApiException(ex.getMessage());
		}
	}
	
	@GetMapping("/reportAdapter/remove/{routeId}")
	public ResultResponse adapterRemove(@PathVariable String routeId) throws Exception {
		try {
			return new ResultResponse(managerAdapterService.adapterRemove(routeId));
		} catch (Exception ex) {
			throw new ApiException(ex.getMessage());
		}
	}
}

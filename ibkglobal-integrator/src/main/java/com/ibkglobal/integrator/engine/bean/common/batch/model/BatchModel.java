package com.ibkglobal.integrator.engine.bean.common.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BatchModel {
	
	private String    workType;
	private WorkModel source;
	private WorkModel target;
	private long      timeOut = 30000; // Default 30초
	
	@Data
	public static class WorkModel {}
	
	@Data
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper=false)
	public static class FileModel extends WorkModel {
		private String path;
		private String fileName;
	}
	
	@Data
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper=false)
	public static class FtpModel extends WorkModel {
		private String user;
		private String password;
		private String path;
	}
	
	@Data
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper=false)
	public static class DbModel extends WorkModel {
		
	}
}

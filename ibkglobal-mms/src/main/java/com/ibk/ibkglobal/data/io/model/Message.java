package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

	private String			name;		// 식별자명
	private String			logKey;
	private String			traceKey;
	private List<IoElement>	sequence;	// header, unit
}

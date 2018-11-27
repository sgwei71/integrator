package com.ibkglobal.message.struct.loader.model;

import java.util.List;

import lombok.Data;

@Data
public class MessageLoader {

	private String				modified;
	private List<MessageInfo>	messageInfo;
}
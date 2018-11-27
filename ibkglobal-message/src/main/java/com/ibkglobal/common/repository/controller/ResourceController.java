package com.ibkglobal.common.repository.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.message.struct.resource.ResourceFEP;
import com.ibkglobal.message.struct.resource.ResourceMCA;

@RestController
public class ResourceController {

	@Autowired
	ResourceMCA resourceMCA;
	
	@Autowired
	ResourceFEP resourceFEP;
	
	@Autowired
	ResourceMCA resourceEAI;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@RequestMapping(value = "/resource/find/{type}/{id}", method = {RequestMethod.GET, RequestMethod.POST})
	public String find(@PathVariable String type, @PathVariable String id) throws JsonProcessingException {
		
		StringBuffer sb = new StringBuffer();
		Interface interface1 = null;
		switch(type.toUpperCase()) {
		case "MCA" :
			interface1 = resourceMCA.getInterface(id);
			if (interface1 != null) {
				sb.append("====== MCA Interface ======<br />");
				sb.append(objectMapper.writeValueAsString(interface1));
				sb.append("<br /><br />");
				
				sb.append("====== MCA Source Io IN BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceMCA.getSourceIo(interface1.getIntfId(), "IN")));
				sb.append("<br /><br />");
				
				sb.append("====== MCA Source Io OUT BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceMCA.getSourceIo(interface1.getIntfId(), "OUT")));
				sb.append("<br /><br />");
				
				sb.append("====== MCA Target Io IN BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceMCA.getTargetIo(interface1.getIntfId(), "IN")));
				sb.append("<br /><br />");
				
				sb.append("====== MCA Target Io OUT BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceMCA.getTargetIo(interface1.getIntfId(), "OUT")));
				sb.append("<br /><br />");
				
				sb.append("====== MCA Mapping List IN BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceMCA.getMappingList(interface1.getIntfId(), "IN")));
				sb.append("<br /><br />");
				
				sb.append("====== MCA Mapping List OUT BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceMCA.getMappingList(interface1.getIntfId(), "OUT")));
				sb.append("<br /><br />");
			}
			break;
		case "FEP" :
			interface1 = resourceFEP.getInterface(id);
			if (interface1 != null) {
				sb.append("====== FEP Interface ======<br />");
				sb.append(objectMapper.writeValueAsString(interface1));
				sb.append("<br /><br />");
				
				sb.append("====== FEP Mapping ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getMappingFEP().get(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
				
				sb.append("====== FEP Message ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getMessageFEP().get(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
				
				sb.append("====== FEP Source Header ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getSourceHeader(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
				
				sb.append("====== FEP Source Header Entity ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getSourceHeaderEntity(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
				
				sb.append("====== FEP Source Body Entity ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getSourceBodyEntity(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
				
				sb.append("====== Target Header ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getTargetHeader(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
				
				sb.append("====== FEP Target Header Entity ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getTargetHeaderEntity(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
				
				sb.append("====== FEP Target Body Entity ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceFEP.getTargetBodyEntity(interface1.getIntfIdnNm())));
				sb.append("<br /><br />");
			}
			break;
		case "EAI" :
			interface1 = resourceEAI.getInterface(id);
			if (interface1 != null) {
				sb.append("====== EAI Interface ======<br />");
				sb.append(objectMapper.writeValueAsString(interface1));
				sb.append("<br /><br />");
				
				sb.append("====== EAI Source Io IN BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceEAI.getSourceIo(interface1.getIntfId(), "IN")));
				sb.append("<br /><br />");
				
				sb.append("====== EAI Source Io OUT BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceEAI.getSourceIo(interface1.getIntfId(), "OUT")));
				sb.append("<br /><br />");
				
				sb.append("====== EAI Target Io IN BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceEAI.getTargetIo(interface1.getIntfId(), "IN")));
				sb.append("<br /><br />");
				
				sb.append("====== EAI Target Io OUT BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceEAI.getTargetIo(interface1.getIntfId(), "OUT")));
				sb.append("<br /><br />");
				
				sb.append("====== EAI Mapping List IN BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceEAI.getMappingList(interface1.getIntfId(), "IN")));
				sb.append("<br /><br />");
				
				sb.append("====== EAI Mapping List OUT BOUND ======<br />");
				sb.append(objectMapper.writeValueAsString(resourceEAI.getMappingList(interface1.getIntfId(), "OUT")));
				sb.append("<br /><br />");
			}
			break;
		}
		
		return sb.toString();
	}
}

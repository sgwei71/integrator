package com.ibkglobal.integrator.tc;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.HeaderCacheFEP;
import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibk.ibkglobal.data.io.MessageCacheFEP;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.message.struct.resource.ResourceFEP;

@Component
public class SchemaFepTest {

	@Autowired
	ResourceFEP resourceFep;
	
	/**
	 * FEP 인터페이스
	 * @param id
	 * @return
	 */
	public Interface getInterface(String id) {
		return resourceFep.getInterface(id);
	}
	
	/**
	 * FEP 인터페이스(전문 명으로 인터페이스 아이디 찾는용)
	 * @param externalKey
	 * @return
	 */
	public String getInterfaceKey(String externalKey) {
		return resourceFep.getInterfaceID(externalKey);
	}
	
	/**
	 * FEP Message(IO 전체 관리)
	 * @param key
	 * @return
	 */
	public MessageCacheFEP getMessage(String key) {
		return resourceFep.getMessageFEP().get(key);
	}
	
	/**
	 * FEP 소스 헤더 기본정보
	 * @param id
	 * @return
	 */
	public HeaderCacheFEP getSourceHeader(String id) {
		return resourceFep.getSourceHeader(id);
	}
	
	/**
	 * FEP 소스 헤더 엔티티정보
	 * @param id
	 * @return
	 */
	public LinkedList<IoCacheFEP> getSourceHeaderEntity(String id) {
		return resourceFep.getSourceHeaderEntity(id);
	}
	
	/**
	 * FEP 소스 바디 엔티티정보
	 * @param id
	 * @return
	 */
	public IoCacheFEP getSourceBodyEntity(String id) {
		return resourceFep.getSourceBodyEntity(id);
	}
	
	/**
	 * FEP 타겟 헤더 기본정보
	 * @param id
	 * @return
	 */
	public HeaderCacheFEP getTargetHeader(String id) {
		return resourceFep.getTargetHeader(id);
	}
	
	/**
	 * FEP 타겟 헤더 엔티티정보
	 * @param id
	 * @return
	 */
	public LinkedList<IoCacheFEP> getTargetHeaderEntity(String id) {
		return resourceFep.getTargetHeaderEntity(id);
	}
	
	/**
	 * FEP 타겟 바디 엔티티정보
	 * @param id
	 * @return
	 */
	public IoCacheFEP getTargetBodyEntity(String id) {
		return resourceFep.getTargetBodyEntity(id);
	}
	
	/**
	 * FEP 매핑 정보
	 * @param id
	 * @return
	 */
	public List<TlgrMapping> getMapping(String id) {
		return resourceFep.getMappingList(id);
	}
}

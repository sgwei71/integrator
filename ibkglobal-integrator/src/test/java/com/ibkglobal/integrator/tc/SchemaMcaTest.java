package com.ibkglobal.integrator.tc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.message.struct.resource.ResourceMCA;

@Component
public class SchemaMcaTest {
	
	@Autowired
	ResourceMCA resourceMca;
	
	/**
	 * MCA 인터페이스 정보
	 * @param id
	 * @return
	 */
	public Interface getInterface(String id) {
		return resourceMca.getInterface(id);
	}
	
	/**
	 * MCA 소스 IO
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheMCA getSourceIo(String id, String type) {
		return resourceMca.getSourceIo(id, type);
	}
	
	/**
	 * MCA 타겟 IO
	 * @param id
	 * @param type
	 * @return
	 */
	public IoCacheMCA getTargetIo(String id, String type) {
		return resourceMca.getTargetIo(id, type);
	}
	
	/**
	 * MCA 매핑 정보
	 * @param id
	 * @param type
	 * @return
	 */
	public List<TlgrMapping> getMapping(String id, String type) {
		return resourceMca.getMappingList(id, type);
	}
}

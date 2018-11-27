package com.ibkglobal.message.struct.resource;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.HeaderCacheFEP;
import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibk.ibkglobal.data.io.MessageCacheFEP;
import com.ibk.ibkglobal.data.io.model.IoElement;
import com.ibk.ibkglobal.data.io.model.Header;
import com.ibk.ibkglobal.data.mapp.Mapping;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.common.file.FileType;
import com.ibkglobal.common.repository.CacheFactory;
import com.ibkglobal.common.repository.CacheType;
import com.ibkglobal.message.struct.header.HeaderFEP;
import com.ibkglobal.message.struct.io.IoFEP;
import com.ibkglobal.message.struct.itface.InterfaceFEP;
import com.ibkglobal.message.struct.mapping.MappingFEP;
import com.ibkglobal.message.struct.message.MessageFEP;

import lombok.Getter;
import lombok.Setter;

@Component
public class ResourceFEP extends ResourceDefault {

	@Getter @Setter
	InterfaceFEP interfaceFEP;
	
	@Getter @Setter
	MappingFEP mappingFEP;
	
	@Getter @Setter
	MessageFEP messageFEP;
	
	@Getter @Setter
	HeaderFEP headerFEP;
	
	@Getter @Setter
	IoFEP ioFEP;
	
	@Autowired
	CacheFactory cacheFactory;
	
	@Autowired
	FileReader fileReader;
	
	@Override
	public void init(String type) throws Exception {
		this.type = type;
		
		interfaceFEP = new InterfaceFEP();
		mappingFEP = new MappingFEP();
		messageFEP = new MessageFEP();
		headerFEP = new HeaderFEP();
		ioFEP = new IoFEP();
		
		load();
	}

	@Override
	public void load() throws Exception {
		
		interfaceFEP.setType(type);
		mappingFEP.setType(type);
		messageFEP.setType(type);
		headerFEP.setType(type);
		ioFEP.setType(type);
		
		if ("file".equals(type)) {
			interfaceFEP.initLoad(fileReader.readFileToObject(FileType.FEP_INTERFACE_LOADER), fileReader);
			mappingFEP.initLoad(fileReader.readFileToObject(FileType.FEP_MAPPING_LOADER), fileReader);
			messageFEP.initLoad(fileReader.readFileToObject(FileType.FEP_MESSAGE_LOADER), fileReader);
			headerFEP.initLoad(fileReader.readFileToObject(FileType.FEP_HEADER_LOADER), fileReader);
			ioFEP.initLoad(fileReader.readFileToObject(FileType.FEP_IO_LOADER), fileReader);
			
			toPrint();
		} else if ("cache".equals(type)) {
			interfaceFEP.init(cacheFactory.getCacheRepository(CacheType.IBK_FEP_INTERFACE_EXT_INFO), cacheFactory.getCacheRepository(CacheType.IBK_FEP_INTERFACE_KEY));
			mappingFEP.init(cacheFactory.getCacheRepository(CacheType.IBK_FEP_MAPPING));
			messageFEP.init(cacheFactory.getCacheRepository(CacheType.IBK_FEP_MESSAGE));
			headerFEP.init(cacheFactory.getCacheRepository(CacheType.IBK_FEP_HEADER_MESSAGE));
			ioFEP.init(cacheFactory.getCacheRepository(CacheType.IBK_FEP_UNIT));
		}
	}	
	
	/**
	 * Get Interface
	 * @param id
	 * @return
	 */
	public Interface getInterface(String id) {
		return interfaceFEP.get(id);
	}
	
	/**
	 * Get Interface ID
	 * @param externalKey : 업무_기관_종별_거래구분
	 * @return: interfaceId
	 */
	public String getInterfaceID(String externalKey) {
		return interfaceFEP.getInterfaceID(externalKey);
	}
	
	/**
	 * Get Source Header Entity
	 * @param id
	 * @return 
	 */
	public LinkedList<IoCacheFEP> getSourceHeaderEntity(String id) {		
		Interface interface1 = interfaceFEP.get(id);
		Mapping mapping = mappingFEP.get(interface1.getIntfIdnNm());
		MessageCacheFEP message = messageFEP.get(mapping.getSorcInopNm());		
		HeaderCacheFEP header = message.getData().getSequence().stream()
				.filter(data -> "HEADER".equals(data.getElementCd()))
				.findFirst()
				.map(map -> headerFEP.get(map.getType()))
				.get();
		
		LinkedList<IoCacheFEP> headerList = new LinkedList<>();
		for (IoElement element : header.getData().getSequence()) {
			headerList.add(ioFEP.get(element.getType()));
		}		
		return headerList;
	}
	
	/**
	 * Get Source Body Entity
	 * @param id
	 * @return
	 */
	public IoCacheFEP getSourceBodyEntity(String id) {
		Interface interface1 = interfaceFEP.get(id);
		Mapping mapping = mappingFEP.get(interface1.getIntfIdnNm());
		MessageCacheFEP message = messageFEP.get(mapping.getSorcInopNm());		
		return message.getData().getSequence().stream()
				.filter(data -> "ENTITY".equals(data.getElementCd()))
				.findFirst()
				.map(map -> ioFEP.get(map.getType()))
				.get();
	}
	
	/**
	 * Get Target Header Entity
	 * @param id
	 * @return
	 */
	public LinkedList<IoCacheFEP> getTargetHeaderEntity(String id) {
		Interface interface1 = interfaceFEP.get(id);
		Mapping mapping = mappingFEP.get(interface1.getIntfIdnNm());
		MessageCacheFEP message = messageFEP.get(mapping.getTgtInopNm());		
		HeaderCacheFEP header = message.getData().getSequence().stream()
				.filter(data -> "HEADER".equals(data.getElementCd()))
				.findFirst()
				.map(map -> headerFEP.get(map.getType()))
				.get();
		
		LinkedList<IoCacheFEP> headerList = new LinkedList<>();
		for (IoElement element : header.getData().getSequence()) {
			headerList.add(ioFEP.get(element.getType()));
		}		
		return headerList;
	}
	
	/**
	 * Get Target Body Entity
	 * @param id
	 * @return
	 */
	public IoCacheFEP getTargetBodyEntity(String id) {
		Interface interface1 = interfaceFEP.get(id);
		Mapping mapping = mappingFEP.get(interface1.getIntfIdnNm());
		MessageCacheFEP message = messageFEP.get(mapping.getTgtInopNm());		
		return message.getData().getSequence().stream()
				.filter(data -> "ENTITY".equals(data.getElementCd()))
				.findFirst()
				.map(map -> ioFEP.get(map.getType()))
				.get();
	}
	
	/**
	 * Get Mapping List
	 * @param id
	 * @return
	 */
	public List<TlgrMapping> getMappingList(String id) {
		Interface interface1 = interfaceFEP.get(id);
		return mappingFEP.get(interface1.getIntfIdnNm()).getMappingList();
	}
	
	/**
	 * Get Source Header
	 * @param id
	 * @return
	 */
	public HeaderCacheFEP getSourceHeader(String id) {
		Interface interface1 = interfaceFEP.get(id);
		Mapping mapping = mappingFEP.get(interface1.getIntfIdnNm());
		MessageCacheFEP message = messageFEP.get(mapping.getSorcInopNm());		
		return message.getData().getSequence().stream()
				.filter(data -> "HEADER".equals(data.getElementCd()))
				.findFirst()
				.map(map -> headerFEP.get(map.getType()))
				.get();
	}
	
	/**
	 * Get Target Header
	 * @param id
	 * @return
	 */
	public HeaderCacheFEP getTargetHeader(String id) {
		Interface interface1 = interfaceFEP.get(id);
		Mapping mapping = mappingFEP.get(interface1.getIntfIdnNm());
		MessageCacheFEP message = messageFEP.get(mapping.getTgtInopNm());		
		return message.getData().getSequence().stream()
				.filter(data -> "HEADER".equals(data.getElementCd()))
				.findFirst()
				.map(map -> headerFEP.get(map.getType()))
				.get();
	}
		
	/**
	 * Get TraceKey
	 * @param id
	 * @param data
	 * @return
	 */
	public String getTraceKey(String id, Map<String, Object> data) {
		Interface interface1 = interfaceFEP.get(id);
		MessageCacheFEP message = messageFEP.get(interface1.getInterfaceType().getSource().getInopIdnNm());
		return (!StringUtils.isEmpty(message.getData().getTraceKey())) ? expression(data, message.getData().getTraceKey()) : "";
	}
	
	/**
	 * Get MsgKey
	 * @param id
	 * @param data
	 * @return
	 */
	public String getMsgKey(String id, Map<String, Object> data) {
		Header header = getSourceHeader(id).getData();
		return (!StringUtils.isEmpty(header.getMsgKey())) ? expression(data, header.getMsgKey()) : "";
	}
	
	/**
	 * Get WorkCode
	 * @param id
	 * @param data
	 * @return
	 */
	public String getWorkCode(String id, Map<String, Object> data) {
		Header header = getSourceHeader(id).getData();
		return (!StringUtils.isEmpty(header.getWorkCode())) ? expression(data, header.getWorkCode()) : "";
	}
	
	/**
	 * Get WorkCode
	 * @param header
	 * @param data
	 * @return
	 */
	public String getWorkCode(Header header, Map<String, Object> data) {
		return (!StringUtils.isEmpty(header.getWorkCode())) ? expression(data, header.getWorkCode()) : "";
	}
	
	/**
	 * Get ErrCode
	 * @param id
	 * @param data
	 * @return
	 */
	public String getErrCode(String id, Map<String, Object> data) {
		Header header = getSourceHeader(id).getData();
		return (!StringUtils.isEmpty(header.getErrCdCon())) ? expression(data, header.getErrCdCon()) : "";
	}
	
	/**
	 * Get LogKey
	 * @param id
	 * @param data
	 * @return
	 */
	public Object[] getLogKey(String id, Map<String, Object> data) {
		Interface interface1 = interfaceFEP.get(id);
		MessageCacheFEP message = messageFEP.get(interface1.getInterfaceType().getSource().getInopIdnNm());
		if ((!StringUtils.isEmpty(message.getData().getLogKey()))) {
			String[] logKeys = message.getData().getLogKey().split(";");					
			Object[] objs = new Object[logKeys.length];					
			int i = 0;
			
			for (String logKey : logKeys) {
				objs[i++] = expression(data, logKey);
			}
			return objs;
		}
		return null;
	}
	
	/**
	 * Message Expression
	 * @param data
	 * @param express
	 * @return
	 */
	private String expression(Map<String, Object> data, String express) {
					
		CharSequence s = express;
		StringBuffer n = new StringBuffer();
		StringBuffer f = new StringBuffer();
		StringBuffer r = new StringBuffer();
		StringBuffer b = new StringBuffer();
		int[] substring = new int[2];

		boolean isNum = false;
		boolean isChr = false;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '.') {
				f.setLength( 0 ) ;
			} else if (c == '(') {					
				isNum = true;
				substring = new int[2];
				n = new StringBuffer();
			} else if (c == ',') {					
				if (isNum) substring[0] = new Integer(n.toString()).intValue();			
				n = new StringBuffer();
			} else if (c == ')') {
				isNum = false;
				substring[1] = new Integer(n.toString()).intValue();

				String fldName = f.toString() ;

				b.append( String.valueOf( data.get( fldName ) ).trim().substring(substring[0], substring[1]));
				//b.append( String.valueOf( data.get( fldName ) ).substring(substring[0], substring[1]));
				f = new StringBuffer();
			} else if (c == '+') {
				if (f.length() > 0)	b.append( String.valueOf( data.get( f.toString() ) ).trim());
				
				f = new StringBuffer();
				r = new StringBuffer();
			} else if (c == '\'') {
				if (isChr == false) {
					isChr = true;
				} else {
					b.append(r.toString().trim());
					isChr = false;
				}
			} else if (c == ' ') {
				
			} else {
				if (isNum) n.append(c);
				else if (isChr) r.append(c);
				else f.append(c);
			}
		}

		if (data != null && f.length() > 0) {
			b.append(String.valueOf( data.get( f.toString() ) ).trim());
		}				
		return b.toString().trim();
	}
	
	public void toPrint() {
		StringBuilder sb = new StringBuilder();
		{
			sb.append("- " + getClass().getSimpleName() + " interface FEP Data LIST -\n");
			for (String key : interfaceFEP.getDataList().keySet()) {
				sb.append(key + " : " + interfaceFEP.getDataList().get(key) + "\n");
			}
			
			sb.append("\n");			
			sb.append("- " + getClass().getSimpleName() + " interface Idn LIST -\n");
			for (String key : interfaceFEP.getIdnMap().keySet()) {
				sb.append(key + " : " + interfaceFEP.getIdnMap().get(key) + "\n");
			}
		}
		
		sb.append("\n");
		{
			sb.append("- " + getClass().getSimpleName() + " mappingFEP FEP Data LIST -\n");
			for (String key : mappingFEP.getDataList().keySet()) {
				sb.append(key + " : " + mappingFEP.getDataList().get(key) + "\n");
			}
		}
		
		sb.append("\n");
		{
			sb.append("- " + getClass().getSimpleName() + " messageFEP FEP Data LIST -\n");
			for (String key : messageFEP.getDataList().keySet()) {
				sb.append(key + " : " + messageFEP.getDataList().get(key) + "\n");
			}
		}
		
		sb.append("\n");
		{
			sb.append("- " + getClass().getSimpleName() + " headerFEP FEP Data LIST -\n");
			for (String key : headerFEP.getDataList().keySet()) {
				sb.append(key + " : " + headerFEP.getDataList().get(key) + "\n");
			}
		}
		
		sb.append("\n");
		{
			sb.append("- " + getClass().getSimpleName() + " ioFEP FEP Data LIST -\n");
			for (String key : ioFEP.getDataList().keySet()) {
				sb.append(key + " : " + ioFEP.getDataList().get(key) + "\n");
			}
		}
		
		//System.out.println(sb.toString());
	}
}

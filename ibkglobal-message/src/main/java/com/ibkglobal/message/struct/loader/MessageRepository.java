package com.ibkglobal.message.struct.loader;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.sf.ehcache.Cache;

public abstract class MessageRepository<Tloader, Tdata> implements Loader<Tloader, Tdata> {
	
	protected String   type;      // 파일 타입
	
	@Getter @Setter
	Tloader            loader;    // 로드
	@Getter @Setter
	Map<String, Tdata> dataList;  // 정보들
	@Getter @Setter
	Cache              cache;     // 캐시정보
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public void init(Cache cache) {
		// 캐시 설정
		setCache(cache);
	}
}

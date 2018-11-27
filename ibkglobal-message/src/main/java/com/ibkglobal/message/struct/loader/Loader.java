package com.ibkglobal.message.struct.loader;

import java.util.Map;

import net.sf.ehcache.Cache;

public interface Loader<Tloader, Tdata> {
	
	public void  setType(String type);                                  // 타입지정
	public void  init(Tloader tloader, Map<String, Tdata> tdataMap); 	// INIT(File)
	public void  init(Cache cache);                                     // INIT(Cache)
	public Tdata get(String key);                                    	// SELECT
	public void  put(String key, Tdata tdata);							// INSERT & UPDATE
	public void  remove(String key);                                 	// REMOVE
}

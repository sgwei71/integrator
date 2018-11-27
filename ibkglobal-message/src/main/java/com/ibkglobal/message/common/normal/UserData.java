/**
 * 
 */
package com.ibkglobal.message.common.normal;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData extends AbstractFlexibleDataSet implements Serializable {

	private LinkedHashMap<String, Object> data;

}

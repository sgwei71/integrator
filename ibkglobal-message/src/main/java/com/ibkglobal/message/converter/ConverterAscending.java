package com.ibkglobal.message.converter;

import java.util.Comparator;

import com.ibk.ibkglobal.data.io.model.Tlgr;

public class ConverterAscending implements Comparator<Tlgr> {

	@Override
	public int compare(Tlgr tlgr1, Tlgr tlgr2) {
		return tlgr1.getTlgrSqc().compareTo(tlgr2.getTlgrSqc());
	}
}

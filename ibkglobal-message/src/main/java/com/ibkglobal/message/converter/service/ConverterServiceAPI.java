package com.ibkglobal.message.converter.service;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.ConverterByte;
import com.ibkglobal.message.converter.ConverterByteAPI;
import com.ibkglobal.message.converter.ConverterList;
import com.ibkglobal.message.converter.ConverterMap;
import com.ibkglobal.message.converter.ConverterMapping;
import com.ibkglobal.message.converter.delimiter.DelimiterToUserData;
import com.ibkglobal.message.converter.delimiter.UserDataToDelimiter;
import com.ibkglobal.message.converter.iso.IsoToUserData;
import com.ibkglobal.message.converter.iso.UserDataToIso;
import com.ibkglobal.message.converter.telegram.FlatToTelegram;
import com.ibkglobal.message.converter.telegram.FlatToTelegramAPI;
import com.ibkglobal.message.converter.telegram.TelegramToFlatAPI;

@Service
@SuppressWarnings("deprecation")
public class ConverterServiceAPI extends ConverterService{
	public byte[] telegramToFlatAPI(StandardTelegram standardTelegram, List<Tlgr> fieldList) throws Exception {
		return TelegramToFlatAPI.telegramToFlatAPI(standardTelegram, fieldList);
	}
	public StandardTelegram flatToTelegramAPI(byte[] data, List<Tlgr> fieldList) throws Exception {
		return FlatToTelegramAPI.flatToTelegramAPI(data, fieldList);
	}
}

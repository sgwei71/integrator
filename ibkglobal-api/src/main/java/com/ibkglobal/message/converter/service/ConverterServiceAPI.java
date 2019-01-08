package com.ibkglobal.message.converter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.message.common.normal.StandardTelegram;
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

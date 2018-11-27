package com.ibkglobal.integrator.log;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.Getter;

/**
 * Log Manager
 */
public class LogManager {

	@Getter
	private static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

	private final static String	rootLogName			= "ROOT";
	private final static String	defaultLogName		= "defaultLog";
	private final static String	exceptionLogName	= "exceptionLog";

	/**
	 * Set Level
	 * @param LogType logType
	 * @param Level level
	 */
	public synchronized static void setLevel(LogType logType, Level level) {
		if (logType == LogType.DYNAMIC) {
			getLogger().setLevel(level);
		} else if (logType == LogType.EXCEPTION) {
			loggerContext.getLogger(exceptionLogName).setLevel(level);
		} else {
			loggerContext.getLogger(rootLogName).setLevel(level);
		}
	}

	/**
	 * Set Level
	 * @param Level level
	 */
	public synchronized static void setLevel(Level level) {
		getLogger().setLevel(level);
	}

	/**
	 * Get Logger
	 * @param LogType logType
	 * @return Logger
	 */
	public static Logger getLogger(LogType logType) {
		if (logType == LogType.DYNAMIC) {
			return getLogger();
		} else if (logType == LogType.EXCEPTION) {
			return loggerContext.getLogger(exceptionLogName);
		}
		return loggerContext.getLogger(rootLogName);
	}

	/**
	 * Get Logger
	 * @return Logger
	 */
	public static Logger getLogger() {
		Logger logger = loggerContext.getLogger(getLoggerName());
		if (logger.getLevel() == null || logger.getAppender(ConstantCode.DYNAMIC_APPENDER) == null) {
			synchronized (logger) {
				logger.addAppender(loggerContext.getLogger(ConstantCode.DYNAMIC_LOGGER)
						.getAppender(ConstantCode.DYNAMIC_APPENDER));
				logger.setLevel(Level.INFO);
			}
		}
		return logger;
	}
	
	/**
	 * Get Logger
	 * @param String name
	 * @return Logger
	 */
	public static Logger getLogger(String name) {
		Logger logger = loggerContext.getLogger(name);
		if (logger.getLevel() == null || logger.getAppender(ConstantCode.DYNAMIC_APPENDER) == null) {
			synchronized (logger) {
				logger.addAppender(loggerContext.getLogger(ConstantCode.DYNAMIC_LOGGER)
						.getAppender(ConstantCode.DYNAMIC_APPENDER));
				logger.setLevel(Level.INFO);
			}
		}
		return logger;
	}

	/**
	 * Get Logger Name
	 * @return String
	 */
	public static String getLoggerName() {
		String name = MDC.get(ConstantCode.LOGGER_KEY);
		if (StringUtils.isEmpty(name)) {
			name = defaultLogName;
		}
		return name;
	}

	/**
	 * Put MDC
	 * @param String name
	 */
	public static void putMdc(String name) {
		MDC.put(ConstantCode.LOGGER_KEY, name);
	}

	/**
	 * Remove MDC
	 * @param String name
	 */
	public static void removeMdc(String name) {
		MDC.remove(name);
	}
}

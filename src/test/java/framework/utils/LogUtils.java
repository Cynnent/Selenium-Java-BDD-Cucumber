package framework.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LogUtils {

	public static void logInfo(Logger logger, String message, Object... params) {
		logger.log(Level.INFO, message, params);
	}

	public static void logDebug(Logger logger, String message, Object... params) {
		logger.log(Level.DEBUG, message, params);
	}

	public static void logError(Logger logger, String message, Object... params) {
		logger.log(Level.ERROR, message, params);
	}

	public static void logWarn(Logger logger, String message, Object... params) {
		logger.log(Level.WARN, message, params);
	}

}

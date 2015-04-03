package fr.stevecohen.util;

public class Logger {
	
	private static boolean 		enabled = true;
	private static boolean 		printLine = true;
	
	public static final int		LOG = 1;
	public static final int		INFO = 2;
	public static final int		WARNING = 4;
	public static final int		ERROR = 8;
	
	private Logger() {
		
	}
	
	public static void log(String message) {
		Logger.log(Logger.LOG, message, false, 1);
	}
	
	public static void log(int level, String message) {
		Logger.log(level, message, false, 1);
	}
	
	public static void log(int level, String message, boolean printStackTrace) {
		Logger.log(level, message, printStackTrace, 1);
	}
	
	public static void warning(String message) {
		Logger.log(Logger.WARNING, message, false, 1);
	}
	
	public static void warning(String message, boolean printStackTrace) {
		Logger.log(Logger.WARNING, message, printStackTrace, 1);
	}
	
	public static void error(String message) {
		Logger.log(Logger.ERROR, message, false, 1);
	}
	
	public static void error(String message, boolean printStackTrace) {
		Logger.log(Logger.ERROR, message, printStackTrace, 1);
	}
	
	public static void info(String message) {
		Logger.log(Logger.INFO, message, false, 1);
	}
	
	public static void info(String message, boolean printStackTrace) {
		Logger.log(Logger.INFO, message, printStackTrace, 1);
	}
	
	private static void log(int level, String message, boolean printStackTrace, int traceGap) {
		if (isEnabled()) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			StackTraceElement ste = stackTraceElements[1+traceGap+1];
			String returnMess = "";
			switch (level) {
			case LOG:
				returnMess += "[LOG] ";
				break;
			case INFO:
				returnMess += "[INFO] ";
				break;
			case WARNING:
				returnMess += "[WARNING] ";
				break;
			case ERROR:
				returnMess += "[ERROR] ";
				break;
			default:
				throw new RuntimeException("Invalid level! Please use Logger level");
			}
			returnMess += message;
			if (printLine)
				returnMess += " (" + ste.getFileName() + ":" + ste.getLineNumber() + ")";
			
			if (level == LOG || level == INFO)
				System.out.println(returnMess);
			if (level == WARNING || level == ERROR)
				System.err.println(returnMess);
			if (printStackTrace) {
				System.err.println(ste.toString());
			}
		}
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean enabled) {
		Logger.enabled = enabled;
	}

	public static boolean isPrintLine() {
		return printLine;
	}

	public static void setPrintLine(boolean printLine) {
		Logger.printLine = printLine;
	}
}

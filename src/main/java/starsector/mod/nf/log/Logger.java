package starsector.mod.nf.log;




/**
 * We use the name and simular interface of log4j Logger.
 * So it is easily replace with log4j or other logger.
 * @author fengyuan
 *
 * @param <T>
 */
public class Logger implements LogHandler{
	
	private static AppenderType DEFAULT_LOGTYPE = AppenderType.MESSAGE;
	
	/**
	 * appender singleton
	 */
	private static Appender console;
	private static Appender log4j;
	private static Appender message;
	
	/**
	 * set the default appender type. Default log type may change when game restarts.
	 * @param type
	 */
	public static void setDefaultLogType(AppenderType type){
		if (type != null)
			DEFAULT_LOGTYPE = type;
	}
	
	public static AppenderType getDefaltAppenderType(){
		if (DEFAULT_LOGTYPE == null){
			DEFAULT_LOGTYPE = AppenderType.CONSOLE;
		}
		return DEFAULT_LOGTYPE;
	}
	
	
	
	/**
	 * get logger with default appender
	 * @param clazz
	 * @return
	 */
	public static Logger getLogger(Class<?> clazz){
		return getLogger(clazz, DEFAULT_LOGTYPE);
	}
	
	/**
	 * get logger with given appender type
	 * @param clazz
	 * @param type
	 * @return
	 */
	public static Logger getLogger(Class<?> clazz, AppenderType type){
		
		if (type == null){
			type = getDefaltAppenderType();
		}
		
		//
		// try to use singleton
		//
		switch (type) {
		case NULL:{
			return new Logger(clazz, NullAppender.INS);
		}case CONSOLE:{
			if (console == null){
				console = new ConsoleLogAppender();
			}
			return new Logger(clazz, console);
		}case LOG4J:{
			if (log4j == null){
				log4j = new Log4jLogAppender();
			}
			return new Logger(clazz, log4j);
		}case MESSAGE:{
			if (console == null)
				console = new ConsoleLogAppender();
			if (message == null){
				message = new MessageLogAppender();
			}
//			return new Logger(clazz, new ChainLogAppender(message, console));
			return new Logger(clazz, message);
		}default:
			return null;
		}
	}
	
	//========================================================
	// here the logger implementation
	//========================================================
	
	protected Class<?> clazz;
	protected Appender appender;
	
	protected Logger(Class<?> clazz, Appender appender){
		this.clazz = clazz;
		this.appender = appender;
	}
	
	@Override
	public Class<?> getLogClass() {
		return clazz;
	}

	@Override
	public Appender getLogAppender() {
		return appender;
	}


	@Override
	public void info(String msg) {
		appender.info(msg, clazz);
	}


	@Override
	public void debug(String msg) {
		appender.debug(msg, clazz);
	}


	@Override
	public void error(String msg, Throwable throwable) {
		appender.error(msg, throwable, clazz);
	}

}

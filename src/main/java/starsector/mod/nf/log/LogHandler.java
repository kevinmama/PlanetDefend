package starsector.mod.nf.log;


/**
 * game logger. since we can't use log4j directly.
 * I use an configurable logger.
 * LogHandler keeps appender which will actually do logging.
 * @author fengyuan
 *
 */
interface LogHandler {
	Class<?> getLogClass();
	Appender getLogAppender();
	public void info(String msg);
	public void debug(String msg);
	public void error(String msg, Throwable throwable);
}

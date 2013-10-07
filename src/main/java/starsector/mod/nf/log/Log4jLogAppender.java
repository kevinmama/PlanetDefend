package starsector.mod.nf.log;

import com.fs.starfarer.api.Global;


/**
 * print log to starsector.log
 * @author fengyuan
 *
 */
class Log4jLogAppender implements Appender{
	
	Log4jLogAppender() {
	}
	
	@Override
	public void info(String msg, Class<?> clazz) {
		Global.getLogger(clazz).info(msg);
	}

	@Override
	public void debug(String msg, Class<?> clazz) {
		Global.getLogger(clazz).debug(msg);
	}

	@Override
	public void error(String msg, Throwable throwable, Class<?> clazz) {
		Global.getLogger(clazz).error(msg, throwable);
	}
	
}

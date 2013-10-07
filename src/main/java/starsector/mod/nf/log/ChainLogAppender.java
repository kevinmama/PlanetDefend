package starsector.mod.nf.log;

import java.util.LinkedList;


/**
 * combine log appender
 * @author fengyuan
 *
 */
class ChainLogAppender implements Appender{
	
	private LinkedList<Appender> chain = new LinkedList<Appender>();
	
	ChainLogAppender(Appender... appenders) {
		for (Appender appender : appenders) {
			chain.add(appender);
		}
	}
	
	public void debug(String msg, java.lang.Class<?> clazz) {
		for (Appender appender : chain) {
			appender.debug(msg, clazz);
		}
	};
	
	@Override
	public void error(String msg, Throwable throwable, Class<?> clazz) {
		for (Appender appender : chain) {
			appender.error(msg, throwable, clazz);
		}
	}
	
	@Override
	public void info(String msg, Class<?> clazz) {
		for (Appender appender : chain) {
			appender.info(msg, clazz);
		}
	}

}

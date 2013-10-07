package starsector.mod.nf.log;

/**
 * consume the log and print nothing
 * @author fengyuan
 *
 */
class NullAppender implements Appender{
	
	public final static NullAppender INS = new NullAppender();
	
	@Override
	public void info(String msg, Class<?> clazz) {
	}

	@Override
	public void debug(String msg, Class<?> clazz) {
	}

	@Override
	public void error(String msg, Throwable throwable, Class<?> clazz) {
	}
}

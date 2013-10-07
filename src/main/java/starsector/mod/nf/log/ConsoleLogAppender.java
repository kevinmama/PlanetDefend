package starsector.mod.nf.log;


/**
 * print log to console
 * @author fengyuan
 *
 */
class ConsoleLogAppender implements Appender{

	ConsoleLogAppender() {
	}
	
	@Override
	public void info(String msg, Class<?> clazz) {
		if (clazz != null){
			msg = clazz.getName() + "  - " + msg;
		}
		System.out.println(msg);
	}

	@Override
	public void debug(String msg, Class<?> clazz) {
		if (clazz != null){
			msg = clazz.getName() + "  - " + msg;
		}
		System.out.println(msg);
	}

	@Override
	public void error(String msg, Throwable throwable, Class<?> clazz) {
		if (clazz != null){
			msg = clazz.getName() + "  - " + msg;
		}
		System.err.println(msg);
		if (throwable != null)
			throwable.printStackTrace(System.err);
	}

}

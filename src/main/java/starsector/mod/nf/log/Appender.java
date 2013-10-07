package starsector.mod.nf.log;

interface Appender {
	public void info(String msg, Class<?> clazz);
	public void debug(String msg, Class<?> clazz);
	public void error(String msg, Throwable throwable, Class<?> clazz);
}

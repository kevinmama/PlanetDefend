package starsector.mod.nf.log;

/**
 * determine where to put log
 * @author fengyuan
 *
 */
public enum AppenderType {
	
	/**
	 * don't print the log
	 */
	NULL,
	
	/**
	 * print to console
	 */
	CONSOLE,
	
	/**
	 * print to starsector.log
	 */
	LOG4J,
	
	/**
	 * print to message dialog and console.
	 * It has an switch to turn off.
	 */
	MESSAGE
}

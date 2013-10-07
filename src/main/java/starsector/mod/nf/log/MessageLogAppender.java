package starsector.mod.nf.log;

import org.lazywizard.lazylib.campaign.MessageUtils;

import com.fs.starfarer.api.Global;


/**
 * print message to game screen
 * @author fengyuan
 *
 */
public class MessageLogAppender implements Appender{
	
	/**
	 * switch whether prints log to screen
	 */
	public static boolean ON = true; 
	
	MessageLogAppender() {
	}
	
	@Override
	public void info(String msg, Class<?> clazz) {
		if (ON && Global.getSector() != null){
			if (clazz != null){
				msg = clazz.getName() + "  - " + msg;
			}
			MessageUtils.showMessage(msg);
		}
	}

	@Override
	public void debug(String msg, Class<?> clazz) {
		if (ON && Global.getSector() != null){
			if (clazz != null){
				msg = clazz.getName() + "  - " + msg;
			}
			MessageUtils.showMessage(msg);
		}
	}

	@Override
	public void error(String msg, Throwable throwable, Class<?> clazz) {
		if (ON && Global.getSector() != null){
			if (clazz != null){
				msg = clazz.getName() + "  - " + msg;
			}
			MessageUtils.showMessage(msg);
			if (throwable != null)
				MessageUtils.showMessage(throwable.getMessage());
		}
	}

}

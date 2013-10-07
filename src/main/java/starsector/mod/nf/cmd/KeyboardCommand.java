package starsector.mod.nf.cmd;

import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.event.KeyPressEvent;


/**
 * command that trigger by keyboard
 * @author fengyuan
 *
 */
public interface KeyboardCommand {
	
	/**
	 * is the command active? Only active command chan be triggered.
	 * @return
	 */
	boolean isActive();
	
	/**
	 * the key code that trigger the command
	 * @return
	 */
	int getTriggerKeyCode();
	
	/**
	 * execute some debug works.
	 * @param eventbus
	 */
	void execute(KeyPressEvent event, EventBus eventbus);
	
}

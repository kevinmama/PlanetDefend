package starsector.mod.nf;

import org.lwjgl.input.Keyboard;

import starsector.mod.nf.log.MessageLogAppender;

/**
 * Nebular Fantasy settings. reload when game loaded.
 * @author fengyuan
 *
 */
public class NFSettings {
	
	//==============================================================================
	// debugging 
	//==============================================================================
	
	/** Is in debugging mode? */
	public static boolean debugging = true;
	
	/** the key that toggle debugging mode. */
	public static int KEY_BINDING_TOGGLE_DEBUGGING = Keyboard.KEY_F12;
	
	public static int KEY_BINDING_LVLUP = Keyboard.KEY_EQUALS;

	public static int KEY_BINDING_CHECK_FLEET = Keyboard.KEY_F1;
	
	public static int KEY_BINDING_DEBUG_MENU = Keyboard.KEY_D;
	
	/**
	 * reload the settings
	 */
	public static void reload(){
		MessageLogAppender.ON = debugging;
	}
	
}

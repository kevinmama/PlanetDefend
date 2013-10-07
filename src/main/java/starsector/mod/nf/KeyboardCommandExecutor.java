package starsector.mod.nf;

import java.util.HashMap;
import java.util.Map;

import starsector.mod.nf.cmd.KeyboardCommand;
import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.event.KeyPressEvent;


/**
 * keyboard command executor, you can register KeyboardCommand and press key
 * to trigger it.
 * @author fengyuan
 *
 */
class KeyboardCommandExecutor extends BaseEventListener{
	
	private Map<Integer, KeyboardCommand> cmdmap;
	
	public KeyboardCommandExecutor() {
		cmdmap = new HashMap<Integer, KeyboardCommand>();
		setName("BaseDebugCmd");
		listen(CoreEventType.KEY_PRESS);
	}
	
	/**
	 * register the cmd, and return the old registered command with the same trigger key code. 
	 * @param cmd
	 * @return
	 */
	public KeyboardCommand registerCommand(KeyboardCommand cmd){
		return cmdmap.put(cmd.getTriggerKeyCode(), cmd);
	}

	@Override
	public void handle(Event event) {
		if (event instanceof KeyPressEvent){
			KeyPressEvent keyPressEvent = (KeyPressEvent) event;
			int keyCode = keyPressEvent.getKeyCode();
			KeyboardCommand cmd = cmdmap.get(keyCode);
			if (cmd != null && cmd.isActive()){
				cmd.execute(keyPressEvent, eventbus);
			}
		}
	}

}

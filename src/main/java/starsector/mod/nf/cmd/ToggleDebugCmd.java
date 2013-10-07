package starsector.mod.nf.cmd;

import static starsector.mod.nf.NFSettings.debugging;

import org.lazywizard.lazylib.campaign.MessageUtils;

import starsector.mod.nf.NFSettings;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.event.KeyPressEvent;
import starsector.mod.nf.log.MessageLogAppender;


public class ToggleDebugCmd implements KeyboardCommand{

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public int getTriggerKeyCode() {
		return NFSettings.KEY_BINDING_TOGGLE_DEBUGGING;
	}

	@Override
	public void execute(KeyPressEvent event, EventBus eventbus) {
		debugging = ! debugging;
		MessageLogAppender.ON = debugging;
		if (debugging){
			MessageUtils.showMessage("debug on");
			eventbus.dispatch(CoreEventType.DEBUG_ON);
		}else{
			MessageUtils.showMessage("debug off");
			eventbus.dispatch(CoreEventType.DEBUG_OFF);
		}
	}

}

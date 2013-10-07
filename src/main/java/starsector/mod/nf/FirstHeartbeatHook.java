package starsector.mod.nf;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;




/**
 * It generate the {@link CoreEventType#HEARTBEAT_FIRST} event and die.
 * @author fengyuan
 *
 */
class FirstHeartbeatHook extends BaseEventListener{
	
	public FirstHeartbeatHook() {
		setName("FirstHeartbeatHook");
		listen(CoreEventType.HEARTBEAT_FRAME);
	}
	
	@Override
	public void handle(Event event) {
		eventbus.dispatch(CoreEventType.HEARTBEAT_FIRST);
		dispose();
	}
	
}

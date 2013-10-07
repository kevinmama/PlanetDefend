package starsector.mod.nf.event;


/**
 * event listener that listen the HEARTBEAT_FIRST event. 
 * It dies when finished handling.
 * It is usefully to do initialization works which needs player fleets created.
 * @author fengyuan
 *
 */
public abstract class FirstHeartbeatEventListener extends BaseEventListener{
	
	public FirstHeartbeatEventListener() {
		listen(CoreEventType.HEARTBEAT_FIRST);
	}
	
	/**
	 * do some initialization things.
	 */
	protected abstract void doInit();
	
	@Override
	public void handle(Event event) {
		doInit();
		dispose();
	}
	
}

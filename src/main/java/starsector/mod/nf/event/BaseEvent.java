package starsector.mod.nf.event;



/**
 * basic event implementation.
 * @author fengyuan
 *
 */
public class BaseEvent implements Event{
	
	private Enum<?> type;
	
	public BaseEvent(Enum<?> type) {
		this.type = type;
	}
	
	@Override
	public Enum<?> getEventType() {
		return type;
	}
}

package starsector.mod.nf.event;

import java.util.LinkedList;
import java.util.List;

/**
 * base event listener, it provide convenient interface to interactive with
 * event bus. You should initialize its name and what events it listen in
 * constructor. Method 'setName' and 'listen' may help you. You can change its
 * status by method 'setActive' 'die'.
 * @author fengyuan
 *
 */
public abstract class BaseEventListener implements EventListener{
	
	/**
	 * name of the listener, used for logging
	 */
	protected String name;
	
	/**
	 * keep the event bus instance, for raise/dispatch events.
	 */
	protected EventBus eventbus;
	
	protected EventListenerStatus status = EventListenerStatus.ACTIVE;
	protected boolean stopPropagation = false;
	
	/**
	 * types of listening events 
	 */
	protected List<Object> listenEventTypes = new LinkedList<Object>();
	
	/**
	 * declare events to listen.
	 * you should call this method in construct method.
	 * @param types
	 * @return
	 */
	protected BaseEventListener listen(Enum<?>... types){
		for (Enum<?> type : types) {
			listenEventTypes.add(type);
		}
		return this;
	}
	
	@Override
	public String getName() {
		if (name != null)
			return name;
		else
			return toString();
	}
	
	protected void setName(String name){
		this.name = name;
	}

	@Override
	public EventListenerStatus getStatus() {
		return status;
	}
	
	@Override
	public void setStatus(EventListenerStatus status) {
		this.status = status;
	}
	
	public void setActive(boolean active){
		if (active)
			status = EventListenerStatus.ACTIVE;
		else
			status = EventListenerStatus.INACTIVE;
	}
	
	@Override
	public boolean isStopPropagation() {
		return false;
	}
	
	public void setStopPropagation(boolean stopPropagation){
		this.stopPropagation = stopPropagation;
	}
	
	/**
	 * dispose the event listener.
	 */
	public void dispose(){
		switch (this.status) {
		case DISPOSING:
		case DISPOSED:
			break;
		default:
			this.status = EventListenerStatus.DISPOSING;
		}
	}

	/**
	 * listener should call EventBus.registerEventListener .
	 * Give listener a chance to keep the eventbus instance. 
	 * @param eventbus
	 */
	public void register(EventBus eventbus) {
		this.eventbus = eventbus;
		for (Object type : listenEventTypes) {
			if (type instanceof Enum){
				eventbus.registerEventListener((Enum<?>)type, this);
			}
		}
	}
	
}

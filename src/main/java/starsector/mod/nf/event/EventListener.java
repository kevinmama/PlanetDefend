package starsector.mod.nf.event;


/**
 * Event listener interface. listener accept event and process data.
 * event bus will check listener's status. when it's die, event bus remove it.
 * when it's active, event bus will call its 'handle' method to process event.
 * @author fengyuan
 *
 */
public interface EventListener extends java.util.EventListener{
	/**
	 * get the name of listener
	 * @return
	 */
	String getName();
	
	/**
	 * get event listener Status
	 * @return
	 */
	EventListenerStatus getStatus();
	
	/**
	 * set event listner status
	 * @param status
	 */
	void setStatus(EventListenerStatus status);
	
	/**
	 * if it is set, the event won't propagate to the next listener
	 * @return
	 */
	boolean isStopPropagation();
	
	/**
	 * handle the event
	 * @param event
	 */
	void handle(Event event);
	
}

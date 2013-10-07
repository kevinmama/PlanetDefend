package starsector.mod.nf.event;

/**
 * Event Listener Status. control the listener's life cycle.
 * DO NOT SET a {@link #DISPOSED} listener to other status.
 * It may cause concurrency problems.
 * @author fengyuan
 *
 */
public enum EventListenerStatus {
	/**
	 * accept and handle event
	 */
	ACTIVE,
	
	/**
	 * not active, don't accept event
	 */
	INACTIVE,
	
	/**
	 * should be removed from event listener queue
	 */
	DISPOSING,
	
	/**
	 * has been removed from event listener queue.
	 */
	DISPOSED
}

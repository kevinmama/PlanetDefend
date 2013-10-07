package starsector.mod.nf.event;

/**
 * Core event type
 * @author fengyuan
 *
 */
public enum CoreEventType {
	
	//=============================================
	// common event types
	// can be shared among mods.
	//=============================================
	
	/**
	 * When the sector is generated and scripts are add to sector.
	 */
	SECTOR_GENERATED,
	
	/**
	 * occur in game load. when receive this event,
	 * All static references, setting caches are expired.
	 */
	SECTOR_RELOAD,
	
	/**
	 * Keyboard press event. Associate with {@link KeyPressEvent}
	 */
	KEY_PRESS,
	
	/**
	 * Mouse event, currently not bind a event object. User can use the lwjgl Mouse interface.
	 */
	MOUSE_CLICK,
	
	/**
	 * event generated at first heartbeat
	 */
	HEARTBEAT_FIRST,
	
	/**
	 * event generated every frame. It will generate when pause.
	 */
	HEARTBEAT_FRAME,
	
	/**
	 * event generate every frame. It won't generate when pause.
	 */
	HEARTBEAT_ADVANCE,
	/**
	 * event generated every hour
	 */
	HEARTBEAT_HOUR,
	/**
	 * event generated every day
	 */
	HEARTBEAT_DAY,
	
	/**
	 * event generated every week
	 */
	HEARTBEAT_WEEK,
	/**
	 * event generated every month
	 */
	HEARTBEAT_MONTH,
	/**
	 * event generated every cycle
	 */
	HEARTBEAT_CYCLE,
	
	/**
	 * debug on
	 */
	DEBUG_ON,
	
	/**
	 * debug off
	 */
	DEBUG_OFF,
	
	
	//==============================================================================
	// driver event type
	//==============================================================================
	
	
	
}

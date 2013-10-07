package starsector.mod.pld.event;

/**
 * PLD specified event typ.
 * @author fengyuan
 *
 */
public enum PLDEventType {
	
	/**
	 * station begin spawn fleet
	 */
	STATION_BEGIN_SPAWN_FLEET,
	
	/**
	 * station spawned fleet
	 */
	STATION_SPAWNED_FLEET,
	
	/**
	 * station's supply below reserve value.
	 */
	STATION_SUPPLY_SHORTAGE,
	
	/**
	 * station's supply has run out!!
	 */
	STATION_SUPPLY_RUN_OUT,
	
	/**
	 * you receive salary and paid salary to your solders
	 */
	SALARY
	
}

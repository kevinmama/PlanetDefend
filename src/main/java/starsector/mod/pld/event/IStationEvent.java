package starsector.mod.pld.event;

import starsector.mod.pld.domain.PLDStation;

/**
 * station related event interface
 */
public interface IStationEvent {
	
	/**
	 * get the related station
	 * @return
	 */
	public PLDStation getStation();
	
}

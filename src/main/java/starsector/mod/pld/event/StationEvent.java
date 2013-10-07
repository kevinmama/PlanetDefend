package starsector.mod.pld.event;

import starsector.mod.nf.event.BaseEvent;
import starsector.mod.pld.domain.PLDStation;

/**
 * station related event
 * @author fengyuan
 *
 */
public class StationEvent extends BaseEvent implements IStationEvent{
	
	private PLDStation station;

	public StationEvent(Enum<?> type, PLDStation station) {
		super(type);
		this.station = station;
	}

	@Override
	public PLDStation getStation() {
		return station;
	}

}

package starsector.mod.pld.event;

import java.util.Collection;

import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDStation;


public class StationSpawnedFleetsEvent extends StationEvent{
	
	private Collection<PLDFleet> fleets;

	public StationSpawnedFleetsEvent(PLDStation station, Collection<PLDFleet> fleets) {
		super(PLDEventType.STATION_SPAWNED_FLEET, station);
		this.fleets = fleets;
	}

	public Collection<PLDFleet> getFleets() {
		return fleets;
	}
	
	
	
}

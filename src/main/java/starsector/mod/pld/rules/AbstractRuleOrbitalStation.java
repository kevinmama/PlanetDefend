package starsector.mod.pld.rules;

import java.util.Collection;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.Event;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDStation;

import com.fs.starfarer.api.campaign.OrbitalStationAPI;


/**
 * orbital station related rules
 * @author fengyuan
 *
 */
public abstract class AbstractRuleOrbitalStation extends BaseEventListener{

	@Override
	public void handle(Event event) {
		Collection<PLDStation> allStations = PLD.getRegistry().getAllStations();
		for (PLDStation pldStation : allStations) {
			apply(event, pldStation, pldStation.getStation());
		}
	}
	
	/**
	 * apply rule to station
	 * @param station
	 */
	protected abstract void apply(Event event, PLDStation pldStation, OrbitalStationAPI station);
	
}

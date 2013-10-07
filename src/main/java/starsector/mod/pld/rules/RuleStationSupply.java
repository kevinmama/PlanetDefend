package starsector.mod.pld.rules;

import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.pld.PLDSettings;
import starsector.mod.pld.domain.PLDStation;
import starsector.mod.pld.misc.PLDUtils;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;


/**
 * crew product supplies and marines consume supplies every day.
 * @author fengyuan
 *
 */
public class RuleStationSupply extends AbstractRuleOrbitalStation{
	
	public RuleStationSupply() {
		setName(RuleStationSupply.class.getSimpleName());
		listen(CoreEventType.HEARTBEAT_DAY);
	}
	
	@Override
	protected void apply(Event event, PLDStation pldStation,
			OrbitalStationAPI station) {
		
		CargoAPI cargo = station.getCargo();
		
		//
		// marines consume supplies and crew produce supplies 
		//
		float total = 0;
		for (int i = 0; i < PLDSettings.STATION_PEOPLE_PRODUCE_RATE_VECTOR.length; i++) {
			int people = PLDUtils.getPeopleByIndex(cargo, i);
			total += people * PLDSettings.STATION_PEOPLE_PRODUCE_RATE_VECTOR[i];
		}
		
		CargoSupport.addSupplies(cargo, total, 0, PLDSettings.STATION_SUPPLIES_LIMIT);
		
		pldStation.update();
	}
	
}

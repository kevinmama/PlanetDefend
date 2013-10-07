package starsector.mod.pld.rules;

import static starsector.mod.nf.support.PreDefinedCargoItemType.GREEN_CREW;
import static starsector.mod.nf.support.PreDefinedCargoItemType.MARINES;
import static starsector.mod.pld.PLDSettings.STATION_CREW_CONVERT_RATE_VECTOR;
import static starsector.mod.pld.PLDSettings.STATION_CREW_GROW_BASE_VECTOR;
import static starsector.mod.pld.PLDSettings.STATION_CREW_GROW_RATE_VECTOR;
import static starsector.mod.pld.PLDSettings.STATION_CREW_LIMIT;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.pld.PLDSettings;
import starsector.mod.pld.domain.PLDStation;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CrewXPLevel;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;


/**
 * station crew grow and covert every day
 * @author fengyuan
 *
 */
public class RuleStationCrew extends AbstractRuleOrbitalStation{
	
	public RuleStationCrew() {
		setName(RuleStationCrew.class.getSimpleName());
		listen(
			CoreEventType.HEARTBEAT_DAY
		);
	}

	@Override
	protected void apply(Event event, PLDStation pldStation,
			OrbitalStationAPI station) {
		int supplyLevel = pldStation.getSupplyLevel();
		
		CargoAPI cargo = station.getCargo();
		
		//
		// handle crew
		//
		int level;
		level = Math.min(STATION_CREW_GROW_BASE_VECTOR.length-1, supplyLevel);
		float base = STATION_CREW_GROW_BASE_VECTOR[level];
		
		level = Math.min(STATION_CREW_GROW_RATE_VECTOR.length-1, supplyLevel);
		float rate = STATION_CREW_GROW_RATE_VECTOR[level];
		
		int grow = (int) (cargo.getTotalCrew()*rate + base);
		int limit = STATION_CREW_LIMIT - (cargo.getTotalCrew() - cargo.getCrew(CrewXPLevel.GREEN));
		CargoSupport.addCrew(cargo, CrewXPLevel.GREEN, grow, 0, limit);
		
		//
		// handle crew convert
		//
		level = Math.min(supplyLevel, STATION_CREW_CONVERT_RATE_VECTOR.length-1);
		rate = STATION_CREW_CONVERT_RATE_VECTOR[level];
		CargoSupport.crewLevelUpRate(cargo, CrewXPLevel.VETERAN, rate);
		CargoSupport.crewLevelUpRate(cargo, CrewXPLevel.REGULAR, rate);
		CargoSupport.crewLevelUpRate(cargo, CrewXPLevel.GREEN, rate);

		//
		// handle marine convert
		//
		CargoSupport.convertCargoItem(
				cargo, GREEN_CREW.getType(), GREEN_CREW.getId(), 0, Float.MAX_VALUE,  
				cargo, MARINES.getType(), MARINES.getId(), 0, PLDSettings.STATION_MARINES_LIMIT,
				cargo.getCrew(CrewXPLevel.GREEN)*rate);
		
	}
}

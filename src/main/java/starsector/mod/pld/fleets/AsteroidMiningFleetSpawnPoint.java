package starsector.mod.pld.fleets;

import starsector.mod.nf.support.CargoSupport;
import starsector.mod.pld.PLD;
import starsector.mod.pld.PLDSettings;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDStation;
import starsector.mod.pld.domain.StationSupplyLevel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CrewXPLevel;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;


/**
 * spawn drone fleet, up to two teams
 * @author fengyuan
 *
 */
public class AsteroidMiningFleetSpawnPoint extends PLDStationSpawnPoint{
	
	public AsteroidMiningFleetSpawnPoint(PLDStation station) {
		super(station);
		reloadSettings();
	}

	@Override
	public String getName() {
		return station.getQualifiedName() + "-AsteroidMiningFleetSpawnPoint";
	}
	
	@Override
	protected void reloadSettings() {
		super.reloadSettings();
		daysToSpawn = PLDSettings.MINING_FLEET_SPAWN_DAY;
		limit = PLDSettings.MINING_FLEET_LIMIT_PER_STATION;
	}

	@Override
	protected boolean checkAndConsumeResource() {
		if (station.getSupplyLevel() >= StationSupplyLevel.NORMAL){
			// take 1000 supplies to spawn
			CargoAPI cargo = station.getStation().getCargo();
			cargo.removeSupplies(PLDSettings.MINING_FLEET_SPAWN_COST);
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected PLDFleet spawnFleet() {
		
		//
		// spawn fleet
		//
		OrbitalStationAPI stationAPI = station.getStation();
		SectorAPI sector = Global.getSector();
		CampaignFleetAPI fleet = sector.createFleet(stationAPI.getFaction().getId(), PLDSettings.MINING_FLEET_VARIANT_ID);
		StarSystemAPI system = (StarSystemAPI) stationAPI.getContainingLocation();
		system.spawnFleet(stationAPI, 0, 0, fleet);
		
		//
		// init cargo
		//
		CargoAPI cargo = fleet.getCargo();
		CargoSupport.setSupplies(cargo, PLDSettings.MINING_FLEET_SUPPLY_RESERVE);
		cargo.addCrew(CrewXPLevel.GREEN, 20);
		
		PLDFleet pldFleet = PLD.getFactory().createPLDFleet(fleet);
		pldFleet.setBase(station);
		
		// pick mining api
		new AsteroidMiningFleetAI(pldFleet).register(eventbus);
		
		return pldFleet;
	}
	
}

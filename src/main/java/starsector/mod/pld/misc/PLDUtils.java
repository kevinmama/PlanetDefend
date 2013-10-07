package starsector.mod.pld.misc;

import java.util.Collection;

import org.apache.commons.collections.Closure;

import starsector.mod.pld.PLD;
import starsector.mod.pld.PLDSettings;
import starsector.mod.pld.domain.PLDStation;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CrewXPLevel;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;


public final class PLDUtils {
	
	private PLDUtils(){}
	
	public static void foreachStation(Closure closure){
		Collection<PLDStation> sations = PLD.getRegistry().getAllStations();
		for (PLDStation pldStation : sations) {
			OrbitalStationAPI station = pldStation.getStation();
			closure.execute(station);
		}
	}
	
	
	public static final int
		IDX_MARINES = 0,
		IDX_GREEN_CREW = 1,
		IDX_REGULAR_CREW = 2,
		IDX_VETERAN_CREW = 3,
		IDX_ELITE_CREW = 4;
	
	/**
	 * <pre>
	 * get crew by integer index: 
	 * 0 GREEN, 
	 * 1 REGULAR,
	 * 2 VETERAN,
	 * 3 ELITE
	 * 4 MARINES
	 * </pre>
	 * @param cargo
	 * @return
	 */
	public static int getPeopleByIndex(CargoAPI cargo, int index){
		switch (index) {
		case IDX_MARINES:
			return cargo.getMarines();
		case IDX_GREEN_CREW:
			return cargo.getCrew(CrewXPLevel.GREEN);
		case IDX_REGULAR_CREW:
			return cargo.getCrew(CrewXPLevel.REGULAR);
		case IDX_VETERAN_CREW:
			return cargo.getCrew(CrewXPLevel.VETERAN);
		case IDX_ELITE_CREW:
			return cargo.getCrew(CrewXPLevel.ELITE);
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * create a dummy fleet
	 * @return
	 */
	public static CampaignFleetAPI createDummyFleet(){
		SectorAPI sector = Global.getSector();
		return sector.createFleet("neutral", "dummy");
	}
	
	
	/**
	 * create a dummy location the access the item and fleet exchange UI,
	 * user should quickly remove it to prevent AI do some strange actions.
	 * @return
	 */
	public static OrbitalStationAPI createDummyStation(String name){
		SectorAPI sector = Global.getSector();
//		LocationAPI sys = sector.getCurrentLocation();
		StarSystemAPI sys = sector.getStarSystem(PLDSettings.DUMMY_STAR_SYSTEM_NAME);
		if (sys == null){
			sys = sector.createStarSystem(PLDSettings.DUMMY_STAR_SYSTEM_NAME);
		}
		return (OrbitalStationAPI)sys.addOrbitalStation(sys.getStar(), 0, 100, 100, name, "neutral");
//		LocationAPI loc = sector.getCurrentLocation();
//		CampaignFleetAPI fleet = sector.getPlayerFleet();
	}
	
	/**
	 * remove dummy station and its containning system
	 * @param dummy
	 */
	public static void removeDummyStation(OrbitalStationAPI dummy){
		SectorAPI sector = Global.getSector();
		StarSystemAPI sys = sector.getStarSystem(PLDSettings.DUMMY_STAR_SYSTEM_NAME);
//		LocationAPI sys = sector.getCurrentLocation();
		if (sys != null){
			sys.removeEntity(dummy);
			if (sys instanceof StarSystemAPI)
				sector.removeStarSystem((StarSystemAPI) sys);
		}
	}
	
	/**
	 * remove a entity
	 */
	public static void removeEntity(SectorEntityToken entity){
		entity.getContainingLocation().removeEntity(entity);
	}
	
}

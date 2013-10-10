package starsector.mod.pld.misc;

import java.util.Collection;

import org.apache.commons.collections.Closure;

import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDArmy;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDRegistry;
import starsector.mod.pld.domain.PLDStation;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CrewXPLevel;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;


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
	 * create an empty detachment
	 * @param army
	 * @return
	 */
	public static PLDFleet createEmptyDetachment(PLDArmy army){
		SectorAPI sector = Global.getSector();
		CampaignFleetAPI dummyFleet = sector.createFleet("neutral", "dummy");
		dummyFleet.setFaction(army.getFlagFleet().getFleet().getFaction().getId());
		dummyFleet.setName(army.getFlagFleet().getName());
		PLDFleet fleet = PLD.getFactory().createPLDFleet(dummyFleet);
		army.addFleet(fleet);
		return fleet;
	}
	
	
	/**
	 * remove a entity
	 */
	public static void removeEntity(SectorEntityToken entity){
		entity.getContainingLocation().removeEntity(entity);
	}
	
	/**
	 * determine if fleet0 in player's army
	 * @param fleet0
	 * @return
	 */
	public static boolean isInPlayerArmy(SectorEntityToken fleet0){
		if (fleet0 != null && fleet0 instanceof CampaignFleetAPI){
			PLDRegistry reg = PLD.getRegistry();
			PLDArmy army = reg.getPlayerArmy();
			PLDFleet fleet = reg.getFleet((CampaignFleetAPI) fleet0);
			return army.contains(fleet);
		}
		return false;
	}
	
}

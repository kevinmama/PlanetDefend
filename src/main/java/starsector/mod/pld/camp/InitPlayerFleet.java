package starsector.mod.pld.camp;

import java.util.Iterator;
import java.util.List;

import starsector.mod.nf.event.FirstHeartbeatEventListener;
import starsector.mod.nf.support.CargoQuantityParams;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.pld.Names;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDStation;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;


/**
 * initialize player fleet
 * @author fengyuan
 *
 */
public class InitPlayerFleet extends FirstHeartbeatEventListener{
	
	public InitPlayerFleet() {
		setName(InitPlayerFleet.class.getSimpleName());
	}

	@Override
	protected void doInit() {
		
		SectorAPI sector = Global.getSector();
		
		//==============================================================================
		// initialize player's status
		//==============================================================================
		
		CampaignFleetAPI playerFleet = sector.getPlayerFleet();
		playerFleet.getCommanderStats().getLogistics().modifyFlat("inital", 50);
		
		//==============================================================================
		// determine player's faction and set player's starting location
		//==============================================================================
		
		StarSystemAPI athena = sector.getStarSystem(Names.SYSTEM_ATHENA);
		if (athena != null){
			// the system create correctly
			sector.setCurrentLocation(athena);
			// find a station
			Iterator<PLDStation> iter = PLD.getRegistry().getAllStations().iterator();
			if (iter.hasNext()){
				OrbitalStationAPI station = iter.next().getStation();
				playerFleet.setLocation(station.getLocation().getX() + 1000, station.getLocation().getY() + 1000);
				playerFleet.setFaction(station.getFaction().getId());
			}
		}
		
		//
		// initialize cargo and ship
		//
		CargoQuantityParams params = new CargoQuantityParams();
		params.credits = 10000;
		params.supplies = 500;
		params.fuel = 100;
		params.greenCrew = 150;
		params.regularCrew = 50;
		params.marines = 50;
		CargoSupport.setCargo(playerFleet.getCargo(), params, null, null);
		playerFleet.getCargo().setFreeTransfer(true);
		playerFleet.getCargo().sort();
		
		FleetDataAPI fleetData = playerFleet.getFleetData();
		@SuppressWarnings("rawtypes")
		List curFleetMembers = fleetData.getMembersListCopy();
		for (int i = 0; i < curFleetMembers.size(); i++) {
			fleetData.removeFleetMember((FleetMemberAPI) curFleetMembers.get(i));
		}
		String[] fleetMemberNames = {
				"eagle_Assault", 
//				"atlas_Standard", 
//				"valkyrie_Elite", 
//				"valkyrie_Elite"
		};
		for (int i = 0; i < fleetMemberNames.length; i++) {
			String fleetMemberName = fleetMemberNames[i];
			FleetMemberAPI fleetMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, fleetMemberName);
			fleetData.addFleetMember(fleetMember);
		}
		
	}

}

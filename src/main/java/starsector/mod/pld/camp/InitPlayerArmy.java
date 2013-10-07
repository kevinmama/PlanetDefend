package starsector.mod.pld.camp;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;

import starsector.mod.nf.event.FirstHeartbeatEventListener;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDArmy;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDObjectFactory;

/*
 * init player's army
 */
public class InitPlayerArmy extends FirstHeartbeatEventListener{
	
	public InitPlayerArmy() {
		setName(InitPlayerArmy.class.getSimpleName());
	}

	@Override
	protected void doInit() {
		//==============================================================================
		// build player's army
		//==============================================================================
		SectorAPI sector = Global.getSector();
		CampaignFleetAPI playerFleet = sector.getPlayerFleet();
		
		PLDObjectFactory factory = PLD.getFactory();
		factory.resetPlayerFleetAndArmy("player");
		
		// create anthor flag fleet
		CampaignFleetAPI flagFleet0 = sector.createFleet("neutral", "flag_fleet");
		flagFleet0.setFaction(playerFleet.getFaction().getId());
		PLDFleet flagFleet = factory.createPLDFleet(flagFleet0);
		sector.getCurrentLocation().spawnFleet(playerFleet, 100, 100, flagFleet0);
		
		PLDArmy playerArmy = PLD.getRegistry().getPlayerArmy();
		playerArmy.addFleet(flagFleet);
		playerArmy.setFlagFleet(flagFleet);
	}

}

package starsector.mod.nf.autodrive;

import starsector.mod.nf.NF;
import starsector.mod.nf.event.EventBus;
import starsector.mod.pld.PLD;

import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;

/**
 * player's auto driver
 * @author fengyuan
 *
 */
public class AutoDrivers {
	
	/**
	 * auto drive to given location
	 * @param target
	 * @param onCompletion
	 */
	public static void gotoloc(SectorEntityToken target, Script onCompletion){
		EventBus eventbus = NF.getEventbus();
		CampaignFleetAPI fleet = PLD.getRegistry().getPlayerFleet().getFleet();
		new GotoLocationDriver(fleet, target, 0, onCompletion).register(eventbus);
	}
	
	/**
	 * follow a target
	 * @param target
	 * @param distance
	 */
	public static void follow(SectorEntityToken target, float distance){
		EventBus eventbus = NF.getEventbus();
		CampaignFleetAPI fleet = PLD.getRegistry().getPlayerFleet().getFleet();
		new FollowDriver(fleet, target, distance).register(eventbus);
	}
	
	
}

package starsector.mod.pld.camp;


import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.log.Logger;
import starsector.mod.pld.PLD;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;

/**
 * when player is beaten, update the player fleet
 * @author fengyuan
 *
 */
public class PlayerFleetDaemon extends BaseEventListener{
	
	private static Logger log = Logger.getLogger(PlayerFleetDaemon.class);
	
	public PlayerFleetDaemon() {
		listen(CoreEventType.HEARTBEAT_HOUR);
	}
	
	@Override
	public void handle(Event event) {
		CampaignFleetAPI newPlayerFleet = Global.getSector().getPlayerFleet();
		CampaignFleetAPI playerFleet = PLD.getRegistry().getPlayerFleet().getFleet();
		if (newPlayerFleet != playerFleet){
			log.info("player fleet changed");
			PLD.getFactory().updatePlayerFleet(newPlayerFleet);
		}
	}

}

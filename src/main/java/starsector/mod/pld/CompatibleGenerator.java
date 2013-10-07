package starsector.mod.pld;

import starsector.mod.nf.NF;
import starsector.mod.nf.NFSectorGeneratorPlugin;
import starsector.mod.nf.cmd.KeyboardCommand;
import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;
import starsector.mod.pld.camp.InitPlayerArmy;
import starsector.mod.pld.camp.PLDCampaignPlugin;
import starsector.mod.pld.camp.PlayerFleetDaemon;
import starsector.mod.pld.cmd.CallArmyMenuCmd;
import starsector.mod.pld.domain.PLDRegistry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;

/**
 * Compatible with core mod. Only active the Mother Ship feature currently.
 * For quickly publish the first version and ends my holiday.
 * @author fengyuan
 *
 */
public class CompatibleGenerator extends NFSectorGeneratorPlugin{
	
	private Logger log = Logger.getLogger(PLDSectorGen.class, AppenderType.LOG4J);
	private EventBus eventbus;
	private PLD pld;
	private PLDRegistry registry;

	@Override
	public void beforeGenerateSector(NF nebularFantasy) {
		/**
		 * init global settings
		 */
		pld = PLD.create();
		registry = PLD.getRegistry();
		eventbus = nebularFantasy.getEventbus();
	}

	@Override
	public void afterGenerateSector(NF nebularFantasy) {
		SectorAPI sector = Global.getSector();
		
		//
		// register rules
		//
		
		BaseEventListener[] listners = {
			new PLDDaemon(pld),
			new InitPlayerArmy(),
			new PlayerFleetDaemon(),
		};
		
		for (BaseEventListener listener : listners) {
			listener.register(eventbus);
		}
		
		//
		// register commands
		//
		KeyboardCommand[] cmds = {
				new CallArmyMenuCmd()
		};
		
		for (KeyboardCommand cmd : cmds) {
			nebularFantasy.registerKeyboardCommand(cmd);
		}
		
		//
		// reset player's respawn point
		//
		
//		sector.setRespawnLocation(registry.athena);
//		sector.getRespawnCoordinates().set(0, 0);	// may fix by event listener
		sector.registerPlugin(new PLDCampaignPlugin());
	}

	@Override
	public void generateSector(NF sector) {
		// do nothing
	}

}

package starsector.mod.pld;


import starsector.mod.nf.NF;
import starsector.mod.nf.NFSectorGeneratorPlugin;
import starsector.mod.nf.cmd.KeyboardCommand;
import starsector.mod.nf.debug.DebugMenuFactory;
import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;
import starsector.mod.pld.camp.InitPlayerArmy;
import starsector.mod.pld.camp.InitPlayerFleet;
import starsector.mod.pld.camp.PLDCampaignPlugin;
import starsector.mod.pld.cmd.CallArmyMenuCmd;
import starsector.mod.pld.debug.ArmyDebugMenuFactory;
import starsector.mod.pld.debug.RegistryDebugMenuFactory;
import starsector.mod.pld.domain.PLDRegistry;
import starsector.mod.pld.misc.MessageNotifier;
import starsector.mod.pld.rules.RuleSalary;
import starsector.mod.pld.rules.RuleStationCrew;
import starsector.mod.pld.rules.RuleStationSpawnpoint;
import starsector.mod.pld.rules.RuleStationSupply;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;


/**
 * generate PLD sector
 * @author fengyuan
 *
 */
public class PLDSectorGen extends NFSectorGeneratorPlugin{
	
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
	public void generateSector(NF nebular_fantasy) {
		log.info("generate 'planet defend' sector: ");
		new AthenaSystemGenerator().generate();
		
		// initialize faction relationship
		log.info("initFactionRelationships");
		initFactionRelationships();
	}
	
	@Override
	public void afterGenerateSector(NF nebularFantasy) {
		
		SectorAPI sector = Global.getSector();
		
		//
		// register rules
		//
		
		BaseEventListener[] listners = {
			new PLDDaemon(pld),
			new MessageNotifier(),
			
			new InitPlayerFleet(),
			new InitPlayerArmy(),
			new RuleStationSpawnpoint(),
			
			new RuleSalary(),
			new RuleStationSupply(),
			new RuleStationCrew(),
			
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
		// register debug menus
		// 

		DebugMenuFactory[] debugMenuFactories = {
			new RegistryDebugMenuFactory(),
			new ArmyDebugMenuFactory()
		};
		
		for (DebugMenuFactory debugMenuFactory : debugMenuFactories) {
			NF.getDebugManager().contributeDebugMenu(debugMenuFactory);
		}
		
		//
		// reset player's respawn point
		//
		
		sector.setRespawnLocation(registry.athena);
		sector.getRespawnCoordinates().set(0, 0);	// may fix by event listener
		sector.registerPlugin(new PLDCampaignPlugin());
	}
	
	private void initFactionRelationships(){
		SectorAPI sector = Global.getSector();
		FactionAPI player = sector.getFaction("player");
		FactionAPI pirates = sector.getFaction("pirates");
		player.setRelationship(pirates.getId(), -1f);
	}

}

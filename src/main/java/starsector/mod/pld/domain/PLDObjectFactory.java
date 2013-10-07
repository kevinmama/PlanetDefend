package starsector.mod.pld.domain;

import static starsector.mod.pld.domain.PLDRegistry.Type.ARMY;
import static starsector.mod.pld.domain.PLDRegistry.Type.FLEET;
import static starsector.mod.pld.domain.PLDRegistry.Type.STATION;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;


/**
 * factory to create PLD objects.
 * work with PLDRegistry to build index of objects.
 * @author fengyuan
 *
 */
public class PLDObjectFactory {
	
	private static final Logger log = Logger.getLogger(PLDObjectFactory.class, AppenderType.CONSOLE);
	private PLDRegistry reg;
	private static final String DISPOSE_OBJECT_MESSAGE_TEMPLATE = "dispose {0} \"{1}\"";
	private static final String CREATE_OBJECT_MESSAGE_TEMPLATE = "create {0} \"{1}\"";
	
	public PLDObjectFactory(PLDRegistry reg) {
		this.reg = reg;
	}
	
	private void log(String template, String... args){
		Object[] vargs = new Object[args.length];
		for (int i = 0; i < vargs.length; i++) {
			vargs[i] = args[i];
		}
		String msg = MessageFormat.format(template, vargs);
		log.info(msg);
	}
	
	
	void dispose(PLDObject object){
		reg.unregister(object);
		log(DISPOSE_OBJECT_MESSAGE_TEMPLATE, "object", object.getQualifiedName());
	}
	
	
	//==============================================================================
	// station
	//==============================================================================
	
	/**
	 * create PLD station
	 * @param station
	 * @return
	 */
	public PLDStation createPLDStation(OrbitalStationAPI station){
		PLDStation pldStation = new PLDStation();
		pldStation.setStation(station);
		reg.register(pldStation, STATION);
		log(CREATE_OBJECT_MESSAGE_TEMPLATE, "station", pldStation.getQualifiedName());
		return pldStation;
	}
	
	/**
	 * dispose PLD station
	 * @param fleet
	 */
	void dispose(PLDStation station){
		reg.unregister(station, STATION);
		log(DISPOSE_OBJECT_MESSAGE_TEMPLATE, "station", station.getQualifiedName());
	}
	
	
	//==============================================================================
	// fleet
	//==============================================================================
	
	/**
	 * create PLD fleet
	 * @param fleet
	 * @return
	 */
	public PLDFleet createPLDFleet(CampaignFleetAPI fleet){
		PLDFleet pldFleet = new PLDFleet(fleet);
		reg.register(pldFleet, FLEET);
		log(CREATE_OBJECT_MESSAGE_TEMPLATE, "fleet", pldFleet.getQualifiedName());
		return pldFleet;
	}
	
	/**
	 * dispose PLD fleet
	 * @param fleet
	 */
	public void dispose(PLDFleet fleet){
		reg.unregister(fleet, FLEET);
		log(DISPOSE_OBJECT_MESSAGE_TEMPLATE, "fleet", fleet.getQualifiedName());
	}
	
	//==============================================================================
	// army
	//==============================================================================
	
	/**
	 * create PLD army
	 * @param name
	 * 	army name
	 * @param fleets
	 * @return
	 */
	public PLDArmy createPLDArmy(String name, PLDFleet flagFleet, List<PLDFleet> fleets){
		PLDArmy army = new PLDArmy(name, flagFleet, fleets);
		reg.register(army, ARMY);
		log(CREATE_OBJECT_MESSAGE_TEMPLATE, "army", army.getQualifiedName());
		return army;
	}
	
	/**
	 * create a army contains only one fleet
	 * @param name
	 * @param flagFleet
	 * @return
	 */
	public PLDArmy createPLDArmy(String name, PLDFleet flagFleet){
		return createPLDArmy(name, flagFleet, Arrays.asList(flagFleet));
	}
	
	/**
	 * dispose PLD army
	 * @param fleet
	 */
	public void dispose(PLDArmy army){
		reg.unregister(army, ARMY);
		log(DISPOSE_OBJECT_MESSAGE_TEMPLATE, "army", army.getQualifiedName());
	}
	
	
	//==============================================================================
	// player
	//==============================================================================
	
	/**
	 * update when player fleet change
	 * @param playerFleet
	 */
	public void updatePlayerFleet(CampaignFleetAPI playerFleet){
		PLDFleet oldFleet = reg.playerFleet;
		oldFleet.dispose();
		PLDFleet fleet = createPLDFleet(playerFleet);
		reg.playerFleet = fleet;
		reg.playerArmy.addFleet(fleet);
		reg.playerArmy.removeFleet(oldFleet);
	}
	
	
	public void resetPlayerFleetAndArmy(String armyName){
		CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
		if (reg.playerFleet != null){
			reg.playerFleet.dispose();
		}
		if (reg.playerArmy != null){
			reg.playerArmy.dispose();
		}
		reg.playerFleet = createPLDFleet(fleet);
		reg.playerArmy = createPLDArmy(armyName, reg.playerFleet);
	}
	
}

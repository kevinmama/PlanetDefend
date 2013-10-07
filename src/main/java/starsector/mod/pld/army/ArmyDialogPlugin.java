package starsector.mod.pld.army;

import org.lwjgl.input.Keyboard;

import starsector.mod.nf.autodrive.AutoDrivers;
import starsector.mod.nf.menu.BaseDlgMenu;
import starsector.mod.nf.menu.BaseDlgMenuItem;
import starsector.mod.nf.menu.MenuDialogPlugin;
import starsector.mod.nf.support.FleetSupport;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDArmy;
import starsector.mod.pld.domain.PLDFleet;

import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CoreInteractionListener;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;

/**
 * army dialog, should provide reorganize, army command, logistic menu. 
 * @author fengyuan
 *
 */
public class ArmyDialogPlugin extends MenuDialogPlugin implements CoreInteractionListener {

	/**
	 * army of player
	 */
	private PLDArmy army;
	
	/**
	 * player fleet
	 */
	private PLDFleet player;
	
	/**
	 * target fleet in the army
	 */
	private PLDFleet target;
	
	/**
	 * a dummy station to exchange items and ships
	 */
	private OrbitalStationAPI dummy;
	
	@Override
	public void init() {
		visualPanel.setVisualFade(0.25f, 0.25f);
		
		army = PLD.getRegistry().getPlayerArmy();
		player = PLD.getRegistry().getPlayerFleet();
		textPanel.addParagraph("communicate with MotherShip:");
		
		//==============================================================================
		// build menu
		//==============================================================================
		
		BaseDlgMenu top = (BaseDlgMenu) getTopMenu();
		final PLDFleet flagFleet = army.getFlagFleet();
		final CampaignFleetAPI flagFleet0 = flagFleet.getFleet();
		if (flagFleet.getFleet().isAlive() && flagFleet != player){
			new BaseDlgMenuItem("follow MotherShip", top){
				{
					keyCode = Keyboard.KEY_F;
				}
				@Override
				public void onSelect(Object context) {
					AutoDrivers.follow(army.getFlagFleet().getFleet(), 0);
					dispose();
				}
			};
			
			new BaseDlgMenuItem("order MotherShip to follow you",top){
				{
					keyCode = Keyboard.KEY_C;
				}
				@Override
				public void onSelect(Object context) {
					flagFleet0.clearAssignments();
					FleetSupport.follow(flagFleet0, player.getFleet());
					dispose();
				}
			};
			
			new BaseDlgMenuItem("order ModerShip go to the nearest non enemy station", top){
				private OrbitalStationAPI station;
				{
					keyCode = Keyboard.KEY_T;
					station = FleetSupport.getNearestStation(flagFleet0, true);
					enable = station != null;
					tooltip = enable ? null : "no such station";
				}
				@Override
				public void onSelect(Object context) {
					flagFleet0.clearAssignments();
					FleetSupport.follow(flagFleet0, station);
					dispose();
				}
			};
			
			new BaseDlgMenuItem("let MotherShip stay in your position",top){
				{
					keyCode = Keyboard.KEY_H;
				}
				@Override
				public void onSelect(Object context) {
					flagFleet0.clearAssignments();
					FleetSupport.gotoloc(flagFleet0, player.getFleet().getLocation(), new Script(){
						@Override
						public void run() {
							flagFleet0.clearAssignments();
							FleetSupport.stay(flagFleet0);
						}
					});
					dispose();
				}
			};
			
			new BaseDlgMenuItem("let MotherShip stop",top){
				{
					keyCode = Keyboard.KEY_S;
				}
				@Override
				public void onSelect(Object context) {
					flagFleet0.clearAssignments();
					FleetSupport.stay(flagFleet0);
					dispose();
				}
			};
			
			new BaseDlgMenuItem("let MotherShip go free", top){
				{
					keyCode = Keyboard.KEY_E;
				}
				public void onSelect(Object context) {
					flagFleet0.clearAssignments();
					dispose();
				};
			};
		}
	}


	@Override
	public void advance(float amount) {
	}


	@Override
	public void backFromEngagement(EngagementResultAPI battleResult) {
	}


	@Override
	public Object getContext() {
		return null;
	}


	@Override
	public void coreUIDismissed() {
	}

	
	
	
	// leave
//	CampaignFleetAPI dummyFleet = PLDUtils.createDummyFleet();
//	CargoSupport.takeAll(dummyFleet, dummy.getCargo());
//	CampaignFleetAPI playerFleet = PLD.getRegistry().getPlayerFleet().getFleet();
//	playerFleet.getContainingLocation().spawnFleet(playerFleet, 0, 0, dummyFleet);
//	
//	dummyFleet.clearAssignments();
//	FleetSupport.addAssignmentForEver(dummyFleet, FleetAssignment.DEFEND_LOCATION, playerFleet);
//	
//	PLDUtils.removeEntity(dummy);
//	Global.getSector().setPaused(false);
//	dialog.dismiss();

}




package starsector.mod.pld.army;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import starsector.mod.nf.menu.BaseDlgMenu;
import starsector.mod.nf.menu.BaseDlgMenuItem;
import starsector.mod.nf.menu.MenuDialogPlugin;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDFleet;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CoreInteractionListener;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

/**
 * exchange with flag fleet
 * @author fengyuan
 *
 */
public class FlagFleetDialogPlugin extends MenuDialogPlugin implements CoreInteractionListener{

	private PLDFleet playerFleet;
	private PLDFleet flagFleet;
	private OrbitalStationAPI dummy;
	
	private static final Color HIGHLIGHT_COLOR = Global.getSettings().getColor("buttonShortcut");
	
	@Override
	public void init() {
		BaseDlgMenu top = (BaseDlgMenu) getTopMenu();
		flagFleet = PLD.getRegistry().getPlayerArmy().getFlagFleet();
		playerFleet = PLD.getRegistry().getPlayerFleet();
		
		// may cause crash, why?
//		visualPanel.showFleetInfo(playerFleet.getName(), playerFleet.getFleet(), flagFleet.getName(), flagFleet.getFleet(), new FleetEncounterContext());
		textPanel.addParagraph("You have come back to mother ship.");
		
		new BaseDlgMenuItem("exchange items", top){
			{
				keyCode = Keyboard.KEY_I;
			}
			@Override
			public void onSelect(Object context) {
				createDummyIfNeed();
				visualPanel.showCore(CoreUITabId.CARGO, dummy, true, FlagFleetDialogPlugin.this);
			}
		};
		
		new BaseDlgMenuItem("exchange ships", top){
			{
				keyCode = Keyboard.KEY_F;
			}
			@Override
			public void onSelect(Object context) {
				createDummyIfNeed();
				visualPanel.showCore(CoreUITabId.FLEET, dummy, true, FlagFleetDialogPlugin.this);
			}
		};
		
		new BaseDlgMenuItem("refit", top){
			{
				keyCode = Keyboard.KEY_R;
			}
			@Override
			public void onSelect(Object context) {
				createDummyIfNeed();
				visualPanel.showCore(CoreUITabId.REFIT, dummy, true, FlagFleetDialogPlugin.this);
			}
		};
		
		new BaseDlgMenuItem("repair", top){
			{
				keyCode = Keyboard.KEY_E;
			}
			
			@Override
			public void show(OptionPanelAPI optionPanel) {
				CampaignFleetAPI fleet = playerFleet.getFleet();
				float needed = fleet.getLogistics().getTotalRepairSupplyCost();
				float supplies = fleet.getCargo().getSupplies();
				if (needed <= 0){
					tooltip = "does not require any repairs";
					enable = false;
				}else if (supplies < needed){
					tooltip = "Full repairs require " + needed + " supplies. Only " + supplies + " supplies are available";
					enable = false;
				}else{
					tooltip = "Full repairs require " + needed + " supplies. " + supplies + " supplies are available";
					enable = true;
				}
				super.show(optionPanel);
			}
			
			@Override
			public void onSelect(Object context) {
				CampaignFleetAPI playerFleet = FlagFleetDialogPlugin.this.playerFleet.getFleet();
				float needed = playerFleet.getLogistics().getTotalRepairSupplyCost();
				float supplies = playerFleet.getCargo().getSupplies();
				if (supplies < needed){
					textPanel.addParagraph("you need ");
					textPanel.highlightLastInLastPara("" + (int) needed, HIGHLIGHT_COLOR);
					textPanel.appendToLastParagraph(" supplies to repair");
					return;
				}else{
					for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
						member.getStatus().repairFully();
						float max = member.getRepairTracker().getMaxCR();
						float curr = member.getRepairTracker().getBaseCR();
						if (max > curr) {
							member.getRepairTracker().applyCREvent(max - curr, "Repaired at mother ship");
						}
					}
					if (needed > 0) {
						playerFleet.getCargo().removeSupplies(needed);
					}
				}
			}
		};
	}
	
	private OrbitalStationAPI createDummyIfNeed(){
//		if (dummy == null){
//			dummy = PLDUtils.createDummyStation(flagFleet.getName());
//			CargoSupport.setCargo(dummy.getCargo(), flagFleet.getFleet(), false);	// sync cargo to dummy
//		}
//		return dummy;
		return null;
	}
	
	private void removeDummyIfNeed(){
//		if (dummy != null){
//			// sync to flagFleet
//			CampaignFleetAPI fleet = flagFleet.getFleet();	
//			CargoSupport.clear(fleet, false);
//			CargoSupport.takeAll(fleet, dummy.getCargo());
//			PLDUtils.removeDummyStation(dummy);
//			CargoSupport.renewFleetMembers(playerFleet.getFleet());
//			dummy = null;
//		}
	}
	
	@Override
	public void advance(float amount) {
	}
	
	@Override
	public Object getContext() {
		return null;
	}

	@Override
	public void coreUIDismissed() {
		removeDummyIfNeed();
		showMenu();
	}

	@Override
	public void backFromEngagement(EngagementResultAPI battleResult) {
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
}

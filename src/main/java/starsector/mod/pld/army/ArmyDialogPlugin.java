package starsector.mod.pld.army;

import starsector.mod.nf.menu.MenuDialogPlugin;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDArmy;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDRegistry;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.EngagementResultAPI;

/**
 * army dialog, should provide reorganize, army command, logistic menu. 
 * @author fengyuan
 *
 */
public class ArmyDialogPlugin extends MenuDialogPlugin {
	
	/**
	 * the fleet trigger the dialog
	 */
	private PLDFleet another;
	
	public PLDFleet getAnother() {
		return another;
	}

	public void setAnother(PLDFleet another) {
		this.another = another;
	}
	
	private boolean selectingTarget = false;

	@Override
	public void init() {
		
		PLDRegistry reg = PLD.getRegistry();
		final PLDArmy playerArmy = reg.getPlayerArmy();
		
		PLDFleet target = null;
		SectorEntityToken target0 = getDialog().getInteractionTarget();
		if (target0 != null && target0 instanceof CampaignFleetAPI){
			target = reg.getFleet((CampaignFleetAPI) target0);
			if (playerArmy.contains(target)){
				setAnother(target);
			}
		}
		
		//==============================================================================
		// build menu
		//==============================================================================
		addMenuFactory(new ArmyMenuFactory());
	}


	@Override
	public void advance(float amount) {
//		if (selectingTarget){
//			SectorEntityToken target = dialog.getInteractionTarget();
//			if (target != null && target instanceof CampaignFleetAPI){
//				PLDFleet fleet = PLD.getRegistry().getFleet((CampaignFleetAPI) target);
//				if (fleet != null){
//					selectTargets.add(fleet);
//				}
//			}
//		}
	}


	@Override
	public void backFromEngagement(EngagementResultAPI battleResult) {
	}


	@Override
	public Object getContext() {
		return null;
	}

}




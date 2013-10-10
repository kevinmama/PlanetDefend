package starsector.mod.pld.camp;

import starsector.mod.pld.PLD;
import starsector.mod.pld.army.ArmyDialogPlugin;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDRegistry;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.CoreCampaignPluginImpl;

public class PLDCampaignPlugin extends CoreCampaignPluginImpl{
	
	@Override
	public PluginPick<InteractionDialogPlugin> pickInteractionDialogPlugin(
			SectorEntityToken interactionTarget) {
		PLDRegistry reg = PLD.getRegistry();
		if (interactionTarget instanceof CampaignFleetAPI){
			PLDFleet fleet = reg.getFleet((CampaignFleetAPI) interactionTarget);
			if (reg.getPlayerArmy().contains(fleet)){
				return new PluginPick<InteractionDialogPlugin>(new ArmyDialogPlugin(), PickPriority.MOD_GENERAL);
			}
		}
		
		return super.pickInteractionDialogPlugin(interactionTarget);
	}
	
}

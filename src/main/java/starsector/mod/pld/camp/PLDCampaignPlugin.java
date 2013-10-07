package starsector.mod.pld.camp;

import starsector.mod.pld.PLD;
import starsector.mod.pld.army.FlagFleetDialogPlugin;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.CoreCampaignPluginImpl;

public class PLDCampaignPlugin extends CoreCampaignPluginImpl{
	
	@Override
	public PluginPick<InteractionDialogPlugin> pickInteractionDialogPlugin(
			SectorEntityToken interactionTarget) {
		
		if (interactionTarget == PLD.getRegistry().getPlayerArmy().getFlagFleet().getFleet()){
			return new PluginPick<InteractionDialogPlugin>(new FlagFleetDialogPlugin(), PickPriority.MOD_GENERAL); 
		}else
			return super.pickInteractionDialogPlugin(interactionTarget);
	}
	
}

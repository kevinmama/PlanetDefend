package starsector.mod.pld.cmd;

import starsector.mod.nf.cmd.KeyboardCommand;
import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.event.KeyPressEvent;
import starsector.mod.pld.PLDSettings;
import starsector.mod.pld.army.ArmyDialogPlugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;

public class CallArmyMenuCmd implements KeyboardCommand{
	
	@Override
	public int getTriggerKeyCode() {
		return PLDSettings.KEYBINDING_ARMY;
	}

	@Override
	public void execute(KeyPressEvent event, EventBus eventbus) {
		SectorAPI sector = Global.getSector();
		sector.getCampaignUI().showInteractionDialog(new ArmyDialogPlugin(), null);
	}

	@Override
	public boolean isActive() {
		return true;
	}
	

}

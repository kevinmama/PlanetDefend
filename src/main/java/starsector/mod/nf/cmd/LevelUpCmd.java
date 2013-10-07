package starsector.mod.nf.cmd;

import org.lazywizard.lazylib.campaign.MessageUtils;

import starsector.mod.nf.NFSettings;
import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.event.KeyPressEvent;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;


public class LevelUpCmd extends AbstractDebugCommand{

	@Override
	public int getTriggerKeyCode() {
		return NFSettings.KEY_BINDING_LVLUP;
	}

	@Override
	public void execute(KeyPressEvent event, EventBus eventbus) {
		MutableCharacterStatsAPI stats = Global.getSector().getPlayerFleet().getCommander().getStats();
		int curLevel = stats.getLevel();
		do{
			stats.addXP(100);
			stats.levelUpIfNeeded();
		}while(stats.getLevel() == curLevel);
		MessageUtils.showMessage("level up: " + curLevel + " --> " + stats.getLevel());
	}

}

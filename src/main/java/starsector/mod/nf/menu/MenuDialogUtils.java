package starsector.mod.nf.menu;

import starsector.mod.nf.support.NFClosure;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CoreInteractionListener;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.SectorEntityToken;

/**
 * some frequently used menu and item
 * @author fengyuan
 *
 */
public class MenuDialogUtils{
	
	/**
	 * create a closure that open 'map' UI to select,
	 * and update dialog plugin after core dismiss.
	 * @param plugin
	 * @param closure
	 * 	scripts runs when core ui dismiss
	 * @return
	 */
	public static void selectTarget(
			final MenuDialogPlugin plugin, 
			final NFClosure<SectorEntityToken, Void> closure
			){
		new Script() {
			@Override
			public void run() {
				plugin.getVisualPanel().showCore(CoreUITabId.MAP, null, true, new CoreInteractionListener() {
					@Override
					public void coreUIDismissed() {
						SectorEntityToken target = Global.getSector().getPlayerFleet().getInteractionTarget();
						closure.execute(target);
						plugin.showMenu();
					}
				});
			}
		}.run();
	}
	
	/**
	 * get the selected target
	 * @return
	 */
	public static SectorEntityToken getSelectedTarget(){
		return Global.getSector().getPlayerFleet().getInteractionTarget();
	}
	
}
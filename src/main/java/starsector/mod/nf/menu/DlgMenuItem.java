package starsector.mod.nf.menu;

import com.fs.starfarer.api.campaign.OptionPanelAPI;


/**
 * menu item for interaction dialog plugin
 * @author fengyuan
 *
 * @param <Context>
 */
public interface DlgMenuItem extends MenuItem{
	
	/**
	 * option text
	 */
	String getText();
	
	void onMouseOver();
	
	/**
	 * invoke when {@link MenuDialogPlugin} is about to show option
	 * to player.
	 * @param optionPanel
	 */
	void show(OptionPanelAPI optionPanel);
	
}

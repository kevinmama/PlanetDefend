package starsector.mod.nf.menu;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.VisualPanelAPI;

/**
 * menu-based interaction dialog
 * @author fengyuan
 *
 */
public abstract class MenuDialogPlugin implements InteractionDialogPlugin{
	
	protected InteractionDialogAPI dialog;
	protected OptionPanelAPI optionPanel;
	protected VisualPanelAPI visualPanel;
	protected TextPanelAPI textPanel;
	private boolean dispose = false;
	
	private BacktraceMenuVisitor<DlgMenu, DlgMenuItem> visitor;
	private DlgMenu topMenu;
	private DlgMenuItem backMenuItem;
	private DlgMenuItem leaveMenuItem;
	private Set<DlgMenuFactory> factories = new LinkedHashSet<DlgMenuFactory>();
	
	@Override
	public void init(InteractionDialogAPI dialog) {
		this.dialog = dialog;
		optionPanel = dialog.getOptionPanel();
		visualPanel = dialog.getVisualPanel();
		textPanel = dialog.getTextPanel();
		
		setTopMenu(new BaseDlgMenu(null));
		
		backMenuItem = new BaseDlgMenuItem("back", null, Keyboard.KEY_BACK){
			@Override
			public void onSelect(Object context) {
				visitor.back();
			}
		};
		
		leaveMenuItem = new BaseDlgMenuItem("leave", null, Keyboard.KEY_ESCAPE){
			public void onSelect(Object context) {
				dispose();
			};
		};
		
		dialog.setOptionOnEscape(leaveMenuItem.getText(), leaveMenuItem);
		
		init();
		
		for (DlgMenuFactory factory : factories) {
			factory.create(this);
		}
		
		showMenu();
	}
	
	/**
	 * do custom initialization work(eg: build menu).
	 */
	protected abstract void init(); 
	
	public void setTopMenu(DlgMenu menu){
		this.topMenu = menu;
		visitor = new BacktraceMenuVisitor<DlgMenu, DlgMenuItem>(menu);
	}
	
	public DlgMenu getTopMenu() {
		return topMenu;
	}
	
	public DlgMenuItem getBackMenuItem() {
		return backMenuItem;
	}

	public void setBackMenuItem(DlgMenuItem backMenuItem) {
		this.backMenuItem = backMenuItem;
	}

	public BacktraceMenuVisitor<DlgMenu, DlgMenuItem> getVisitor() {
		return visitor;
	}
	
	public DlgMenuItem getLeaveMenuItem() {
		return leaveMenuItem;
	}

	public void setLeaveMenuItem(DlgMenuItem leaveMenuItem) {
		this.leaveMenuItem = leaveMenuItem;
	}

	public void setVisitor(BacktraceMenuVisitor<DlgMenu, DlgMenuItem> visitor) {
		this.visitor = visitor;
	}

	@Override
	public void optionSelected(String optionText, Object optionData) {
		if (optionData != null){
			DlgMenuItem option = (DlgMenuItem) optionData;
			visitor.select(option, visitor);
		}
		
		if (!dispose){
			showMenu();
		}
	}

	@Override
	public void optionMousedOver(String optionText, Object optionData) {
		if (optionData != null){
			DlgMenuItem option = (DlgMenuItem) optionData;
			option.onMouseOver();
		}
	}

	/**
	 * update options by menu
	 */
	public void showMenu(){
		optionPanel.clearOptions();
		
		//
		// add menu sub items
		//
		
		DlgMenu cur = visitor.cur();
		Collection<DlgMenuItem> items = cur.getItems();
		for (DlgMenuItem item : items) {
			if (item.isVisible()){
				item.show(optionPanel);
			}
		}
		
		//
		// add back/leave option
		//
		if (cur == visitor.top()){
			leaveMenuItem.show(optionPanel);
		}else{
			backMenuItem.show(optionPanel);
		}
	}
	
	/**
	 * dispose the dialog and resume the game
	 */
	public void dispose(){
		dispose = true;
		Global.getSector().setPaused(false);
		dialog.dismiss();
	}

	public InteractionDialogAPI getDialog() {
		return dialog;
	}

	public OptionPanelAPI getOptionPanel() {
		return optionPanel;
	}

	public VisualPanelAPI getVisualPanel() {
		return visualPanel;
	}

	public TextPanelAPI getTextPanel() {
		return textPanel;
	}
	
	public void addMenuFactory(DlgMenuFactory factory){
		factories.add(factory);
	}
	
}

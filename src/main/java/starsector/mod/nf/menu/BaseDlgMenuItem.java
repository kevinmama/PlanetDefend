package starsector.mod.nf.menu;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.campaign.OptionPanelAPI;

public class BaseDlgMenuItem extends BaseMenuItem implements DlgMenuItem{
	
	protected String text;
	protected String tooltip;
	protected Color color;
	protected boolean enable = true;
	protected int keyCode;
	
	public BaseDlgMenuItem(String text){
		this(text, null);
	}
	
	public BaseDlgMenuItem(String text, BaseDlgMenu parent){
		this.text = text;
		if (parent != null)
			parent.addItem(this);
	}
	
	public String getText() {
		return text;
	}

	public Color getColor() {
		return color;
	}

	public String getTooltip() {
		return tooltip;
	}

	public boolean isEnable() {
		return enable;
	}

	@Override
	public void onMouseOver() {
		// do nothing
	}

	public int getKeyCode() {
		return keyCode;
	}

	@Override
	public void show(OptionPanelAPI optionPanel) {
		if (getColor() != null){
			optionPanel.addOption(getText(), this, getColor(), getTooltip());
		}else{
			if (getTooltip() != null){
				optionPanel.addOption(getText(), this, getTooltip());
			}else{
				optionPanel.addOption(getText(), this);
			}
		}
		optionPanel.setEnabled(this, isEnable());
		if (keyCode != Keyboard.KEY_NONE)
			optionPanel.setShortcut(this, getKeyCode(), false, false, false, true);
	}
	
}

package starsector.mod.nf.menu;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.VerificationsInOrder;

import org.junit.Before;
import org.junit.Test;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.VisualPanelAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;

public class MenuDialogPluginTest {
	
	private MenuDialogPlugin plugin;
	private BaseDlgMenu m1, m2, m2_3;
	
	@Mocked InteractionDialogAPI dialog;
	@Mocked VisualPanelAPI visualPanel;
	@Mocked OptionPanelAPI optionPanel;
	
	@Mocked(value={"show"})  BaseDlgMenuItem i1, i2, i3, j1, j2, k1, k2, k3;
	
	
	@Before
	public void before(){
		
		BaseMenuItem[] items = {i1, i2, i3, j1, j2, k1, k2, k3};
		for (BaseMenuItem item : items){
			item.setVisible(true);
		}
		
		plugin = new MenuDialogPlugin(){
			
			@Override
			public void init() {
				
				BaseDlgMenu top = (BaseDlgMenu) getTopMenu();
				m1 = new BaseDlgMenu("menu 1", top);
				m1.addItem(i1).addItem(i2).addItem(i3);
				
				m2 = new BaseDlgMenu("menu 2", top);
				m2_3 = new BaseDlgMenu("menu 2_3", m2){};
				m2.addItem(j1).addItem(j2).addItem(m2_3);
				
				m2_3.addItem(k1).addItem(k2).addItem(k3);
			}
			
			@Override
			public void advance(float amount) {
			}

			@Override
			public void backFromEngagement(EngagementResultAPI battleResult) {
			}

			@Override
			public Object getContext() {
				return null;
			}
		};
	}
	
	@Test
	public void test() {
		
		new NonStrictExpectations() {
			
			@Mocked SectorAPI sector;
			@Mocked Global global;
			{
				dialog.getVisualPanel();
				result = visualPanel;
				dialog.getOptionPanel();
				result = optionPanel;
				Global.getSector();
				result = sector;
			}
		};
		
		
		plugin.init(dialog);
		
		//
		// simulate user selection
		//
		plugin.optionSelected(null, null);	// init
		// m1 -> i2 -> back
		plugin.optionSelected(m1.getText(), m1);
		plugin.optionSelected(i2.getText(), i2);
		plugin.optionSelected(plugin.getBackMenuItem().getText(), plugin.getBackMenuItem());
		
		// m2 -> m2_3 -> k3 -> back -> back
		plugin.optionSelected(m2.getText(), m2);
		plugin.optionSelected(m2_3.getText(), m2_3);
		plugin.optionSelected(k3.getText(), k3);
		plugin.optionSelected(plugin.getBackMenuItem().getText(), plugin.getBackMenuItem());
		plugin.optionSelected(plugin.getBackMenuItem().getText(), plugin.getBackMenuItem());
		
		// dispose
		plugin.optionSelected(plugin.getLeaveMenuItem().getText(), plugin.getLeaveMenuItem());
		
		new VerificationsInOrder() {
			
			private VerificationsInOrder addOpt(BaseDlgMenuItem... items){
				for (BaseDlgMenuItem item : items) {
					item.show(optionPanel);
				}
				return this;
			}
			
			{
				addOpt(m1, m2);
				addOpt(i1, i2, i3);
				addOpt(m1, m2);
				addOpt(j1, j2); //, m2_3);
				addOpt(k1, k2, k3);
				addOpt(j1, j2); // m2_3);
				addOpt(m1, m2);
				dialog.dismiss();
				
			}
			
		};
	}

}

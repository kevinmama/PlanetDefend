package starsector.mod.pld.army;

import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Keyboard;

import starsector.mod.nf.menu.BaseDlgMenu;
import starsector.mod.nf.menu.BaseDlgMenuItem;
import starsector.mod.nf.menu.DlgMenuFactory;
import starsector.mod.nf.menu.DlgMenuItem;
import starsector.mod.nf.menu.MenuDialogPlugin;
import starsector.mod.nf.menu.MenuDialogUtils;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.nf.support.FleetSupport;
import starsector.mod.nf.support.NFClosure;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDArmy;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDRegistry;
import starsector.mod.pld.misc.DummyStationExchange;
import starsector.mod.pld.misc.PLDUtils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CoreInteractionListener;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.VisualPanelAPI;
import com.fs.starfarer.api.impl.campaign.FleetEncounterContext;

/**
 * army menu item factory
 * @author fengyuan
 *
 */
public class ArmyMenuFactory implements DlgMenuFactory{
	
	private ArmyOrder order = new ArmyOrder();
	
	@Override
	public void create(MenuDialogPlugin plugin0) {
		ArmyDialogPlugin plugin = (ArmyDialogPlugin) plugin0;
		PLDRegistry reg = PLD.getRegistry();
		BaseDlgMenu top = (BaseDlgMenu) plugin.getTopMenu();
		final PLDFleet playerFleet = reg.getPlayerFleet();
		final CampaignFleetAPI playerFleet0 = playerFleet.getFleet();
		
		//
		// reorganize
		//
		
		BaseDlgMenu reorganizeMenu = new BaseDlgMenu("reorganize", top, Keyboard.KEY_R);
		createReorganizeMenuItems(reorganizeMenu, plugin);
		
		//
		// order
		//
		
		BaseDlgMenu orderMenu = new BaseDlgMenu("order your fleets", top, Keyboard.KEY_O);
		createOrderMenuItems(orderMenu, plugin);
	}
	
	
	/**
	 * create reorganize menu items
	 * @param dialog
	 * @return
	 */
	public DlgMenuItem[] createReorganizeMenuItems(BaseDlgMenu parent, final ArmyDialogPlugin dialog){
		
		class ReorganizeMenuItem extends BaseDlgMenuItem{
			
			public ReorganizeMenuItem(String text, BaseDlgMenu parent, int keyCode) {
				super(text, parent, keyCode);
			}

			@Override
			public boolean isVisible() {
				return dialog.getAnother() != null;
			}
		}
		
		final Script refreshMenuScript = new Script() {
			@Override
			public void run() {
				dialog.showMenu();
			}
		};

		
		final PLDFleet playerFleet = PLD.getRegistry().getPlayerFleet();
		final CampaignFleetAPI playerFleet0 = playerFleet.getFleet();
		final PLDArmy playerArmy = PLD.getRegistry().getPlayerArmy();
		
		
		return new DlgMenuItem[] {
			
			new BaseDlgMenuItem("divide your fleet", parent, Keyboard.KEY_D){
				public boolean isVisible() {
					return dialog.getAnother() == null; 
				}
				@Override
				public void onSelect(Object context) {
					final PLDFleet detachmentFleet = PLDUtils.createEmptyDetachment(playerArmy);
					DummyStationExchange exchange = new DummyStationExchange("detachment", detachmentFleet.getFleet(), true);
					exchange.showExchange(dialog.getVisualPanel(), CoreUITabId.FLEET, new Script() {
						@Override
						public void run() {
							CampaignFleetAPI fleet = detachmentFleet.getFleet();
							if (!fleet.getFleetData().getMembersListCopy().isEmpty()){
								// have at least a ship
								LocationAPI loc = Global.getSector().getCurrentLocation();
								loc.spawnFleet(playerFleet0, 0, 0, fleet);
								dialog.setAnother(detachmentFleet);
								refreshMenuScript.run();
							}
						}
					});
				}
			},
			
			new ReorganizeMenuItem("show fleet information", parent, Keyboard.KEY_O){
				@Override
				public void onSelect(Object context) {
					CampaignFleetAPI another0 = dialog.getAnother().getFleet();
					List<String> info = FleetSupport.getFleetInfo(another0);
					TextPanelAPI text = dialog.getTextPanel();
					for (String line : info) {
						text.addParagraph(line);
					}
					VisualPanelAPI visual = dialog.getVisualPanel();
					visual.showFleetInfo(playerFleet0.getName(), playerFleet0, another0.getName(), another0, new FleetEncounterContext());
				}
			},
			
			new ReorganizeMenuItem("exchange resources and weapons", parent, Keyboard.KEY_I){
				@Override
				public void onSelect(Object context) {
					PLDFleet another = dialog.getAnother();
					CampaignFleetAPI another0 = another.getFleet();
					DummyStationExchange exchange = new DummyStationExchange(another0.getName(), another0, false);
					exchange.showExchange(dialog.getVisualPanel(), CoreUITabId.CARGO, refreshMenuScript);
				}
			},
			
			new ReorganizeMenuItem("exchange ships", parent, Keyboard.KEY_F){
				@Override
				public void onSelect(Object context) {
					PLDFleet another = dialog.getAnother();
					CampaignFleetAPI another0 = another.getFleet();
					DummyStationExchange exchange = new DummyStationExchange(another0.getName(), another0, false);
					exchange.showExchange(dialog.getVisualPanel(), CoreUITabId.FLEET, refreshMenuScript);
				}
			},
			
			new ReorganizeMenuItem("merge your fleets", parent, Keyboard.KEY_M){
				public boolean isVisible() {
					return dialog.getAnother() != null;
				}
				public void onSelect(Object context) {
					PLDFleet another = dialog.getAnother();
					CampaignFleetAPI another0 = another.getFleet();
					CargoSupport.takeAll(playerFleet0, another0);
					dialog.setAnother(null);
				};
			},
			
		};
	}
	
	
	private class OrderMenuItem extends BaseDlgMenuItem{
		public OrderMenuItem(String text, BaseDlgMenu parent, int keyCode) {
			super(text, parent, keyCode);
		}
		@Override
		public boolean isVisible() {
			return order.hasFleet();
		}
	}
	
	private class OrderMenu extends BaseDlgMenu{
		public OrderMenu(String text, BaseDlgMenu parent, int keyCode) {
			super(text, parent, keyCode);
		}
		@Override
		public boolean isVisible() {
			return order.hasFleet();
		}
	}

	
	
	
	/**
	 * create fleet selection menu item and give order menu
	 * @param plugin
	 * @return
	 */
	private DlgMenuItem[] createOrderMenuItems(BaseDlgMenu parent, final ArmyDialogPlugin plugin){
		
		OrderMenu giveOrderMenu = new OrderMenu("give orders", parent, Keyboard.KEY_G);
		createOrderAction(giveOrderMenu, plugin);
		
		return new DlgMenuItem[]{
				
			new BaseDlgMenuItem("add a fleet to order", parent, Keyboard.KEY_D){
				@Override
				public void onSelect(Object context) {
					MenuDialogUtils.selectTarget(plugin, new NFClosure<SectorEntityToken, Void>() {
						@Override
						public Void execute(SectorEntityToken target) {
							if (PLDUtils.isInPlayerArmy(target)){
								order.addFleet(PLD.getRegistry().getFleet((CampaignFleetAPI)target));
								TextPanelAPI text = plugin.getTextPanel();
								order.showSelectedFleets(text);
							}
							return null;
						}
					});
				}
			},
			
			new OrderMenuItem("remove a selected fleet", parent, Keyboard.KEY_R){
				@Override
				public void onSelect(Object context) {
					MenuDialogUtils.selectTarget(plugin, new NFClosure<SectorEntityToken, Void>() {
						@Override
						public Void execute(SectorEntityToken target) {
							if (PLDUtils.isInPlayerArmy(target)){
								order.removeFleet(PLD.getRegistry().getFleet((CampaignFleetAPI) target));
								TextPanelAPI text = plugin.getTextPanel();
								order.showSelectedFleets(text);
							}
							return null;
						}
					});
				}
			},
				
			new BaseDlgMenuItem("select all except player fleet", parent, Keyboard.KEY_A){
				@Override
				public void onSelect(Object context) {
					PLDRegistry reg = PLD.getRegistry();
					Collection<PLDFleet> fleets = reg.getPlayerArmy().getFleetsCopy();
					fleets.remove(reg.getPlayerFleet());
					order.addFleets(fleets);
					order.showSelectedFleets(plugin.getTextPanel());
				}
			},
			
			new OrderMenuItem("clear selected fleets", parent, Keyboard.KEY_C){
				public void onSelect(Object context) {
					order.clearFleets();
					order.showSelectedFleets(plugin.getTextPanel());
				};
			},
			
			giveOrderMenu
		};
	}

	
	/**
	 * create detail order action
	 * @param parent
	 * @param plugin
	 * @return
	 */
	private DlgMenuItem[] createOrderAction(BaseDlgMenu parent, final ArmyDialogPlugin plugin){
		return new DlgMenuItem[]{
			new OrderMenuItem("follow target", parent, Keyboard.KEY_F){
				public void onSelect(Object context) {
					MenuDialogUtils.selectTarget(plugin, new NFClosure<SectorEntityToken, Void>() {
						
						@Override
						public Void execute(SectorEntityToken target) {
							if (target != null){
								order.follow(target);
								plugin.getTextPanel().addParagraph("follow target: " + target.getName());
								plugin.showMenu();
							}
							return null;
						}
					});
				};
			},
			
			new OrderMenuItem("call", parent, Keyboard.KEY_C){
				public void onSelect(Object context) {
					order.call();
				};
			},
			
			new OrderMenuItem("stop in my position", parent, Keyboard.KEY_S){
				public void onSelect(Object context) {
					order.stopHere();
				};
			},
			
			new OrderMenuItem("stop", parent, Keyboard.KEY_H){
				public void onSelect(Object context) {
					order.stop();
				};
			},
			
			new OrderMenuItem("go free", parent, Keyboard.KEY_E){
				public void onSelect(Object context) {
					order.gofree();
				};
			}
		};
	}
	
}

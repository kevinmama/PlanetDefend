package starsector.mod.pld.army;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import starsector.mod.nf.support.FleetSupport;
import starsector.mod.nf.support.NFClosure;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDFleet;

import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;

/**
 * record command 
 * @author fengyuan
 *
 */
public class ArmyOrder {
	
	private Set<PLDFleet> fleets = new LinkedHashSet<PLDFleet>();
	
	public void addFleet(PLDFleet fleet){
		fleets.add(fleet);
	}
	
	public void addFleets(Collection<PLDFleet> fleets){
		this.fleets.addAll(fleets);
	}
	
	public void removeFleet(PLDFleet fleet){
		this.fleets.remove(fleet);
	}
	
	public void clearFleets(){
		fleets.clear();
	}
	
	public boolean hasFleet(){
		return !fleets.isEmpty();
	}
	
	public Set<PLDFleet> getAliveFleets(){
		foreachAliveFleet(null);	// remove the dead fleets
		return fleets; 
	}
	
	public void showSelectedFleets(TextPanelAPI textPanel){
		textPanel.addParagraph("selected fleets: ");
		boolean first = true;
		for (PLDFleet fleet : getAliveFleets()) {
			if (first){
				first = false;
			}else{
				textPanel.appendToLastParagraph(" , ");
			}
			textPanel.appendToLastParagraph(fleet.getQualifiedName());
		}
	}
	
	
	private void foreachAliveFleet(NFClosure<CampaignFleetAPI, Void> closure){
		for (PLDFleet fleet : fleets) {
			CampaignFleetAPI fleet0 = fleet.getFleet();
			if (!fleet0.isAlive()){
				fleet.dispose();
				continue;
			}else{
				if (closure != null)
					closure.execute(fleet.getFleet());
			}
		}
	}
	
	/**
	 * follow a target
	 * @param tk
	 */
	public void follow(final SectorEntityToken tk){
		foreachAliveFleet(new NFClosure<CampaignFleetAPI, Void>() {
			@Override
			public Void execute(CampaignFleetAPI fleet0) {
				fleet0.clearAssignments();
				FleetSupport.follow(fleet0, tk);
				return null;
			}
		});
	}
	
	/**
	 * call fleets to your position
	 */
	public void call(){
		foreachAliveFleet(new NFClosure<CampaignFleetAPI,Void>() {
			@Override
			public Void execute(CampaignFleetAPI fleet0) {
				fleet0.clearAssignments();
				FleetSupport.follow(fleet0, PLD.getRegistry().getPlayerFleet().getFleet());
				return null;
			}
		});
	}
	
	/**
	 * move to your current position
	 */
	public void stopHere(){
		foreachAliveFleet(new NFClosure<CampaignFleetAPI, Void>(){
			@Override
			public Void execute(CampaignFleetAPI input) {
				final CampaignFleetAPI fleet0 = (CampaignFleetAPI) input;
				fleet0.clearAssignments();
				FleetSupport.gotoloc(fleet0, PLD.getRegistry().getPlayerFleet().getFleet(), new Script() {
					
					@Override
					public void run() {
						fleet0.clearAssignments();
						FleetSupport.stop(fleet0);
					}
				});
				return null;
			}
		});
	}
	
	/**
	 * stop moving
	 */
	public void stop(){
		foreachAliveFleet(new NFClosure<CampaignFleetAPI, Void>() {
			@Override
			public Void execute(CampaignFleetAPI fleet0) {
				fleet0.clearAssignments();
				FleetSupport.stop(fleet0);
				return null;
			}
		});
	}
	
	/**
	 * let targets free
	 */
	public void gofree(){
		foreachAliveFleet(new NFClosure<CampaignFleetAPI, Void>(){
			@Override
			public Void execute(CampaignFleetAPI fleet0) {
				fleet0.clearAssignments();
				return null;
			}
		});
	}
	
}

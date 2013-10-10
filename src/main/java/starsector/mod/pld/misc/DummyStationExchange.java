package starsector.mod.pld.misc;

import starsector.mod.nf.support.CargoSupport;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CoreInteractionListener;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.VisualPanelAPI;

/**
 * exchange to another fleet by dummy station
 * @author fengyuan
 *
 */
public class DummyStationExchange{
	
	private OrbitalStationAPI dummy;
	private CampaignFleetAPI source;
	private boolean includeFlagShip;
	
	public DummyStationExchange(String name, CampaignFleetAPI source, boolean includeFlagShip) {
		if (name == null)
			name = source.getName();
		dummy = createDummyStation(name);
		this.source = source;
		this.includeFlagShip = includeFlagShip;
		CargoSupport.setCargo(dummy.getCargo(), source, includeFlagShip);	// sync cargo to dummy
	}
	
	/**
	 * sync station's cargo and ships to fleet
	 * @param fleet
	 * @param dummyStation
	 * @param includeFlagShip
	 * @return
	 */
	public void sync(){
		CargoSupport.clear(source, includeFlagShip);
		CargoSupport.takeAll(source, dummy.getCargo());
		removeDummyStation(dummy);
	}
	
	/**
	 * create a dummy location the access the item and fleet exchange UI,
	 * user should quickly remove it to prevent AI do some strange actions.
	 * @return
	 */
	private OrbitalStationAPI createDummyStation(String name){
		SectorAPI sector = Global.getSector();
		LocationAPI sys = sector.getCurrentLocation();
		return (OrbitalStationAPI) sys.addOrbitalStation(sector.getPlayerFleet(), 0, 100, 100, name, "neutral");
	}
	
	/**
	 * remove dummy station and its containning system
	 * @param dummy
	 */
	private void removeDummyStation(OrbitalStationAPI dummy){
		LocationAPI sys = dummy.getContainingLocation();
		sys.removeEntity(dummy);
	}
	
	public void showExchange(final VisualPanelAPI visual, CoreUITabId type, final Script onCompletion){
		visual.showCore(type, dummy, true, new CoreInteractionListener() {
			@Override
			public void coreUIDismissed() {
				sync();
				if (onCompletion != null)
					onCompletion.run();
				CargoSupport.renewFleetMembers(Global.getSector().getPlayerFleet());
			}
		});
	}
	
}

package starsector.mod.nf.cmd;

import java.util.List;

import org.lazywizard.lazylib.campaign.MessageUtils;

import starsector.mod.nf.NFSettings;
import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.event.KeyPressEvent;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;


/**
 * roundly check frendly fleets
 * @author fengyuan
 *
 */
public class CheckFrendlyFleetCmd extends AbstractDebugCommand{
	
	@Override
	public int getTriggerKeyCode() {
		return NFSettings.KEY_BINDING_CHECK_FLEET;
	}

	@Override
	public void execute(KeyPressEvent event, EventBus eventbus) {
		SectorAPI sector = Global.getSector();
		LocationAPI location = sector.getCurrentLocation();
		List<CampaignFleetAPI> fleets = location.getFleets();
		for (CampaignFleetAPI fleet : fleets) {
			// report fleet cargos
			String name = fleet.getFullName();
			// meta datas
			FactionAPI faction = fleet.getFaction();
			FleetDataAPI data = fleet.getFleetData();
			float fleetPointsUsed = data.getFleetPointsUsed();
			
			// cargos
			CargoAPI cargo = fleet.getCargo();
			float supplies = cargo.getSupplies();
			float fuel = cargo.getFuel();
			int crews = cargo.getTotalCrew();
			int marines = cargo.getMarines();
			
			float maxCapacity = cargo.getMaxCapacity();
			float maxFuel = cargo.getMaxFuel();
			float maxPersonnel = cargo.getMaxPersonnel();
			
			String msg = String.format("fleet[%s]: data=(%s, %.2f) cargo=(%.0f/%.0f, %.0f/%.0f, %d/%.0f, %d/%.0f)", 
					name, 
					faction.getId(), fleetPointsUsed,
					supplies, maxCapacity, fuel, maxFuel, crews, maxPersonnel, marines, maxPersonnel);
			MessageUtils.showMessage(msg);
		}
	}

	@Override
	public boolean isActive() {
		return NFSettings.debugging;
	}

}

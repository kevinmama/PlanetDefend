package starsector.mod.pld.camp;

import java.util.List;

import org.apache.log4j.Logger;

import starsector.mod.nf.support.CargoQuantityParams;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.pld.Names;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.characters.CharacterCreationPlugin;


/**
 * create character and store character informations
 * @author fengyuan
 *
 */
public class PLDCharacterCreationPlugin implements CharacterCreationPlugin{
	
	private Logger log;

	public PLDCharacterCreationPlugin() {
		log = Global.getLogger(PLDCharacterCreationPlugin.class);
	}
	
	@Override
	public String getPrompt() {
		return null;
	}

	@Override
	public List<Response> getResponses() {
		return null;
	}

	@Override
	public void submit(Response response, CharacterCreationData data) {
	}

	@Override
	public void startingShipPicked(String variantId, CharacterCreationData data) {
		log.info("set starting location to " + Names.SYSTEM_ATHENA);
		
		// add a location, advoiding exception
		// since system is not created, we can only use system name to be created.
		// be careful!!!
		data.setStartingLocationName(Names.SYSTEM_ATHENA);
		data.getStartingCoordinates().set(1000, 1000);
		
		CargoAPI cargo = data.getStartingCargo();
		CargoQuantityParams params = new CargoQuantityParams();
		params.credits = 5000;
		params.supplies = 10;
		params.fuel = 5;
		params.greenCrew = 4;
		params.regularCrew = 3;
		params.veteranCrew = 2;
		params.eliteCrew = 1;
		params.marines = 5;
		CargoSupport.setCargo(cargo, params, null, null);
	}

}

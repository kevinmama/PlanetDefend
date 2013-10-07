package starsector.mod.pld;


import static org.apache.commons.collections.IteratorUtils.filteredIterator;
import static starsector.mod.nf.support.MathSupport.generateRandomVectorWithinInterval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.json.JSONException;
import org.json.JSONObject;

import starsector.mod.nf.log.Logger;
import starsector.mod.nf.support.CargoQuantityParams;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.nf.support.CollectionSupport;
import starsector.mod.nf.support.Distribution;
import starsector.mod.nf.support.MathSupport;
import starsector.mod.nf.support.OrbitalStationGenerationParams;
import starsector.mod.nf.support.RandomAsteroidGenerationParams;
import starsector.mod.nf.support.RandomPlanetGenerationParams;
import starsector.mod.nf.support.SectorGenerationAdapter;
import starsector.mod.nf.support.SectorGenerationSupport;
import starsector.mod.pld.domain.PLDRegistry;

import com.fs.starfarer.api.FactoryAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;


/**
 * the main star system of PLD sector
 * @author fengyuan
 *
 */
public class AthenaSystemGenerator {
	
	private static Logger log = Logger.getLogger(AthenaSystemGenerator.class);
	
	private PLDRegistry registry;
//	private EventBus eventbus;
	private StarSystemAPI athena;
	private PlanetAPI star;
	private RandomDataGenerator rdgen = new RandomDataGenerator();
	private FactionAPI playerFaction;
	private String[] knownFactionIds = new String[]{
			"hegemony", 
			"pirates", 
//			"sindrian_diktat", 
//			"tritachyon"
		};
	private List<PlanetAPI> planets;
	
	public void generate(){
		try{
			this.registry = PLD.getRegistry();
//			this.eventbus = nf.getEventbus();
			SectorAPI sector = Global.getSector();
			playerFaction = sector.getFaction("player");
			generateStarSystem();
			generateAsteroidBelts();
			generatePlanets();
			generateStations();
			athena.autogenerateHyperspaceJumpPoints(true, true);
			
			sector.setRespawnLocation(athena);
			sector.getRespawnCoordinates().set(0, 0);
//			sector.getRespawnCoordinates().set(station.getLocation());
			
		}catch(Exception e){
			log.error("generate athena system error", e);
			throw new RuntimeException(e);
		}
	}
	
	
	private void generateStarSystem(){
		SectorAPI sector = Global.getSector();
		athena = sector.createStarSystem(Names.SYSTEM_ATHENA);
		athena.getLocation().set(1000, 1000);
		athena.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");
		star = athena.initStar("star_red", 500f);
		registry.athena = athena;
	}
	
	
	private void generateAsteroidBelts(){
		//
		// generate three asteroid belt
		//
		RandomAsteroidGenerationParams params = new RandomAsteroidGenerationParams();
		params.system = athena;
		params.focus = star;
		params.baseAsteroidNumPerBelt = 100;
		params.baseWidth = 300;
		params.baseOrbitRadius = 1500;
		params.baseOrbitDays = 300;
		params.possibleOrbitRadiusDistribution = Distribution.GAUSSIAN;
		
		params.num = 2;
		params.possibleMinOrbitRadius = 1000;
		params.possibleMaxOrbitRadius = 2000;
		SectorGenerationSupport.generateRandomAsteroidBelt(params, null);
		
		params.num = 3;
		params.possibleMinOrbitRadius = 5000;
		params.possibleMaxOrbitRadius = 7000;
		SectorGenerationSupport.generateRandomAsteroidBelt(params, null);
		
		
		params.num = 4;
		params.possibleMinOrbitRadius = 10000;
		params.possibleMaxOrbitRadius = 13000;
		SectorGenerationSupport.generateRandomAsteroidBelt(params, null);
	}
	
	
	/**
	 * for each faction, create its planet.
	 */
	private void generatePlanets() throws Exception{
		
		//
		// 1. load available planet names from planet_name.csv
		// 2. load available planet types, with special planet type removed.
		//
		List<String> availablePlanetNames = PLDSettings.loadPlanetNames();
		final JSONObject availablePlanetTypes = PLDSettings.loadPlanetTypes();
		
		//
		// remove special planet types
		//
		@SuppressWarnings("unchecked")
		List<String> planetTypeIds = IteratorUtils.toList(filteredIterator(availablePlanetTypes.keys(), new Predicate() {
			@Override
			public boolean evaluate(Object key) {
				try {
					JSONObject data = availablePlanetTypes.getJSONObject((String)key);
					return !data.optBoolean("isStar", false) && !data.optBoolean("isGasGiant", false);
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
			}
		}));
		
		//
		// randomly pick ${knownFactionIds.lenths} names
		//
		int[] randomIndex = MathSupport.round(generateRandomVectorWithinInterval(knownFactionIds.length, 0, availablePlanetNames.size()-1, Distribution.UNIFORM));
		availablePlanetNames = CollectionSupport.select(availablePlanetNames, randomIndex);		
		
		//
		// generate planet for each known faction
		//
		RandomPlanetGenerationParams params = new RandomPlanetGenerationParams();
		params.num = knownFactionIds.length;
		params.system = athena;
		params.focus = star;
		params.availableNames = availablePlanetNames;
		params.availableTypes = planetTypeIds;
		params.baseRadius = 200;
		params.baseOrbitRadius = 1500;
		params.baseOrbitDays = 150;
		params.possibleMinOrbitRadius = 7000;
		params.possibleMaxOrbitRadius = 10000;
		params.possibleOrbitRadiusDistribution = Distribution.LINEAR;
		
		planets = SectorGenerationSupport.generateRandomPlanet(params, null);
	}
	
	/**
	 * generate a station per normal planet
	 */
	private void generateStations() {
		
		List<String> knownFactionIds = new ArrayList<String>(Arrays.asList(this.knownFactionIds));
		Collections.shuffle(knownFactionIds);
		
		int i = 0;
		for (PlanetAPI planet : planets) {
			OrbitalStationGenerationParams params = new OrbitalStationGenerationParams();
			params.system = athena;
			params.focus = planet;
			params.name = planet.getName() + " Station";
			params.factionId = knownFactionIds.get(i++);
			params.baseOrbitRadius = planet.getRadius() + 100;
			params.baseOrbitDays = 30;
			params.onStar = false;
			params.onMoon = false;
			params.onGasGaint = false;
			SectorGenerationSupport.generateOrbitalStation(params, new SectorGenerationAdapter<OrbitalStationGenerationParams, OrbitalStationAPI>(){
				@Override
				public void intercept(OrbitalStationAPI station) {
					initStationCargos(station);
					PLD.getFactory().createPLDStation(station);
				}
			});
		}
	}
	
	private void initStationCargos(OrbitalStationAPI station){
		FactoryAPI factory = Global.getFactory();
		// init cargo
		CargoAPI cargo = station.getCargo();
		cargo.setFreeTransfer(true);
		CargoQuantityParams quantity = new CargoQuantityParams();
		quantity.credits = 100000;
		quantity.supplies = 5000;
		quantity.fuel = 3000;
		quantity.marines = 500;
		quantity.greenCrew = 1000;
		quantity.regularCrew = 300;
		quantity.veteranCrew = 200;
		quantity.eliteCrew = 100;
		CargoSupport.setCargo(cargo, quantity, null, null);
		
		// init ship
		String[] shipVariantIds = {
				"drone_assault",
				"drone_pd_midline",
				"drone_pd",
				"drone_sensor",
				"drone_terminator",
		};
		for (String id : shipVariantIds) {
			FleetMemberAPI ship = factory.createFleetMember(FleetMemberType.SHIP, id);
			cargo.getMothballedShips().addFleetMember(ship);
		}
	}
	
}

package starsector.mod.pld;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;
import starsector.mod.nf.support.SettingSupport;

import com.fs.starfarer.api.Global;


/**
 * settings of PLD. See 'planet_defend.json' for comment. 
 * @author fengyuan
 *
 */
public class PLDSettings {
	
	private static final Logger log = Logger.getLogger(PLDSettings.class, AppenderType.LOG4J);
	
	//==============================================================================
	// key bindings
	//==============================================================================
	public static int KEYBINDING_ARMY = Keyboard.KEY_A;
	
	
	/**
	 * 1 unit per hour per miner
	 */
	public static int MINING_RATE = 1;
	
	/**
	 * mining fleet variant id
	 */
	public static String MINING_FLEET_VARIANT_ID = "asteroidMiningFleet";
	

	/**
	 * supplies cost by spawining mining fleet
	 */
	public static int MINING_FLEET_SPAWN_COST = 1000;
	
	/**
	 * days to spawining mining fleet
	 */
	public static int MINING_FLEET_SPAWN_DAY = 7;
	
	/**
	 * mining fleet limit per station
	 */
	public static int MINING_FLEET_LIMIT_PER_STATION = 3;
	
	/**
	 * how many supply after deliver, reserve for travelling.
	 */
	public static int MINING_FLEET_SUPPLY_RESERVE = 50;
	
	//==============================================================================
	// salary
	//==============================================================================
	
	public static int SALARY_OFFICER_BASE = 250;
	public static TreeMap<Integer, Integer> SALARY_OFFICER_MULTIPLIER;
	
	/** marines and crew's salary every week */
	public static int[] SALARY_PEOPLE_VECTOR = {25, 1, 4, 8, 25};

	//==============================================================================
	// station supplies
	//==============================================================================
	
	/**
	 * sation supply level, affect crew grow and convert rate, 
	 */
	public static float[] STATION_SUPPLY_LEVEL_VECTOR = {0f, 2000f, 4000f, 6000f, 8000f, 10000f};
	public static int STATION_SUPPLIES_LIMIT = 20000;
	public static float[] STATION_PEOPLE_PRODUCE_RATE_VECTOR = {-0.08f, 0.01f, 0.02f, 0.05f, 0.1f};
	
	//==============================================================================
	// crew grows
	//==============================================================================
	
	/** number of new crew every day at each supply level */
	public static int[] STATION_CREW_GROW_BASE_VECTOR = {-10, -2, 1, 5, 10};
	
	/** rate base on current total crews */
	public static float[] STATION_CREW_GROW_RATE_VECTOR = {-0.1f, -0.01f, 0.0070f, 0.0137f, 0.0264f};
	
	/** crew level up rate */
	public static float[] STATION_CREW_CONVERT_RATE_VECTOR = {0f, 0f, 0.0070f, 0.0137f, 0.0264f};
	
	/** marine */
	public static float[] STATION_MARINE_CONVERT_RATE_VECTOR = {-0.2f, -0.01f, 0f, 0.0137f, 0.0264f};
	public static int STATION_CREW_LIMIT = 10000;
	public static int STATION_MARINES_LIMIT = 3000;
	
	
	//==============================================================================
	// misc
	//==============================================================================
	
	/**
	 * dummy star system name, should be enough special to avoid conflict.
	 */
	public static String DUMMY_STAR_SYSTEM_NAME = "DUMMY#PLD#FENGYUAN#NF#DUMMY";
	
	/**
	 * 1. update the static reference
	 * 2. reload setting from 'planet_defend.json',
	 * but some settings only become active after start new game.
	 */
	public static void reload(){
		
		try {
			JSONObject jso = Global.getSettings().loadJSON("data/config/planet_defend.json");
			
			//
			// mining
			//
			
			JSONObject mining = jso.getJSONObject("mining");
			MINING_RATE = mining.getInt("mining_rate");
			MINING_FLEET_VARIANT_ID = mining.getString("mining_fleet_variant_id");
			MINING_FLEET_SPAWN_DAY = mining.getInt("mining_fleet_spawn_day");
			MINING_FLEET_SPAWN_COST = mining.getInt("mining_fleet_spawn_cost");
			MINING_FLEET_LIMIT_PER_STATION = mining.getInt("mining_fleet_limit_per_station");
			MINING_FLEET_SUPPLY_RESERVE = mining.getInt("mining_fleet_supply_reserve");
			
			//
			// salary
			//
			{ 
				JSONObject salary = jso.getJSONObject("salary");
				SALARY_OFFICER_BASE = salary.getInt("officer_base");
				JSONObject multiplier = salary.getJSONObject("officer_multiplier");
				SALARY_OFFICER_MULTIPLIER = new TreeMap<Integer, Integer>();
				Iterator<?> keys = multiplier.keys();
				while (keys.hasNext()){
					String key = (String) keys.next();
					int factor = multiplier.getInt(key);
					int level = Integer.parseInt(key);
					SALARY_OFFICER_MULTIPLIER.put(level, factor);
				}
			}
		} catch (Exception e) {
			log.error("reload PLD settiongs error", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * load the planet name list
	 * @return
	 */
	public static final List<String> loadPlanetNames() {
		try {
			return SettingSupport.readLines("data/strings/planet_names.dat");
		} catch (Exception e) {
			log.error("load Planet Names error", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * load planet types from core mod
	 * @return
	 */
	public static final JSONObject loadPlanetTypes(){
		try {
			return Global.getSettings().loadJSON("data/config/planets.json");
		} catch (Exception e) {
			log.error("load Planet Types error", e);
			throw new RuntimeException(e);
		}
	}
	
}

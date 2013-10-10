package starsector.mod.pld.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;

/**
 * hold the custom game objects
 * @author fengyuan
 *
 */
public class PLDRegistry {
	
	private final AtomicLong nextGId = new AtomicLong(1);
	public StarSystemAPI athena;
	
	enum Type {
		GLOBAL,
		STATION,
		FLEET,
		ARMY
	}
	
	/**
	 * global registry
	 */
	Map<Long, PLDObject> objectRegistry = new ConcurrentHashMap<Long, PLDObject>();
	
	/**
	 * station registry, index by qualified name.
	 */
	Map<OrbitalStationAPI, PLDStation> stationRegistry = new ConcurrentHashMap<OrbitalStationAPI, PLDStation>();
	
	/**
	 * fleet registry, index by qualified name
	 */
	Map<CampaignFleetAPI, PLDFleet> fleetRegistry = new ConcurrentHashMap<CampaignFleetAPI, PLDFleet>();
	
	/**
	 * army registry
	 */
	Map<Long, PLDArmy> armyRegistry = new ConcurrentHashMap<Long, PLDArmy>();
	
	
	/**
	 * player fleet
	 */
	PLDFleet playerFleet = null;
	
	/**
	 * player army
	 */
	PLDArmy playerArmy = null;
	
	/**
	 * register PLD game object
	 * @param object
	 * @return if the name registry has record before, return it.
	 */
	void register(PLDObject object, Type... types){
		object.setGid(nextGId.getAndIncrement());
		objectRegistry.put(object.getGid(), object);
		for (Type type : types) {
			switch (type) {
//			case GLOBAL: objectRegistry.put(object.getGid(), object);break;
			case STATION: stationRegistry.put(((PLDStation)object).getStation(), (PLDStation) object);break;
			case FLEET: fleetRegistry.put(((PLDFleet) object).getFleet(), (PLDFleet) object);break;
			case ARMY: armyRegistry.put(object.getGid(), (PLDArmy)object);break;
			default:
				break;
			}
		}
	}
	
	/**
	 * unregister PLD game object
	 */
	void unregister(PLDObject object, Type... types){
		objectRegistry.remove(object.getGid());
		for (Type type : types) {
			switch (type) {
//			case GLOBAL: objectRegistry.remove(object.getGid()); break;
			case STATION: stationRegistry.remove(object); break;
			case FLEET: fleetRegistry.remove(object); break;
			case ARMY: armyRegistry.remove(object.getGid()); break;
			default:
				break;
			}
		}
	}
	
	/**
	 * get Object by Id
	 * @return
	 */
	public PLDObject get(long gid){
		return objectRegistry.get(gid);
	}
	
	public PLDFleet getFleet(CampaignFleetAPI fleet){
		return fleetRegistry.get(fleet);
	}
	
	public PLDStation getStation(OrbitalStationAPI station){
		return stationRegistry.get(station);
	}
	
	public PLDArmy getArmy(long gid){
		return armyRegistry.get(gid);
	}
	
	/**
	 * get all registered stations
	 * @return
	 */
	public Collection<PLDStation> getAllStations(){
		return stationRegistry.values();
	}
	
	/**
	 * get all registered fleets
	 * @return
	 */
	public Collection<PLDFleet> getAllFleets(){
		return fleetRegistry.values();
	}
	
	
	public PLDFleet getPlayerFleet(){
		return playerFleet;
	}
	
	public PLDArmy getPlayerArmy(){
		return playerArmy;
	}
	
}

package starsector.mod.pld.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
	Map<Long, PLDObject> objectRegistry = new HashMap<Long, PLDObject>();
	
	/**
	 * station registry, index by qualified name.
	 */
	Map<String, PLDStation> stationRegistry = new HashMap<String, PLDStation>();
	
	/**
	 * fleet registry, index by qualified name
	 */
	Map<String, PLDFleet> fleetRegistry = new HashMap<String, PLDFleet>();
	
	/**
	 * army registry
	 */
	Map<String, PLDArmy> armyRegistry = new HashMap<String, PLDArmy>();
	
	
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
			case STATION: stationRegistry.put(object.getQualifiedName(), (PLDStation) object);break;
			case FLEET: fleetRegistry.put(object.getQualifiedName(), (PLDFleet) object);break;
			case ARMY: armyRegistry.put(object.getQualifiedName(), (PLDArmy)object);break;
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
			case STATION: stationRegistry.remove(object.getQualifiedName()); break;
			case FLEET: fleetRegistry.remove(object.getQualifiedName()); break;
			case ARMY: armyRegistry.remove(object.getQualifiedName()); break;
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

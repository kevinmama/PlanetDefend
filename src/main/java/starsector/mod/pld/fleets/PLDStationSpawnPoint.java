package starsector.mod.pld.fleets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.log.Logger;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDRegistry;
import starsector.mod.pld.domain.PLDStation;
import starsector.mod.pld.event.PLDEventType;
import starsector.mod.pld.event.StationEvent;
import starsector.mod.pld.event.StationSpawnedFleetsEvent;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;


/**
 * PLD station spawn point, span a fleet may cost some resource.
 * and has spawn limit for each type.
 * @author fengyuan
 *
 */
public abstract class PLDStationSpawnPoint extends BaseEventListener{
	
	private static Logger log = Logger.getLogger(PLDStationSpawnPoint.class);
	
	protected PLDRegistry reg;
	protected PLDStation station;
	
	/**
	 * maximum number of fleets
	 */
	protected int limit = 2;
	
	/**
	 * days to spawn the fleet
	 */
	protected int daysToSpawn = 7;
	
	/**
	 * is the fleet spawning
	 */
	private boolean spawning = false;
	
	/**
	 * the current spawing timestamp 
	 */
	private long spawningTimestamp = 0L;
	
	/**
	 * the spawned fleets
	 */
	private List<PLDFleet> fleets;
	
	public PLDStationSpawnPoint(PLDStation station) {
		this.station = station;
		fleets = new LinkedList<PLDFleet>();
		listen(
			CoreEventType.SECTOR_RELOAD,
			CoreEventType.HEARTBEAT_DAY
		);
	}
	
	/**
	 * check if there is enouth resources to create fleet.
	 * If enouth, remove the resources from station and return true.
	 * @param station
	 * @return
	 */
	protected abstract boolean checkAndConsumeResource();
	
	/**
	 * create fleets and assign mission.
	 * @param station
	 */
	protected Collection<PLDFleet> spawnFleets(){
		ArrayList<PLDFleet> list = new ArrayList<PLDFleet>();
		PLDFleet fleet = spawnFleet();
		if (fleet != null)
			list.add(fleet);
		return list;
	}
	
	/**
	 * create fleet and assgin mission. If you want to spawn a single fleet,
	 * you can override this method.
	 * @return
	 */
	protected PLDFleet spawnFleet(){
		return null;
	}
	
	/**
	 * you should expire all cached settings here.
	 */
	protected void reloadSettings(){}
	
	@Override
	public void handle(Event event) {
		
		if (event.getEventType() == CoreEventType.SECTOR_RELOAD){
			
			return;
		}
		
		
		SectorAPI sector = Global.getSector();
		CampaignClockAPI clock = sector.getClock();
		
		// check if fleet alive
		Iterator<PLDFleet> iter = fleets.iterator();
		while (iter.hasNext()){
			PLDFleet pldFleet = iter.next();
			CampaignFleetAPI fleet = pldFleet.getFleet();
			if (!fleet.isAlive()){
				log.info("remove fleet type: " + getName());
				pldFleet.dispose();
				iter.remove();
			}
		}
		
		if (spawning){
			// spawning fleets
			float nDays = clock.getElapsedDaysSince(spawningTimestamp);
			if (nDays >= daysToSpawn){
				spawning = false;
				Collection<PLDFleet> spawnFleets = spawnFleets();
				if (!spawnFleets.isEmpty()){
					fleets.addAll(spawnFleets);
					eventbus.raise(new StationSpawnedFleetsEvent(station, spawnFleets));
				}
			}
		}else{
			// is to spawn?
			if (fleets.size() < limit && checkAndConsumeResource()){
				spawning = true;
				spawningTimestamp = clock.getTimestamp();
				eventbus.raise(new StationEvent(PLDEventType.STATION_BEGIN_SPAWN_FLEET, station));
			}
		}
	}

}

package starsector.mod.pld.domain;

import java.util.Arrays;

import starsector.mod.pld.PLDSettings;

import com.fs.starfarer.api.campaign.OrbitalStationAPI;


/**
 * PLD station object, hold the internal station object and related objects (eg. spawn point).
 * @author fengyuan
 *
 */
public class PLDStation extends PLDObject{
	
	/**
	 * the internal station object
	 */
	private OrbitalStationAPI station;
	
	/**
	 * level of supply
	 */
	private int supplyLevel;
	
	public String getName(){
		return (String)station.getName();
	}
	
	public OrbitalStationAPI getStation() {
		return station;
	}
	
	void setStation(OrbitalStationAPI station){
		this.station = station;
	}
	
	/**
	 * get the station's supply level, supply level affects a lot of things.
	 * such crew grow and convert rate, ship production
	 * @return
	 */
	public int getSupplyLevel() {
		return supplyLevel;
	}

	/**
	 * update station status
	 */
	public void update(){
		//
		// update supply level
		//
		int level = Arrays.binarySearch(PLDSettings.STATION_SUPPLY_LEVEL_VECTOR, station.getCargo().getSupplies());
		this.supplyLevel = Math.abs(level);
	}
	
}

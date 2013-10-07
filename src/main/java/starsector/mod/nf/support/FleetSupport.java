package starsector.mod.nf.support;

import java.util.List;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.campaign.FleetUtils;
import org.lwjgl.util.vector.Vector2f;

import starsector.mod.pld.domain.PLDRegistry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;

/**
 * functions help to control fleets
 * @author fengyuan
 *
 */
public class FleetSupport {
	
	private static final int MANY_DAYS = 5000;
	
	/**
	 * 
	 * @param fleet
	 * @param assignment
	 * @param target
	 */
	public static void addAssignmentForEver(final CampaignFleetAPI fleet, final FleetAssignment assignment, final SectorEntityToken target){
		fleet.addAssignment(assignment, target, MANY_DAYS, new Script() {
			@Override
			public void run() {
				fleet.clearAssignments();
				addAssignmentForEver(fleet, assignment, target);
			}
		});
	}
	
	
	/**
	 * go to the target
	 * @param fleet
	 * @param target
	 * @param clear
	 */
	public static void gotoloc(CampaignFleetAPI fleet, SectorEntityToken target, Script onCompletion){
		fleet.addAssignment(FleetAssignment.GO_TO_LOCATION, target, MANY_DAYS, onCompletion);
	}
	
	public static void gotoloc(CampaignFleetAPI fleet, Vector2f location, Script onCompletion){
		SectorEntityToken tk = Global.getSector().getCurrentLocation().createToken(location.x, location.y);
		fleet.addAssignment(FleetAssignment.GO_TO_LOCATION, tk, MANY_DAYS, onCompletion);
	}
	
	/**
	 * also can be used for park on asteroid or station, planet.
	 * @param fleet
	 * @param target
	 * @param clear
	 * 	clear current assignments
	 */
	public static void follow(final CampaignFleetAPI fleet, final SectorEntityToken target){
		addAssignmentForEver(fleet, FleetAssignment.GO_TO_LOCATION, target);
	}
	
	/**
	 * let fleet stay
	 * @param fleet
	 */
	public static void stay(final CampaignFleetAPI fleet){
		Vector2f location = fleet.getLocation();
		SectorEntityToken tk = Global.getSector().getCurrentLocation().createToken(location.x, location.y);
		addAssignmentForEver(fleet, FleetAssignment.GO_TO_LOCATION, tk);
	}
	
	/**
	 * check if enemy in range
	 * @param fleet
	 * @param range
	 * @return
	 */
	public static boolean isEnemyInRange(CampaignFleetAPI fleet, float range){
		List<CampaignFleetAPI> enemies = FleetUtils.getEnemyFleetsInSystem(fleet);
		range = range*range;
		for (CampaignFleetAPI enemy : enemies) {
			float distance = MathUtils.getDistanceSquared(fleet, enemy);
			if (distance <= range)
				return true;
		}
		return false;
	}
	
	/**
	 * get the nearest station
	 */
	public static OrbitalStationAPI getNearestStation(CampaignFleetAPI fleet, boolean nonemeny){
		LocationAPI sys = Global.getSector().getCurrentLocation();
		List<SectorEntityToken> stations = sys.getOrbitalStations();
		if (stations != null){
			for (SectorEntityToken station : stations) {
				if (nonemeny){
					if (station.getFaction().getRelationship(fleet.getFaction().getId())<0){
						continue;
					}
				}
				return (OrbitalStationAPI) station;
			}
		}
		return null;
	}
	
}

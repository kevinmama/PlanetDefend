package starsector.mod.nf.support;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.campaign.FleetUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

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
	public static void stop(final CampaignFleetAPI fleet){
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
		OrbitalStationAPI target = null;
		if (stations != null){
			float dist = Float.MAX_VALUE;
			for (SectorEntityToken station : stations) {
				if (nonemeny){
					if (station.getFaction().getRelationship(fleet.getFaction().getId())<0){
						continue;
					}
				}
				
				float new_dist = MathUtils.getDistanceSquared(fleet, station);
				if (new_dist < dist){
					target = (OrbitalStationAPI) station;
					dist = new_dist;
				}
			}
		}
		return target;
	}
	
	/**
	 * return fleet information in lines
	 * @param fleet
	 * @return
	 */
	public static List<String> getFleetInfo(CampaignFleetAPI fleet){
		ArrayList<String> info = new ArrayList<String>();
		if (fleet == null){
			info.add("null");
			return info;
		}
		
		FleetDataAPI data = fleet.getFleetData();
		CargoAPI cargo = fleet.getCargo();
		float maxCapacity = cargo.getMaxCapacity();
		float maxFuel = cargo.getMaxFuel();
		float maxPersonnel = cargo.getMaxPersonnel();
		
		CargoQuantityParams params = CargoSupport.getCargo(cargo);
		info.add("=== fleet (" + fleet.getFullName() + ") ===");
		info.add("credits=" + params.credits);
		info.add(format("supplies={0,number,integer}/{1,number,integer}", params.supplies, maxCapacity));
		info.add(format("fuel={0,number,integer}/{1,number,integer}", params.fuel, maxFuel));
		
		//
		// compute required crews
		//
		int minCrew = 0;
		List<FleetMemberAPI> members = data.getMembersListCopy();
		for (FleetMemberAPI member : members) {
			minCrew += member.getMinCrew();
		}
		
		info.add(format("crew={0,number,integer}/{1,number,integer}, {2,number,integer} required", cargo.getTotalCrew(), maxPersonnel, minCrew));
		info.add("crew(e/v/r/g)=" + StringUtils.join(Arrays.asList(params.eliteCrew, params.veteranCrew, params.regularCrew, params.greenCrew), " / "));
		info.add(format("marines={0,number,integer}/{1,number,integer}", params.marines, maxPersonnel));
		info.add(format("LR={0,number}/{1,number}", fleet.getLogistics().getLogisticsRating()));
		return info;
	}
	
}

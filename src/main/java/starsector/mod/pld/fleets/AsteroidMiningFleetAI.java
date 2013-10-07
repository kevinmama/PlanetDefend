package starsector.mod.pld.fleets;

import java.text.MessageFormat;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;
import starsector.mod.nf.support.CampaignSupport;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.nf.support.FleetSupport;
import starsector.mod.pld.PLDSettings;
import starsector.mod.pld.domain.PLDFleet;

import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;


/**
 * mining fleet AI, repeatly go to mine and deliver to its base.
 * @author fengyuan
 *
 */
public class AsteroidMiningFleetAI extends BaseEventListener{
	
	private PLDFleet pldFleet;
	private Logger log = Logger.getLogger(AsteroidMiningFleetAI.class, AppenderType.CONSOLE);
	
	private enum MiningFleetAction {
		DEFAULT, // used to escape from enemy
		GOING_TO_MINE,
		MINING,
		RETURING,
		DELIVERING,
	}
	
	private MiningFleetAction action = MiningFleetAction.GOING_TO_MINE;
	private CampaignFleetAPI fleet;
	private OrbitalStationAPI station;
	private SectorEntityToken target;
	
	public AsteroidMiningFleetAI(PLDFleet pldFleet) {
		listen(CoreEventType.HEARTBEAT_HOUR, CoreEventType.HEARTBEAT_DAY);
		this.pldFleet = pldFleet;
		this.fleet = pldFleet.getFleet();
		station = pldFleet.getBase().getStation();
		target = null;
		this.action = MiningFleetAction.GOING_TO_MINE;
	}

	/**
	 * event base AI
	 */
	@Override
	public void handle(Event event) {
		
		if (!fleet.isAlive()){
			pldFleet.dispose();
			dispose();
		}
		
		/**
		 * 
		 */
		if (FleetSupport.isEnemyInRange(fleet, 500)){
			action = MiningFleetAction.DEFAULT;
			fleet.clearAssignments();
			return;
		}
		
		if (event.getEventType() == CoreEventType.HEARTBEAT_DAY){
			//
			// check nearest asteroid every day if going to mine
			// since the asteroid is moving, the old target may not be the nearest
			//
			if (action == MiningFleetAction.GOING_TO_MINE){
				//
				// if far from the target
				//
				if (target != null && MathUtils.getDistanceSquared(fleet, target) > 25000){
					findNearestAsteroidAndGoToMine();
				}
			}
			return;
		}
		
		switch (action) {
		case DEFAULT:
			action = MiningFleetAction.MINING;
			break;
		case GOING_TO_MINE:
			if (target == null){
				findNearestAsteroidAndGoToMine();
			}
			break;
		case MINING:{
			//
			// check if the target is near.
			//
			if (target == null || MathUtils.getDistanceSquared(fleet, target) > 10000){
				action = MiningFleetAction.GOING_TO_MINE;
			}else{
				//
				// mine until cargo full 
				// 
				CargoAPI cargo = fleet.getCargo();
				float maxCapacity = cargo.getMaxCapacity();
				if (cargo.getSupplies() >= maxCapacity){
//					log.info(getName() + " begin returning");
					target = null;	// clear the mining target
					action = MiningFleetAction.RETURING;
				}else{
					//
					// keep mining
					//
					cargo.addSupplies(fleet.getNumFighters());	// one supply per hour per unit
					fleet.clearAssignments();
					FleetSupport.follow(fleet, target);
				}
			}
			break;
		}case RETURING:{
			// return base
			fleet.clearAssignments();
			FleetSupport.gotoloc(fleet, station, new Script() {
				@Override
				public void run() {
					action = MiningFleetAction.DELIVERING;
				}
			});
			break;
		}case DELIVERING:{
			//
			// deliver one in ten resource each hour
			//
			CargoAPI cargo = fleet.getCargo();
			float supplies = cargo.getSupplies();
			if (supplies > PLDSettings.MINING_FLEET_SUPPLY_RESERVE){
				// we unload supplies
				float unloaded = - CargoSupport.addSupplies(cargo, -(cargo.getMaxCapacity()/24), PLDSettings.MINING_FLEET_SUPPLY_RESERVE, Float.MAX_VALUE);
				station.getCargo().addSupplies(unloaded);
				log.info("fleet " + fleet.getFullName() + " unloaded " + unloaded + " supplies to " + station.getFullName());
				fleet.clearAssignments();
				FleetSupport.follow(fleet, station);
			}else{
				// go to mine
				action = MiningFleetAction.GOING_TO_MINE;
			}
		}default:
			break;
		}
		
	}
	
	/**
	 * assign go to mine task to the fleet
	 */
	private void findNearestAsteroidAndGoToMine(){
		SectorEntityToken newTarget = CampaignSupport.getNearestAsteroid(fleet);
		if (target != newTarget){
			target = newTarget;
			fleet.clearAssignments();
			fleet.addAssignment(FleetAssignment.GO_TO_LOCATION, target, 1000, new Script(){
				@Override
				public void run() {
					Vector2f location = target.getLocation();
					log.info(MessageFormat.format("I reach the target({0,number},{1,number}) and start mining", location.getX(), location.getY()));
					action = MiningFleetAction.MINING;
				}
			});
		}
	}
}

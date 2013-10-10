package starsector.mod.nf.support;

import static starsector.mod.nf.support.PreDefinedCargoItemType.FUEL;
import static starsector.mod.nf.support.PreDefinedCargoItemType.MARINES;
import static starsector.mod.nf.support.PreDefinedCargoItemType.SUPPLIES;

import java.util.List;

import org.apache.commons.collections.map.MultiKeyMap;

import com.fs.starfarer.api.FactoryAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CargoItemQuantity;
import com.fs.starfarer.api.campaign.CargoAPI.CargoItemType;
import com.fs.starfarer.api.campaign.CargoAPI.CrewXPLevel;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberStatusAPI;
import com.fs.starfarer.api.util.MutableValue;

/**
 * functions to handle cargo object
 * @author fengyuan
 *
 */
public class CargoSupport {
	/**
	 * add credits
	 * @param cargo
	 * @param quantity
	 * @param reserve
	 * @param limit
	 * @param preview
	 * @return
	 */
	public static float addCredits(CargoAPI cargo, float quantity, float reserve, float limit, boolean preview){
		//
		// compute the read add quantity
		//
		if (quantity == 0){
			return 0;
		}
		
		MutableValue credits = cargo.getCredits();
		float current = credits.get();
		float realQuantity = MathSupport.getRealAddQuatity(current, quantity, reserve, limit);
		
		if (!preview){
			if (realQuantity > 0)
				credits.add(realQuantity);
			else if (realQuantity < 0){
				credits.subtract(-realQuantity);
			}
		}
		return realQuantity;
	}
	
	/**
 	 * add quantity to cargo. quantity may be negative.
	 * returns the real added signed quantity.
	 * @param cargo
	 * @param params
	 * @param preview
	 * 	if preview, just compute the quantity to add
	 * @return
	 */
	public static float addCargoItem(CargoAPI cargo, CargoItemQuantityOperationParams params, boolean preview){
		//
		// compute the read add quantity
		//
		if (params.quantity == 0){
			return 0;
		}
		float current = cargo.getQuantity(params.itemType, params.data);
		float realQuantity = MathSupport.getRealAddQuatity(current, params.quantity, params.reserve, params.limit);

		//
		// perform add
		//
		if (!preview){
			if (realQuantity > 0)
				cargo.addItems(params.itemType, params.data, realQuantity);
			else if (realQuantity < 0){
				cargo.removeItems(params.itemType, params.data, -realQuantity);
			}
		}
		return realQuantity;
	}
	
	/**
	 * add cargo item
	 * @see
	 * 	#addCargoItem(CargoAPI, CargoItemQuantityOperationParams, boolean)
	 * @param cargo
	 * @param params
	 * @param preview
	 * @return
	 */
	public static float addCargoItem(CargoAPI cargo, CargoItemQuantityOperationParams params){
		return addCargoItem(cargo, params, false);
	}
	
	/**
	 * add quantity to cargo. quantity may be negative.
	 * returns the real added signed quantity.
	 * 
	 * @param cargo
	 * @param itemType
	 * @param data
	 * @param quantity
	 * @param reserve
	 * @param limit
	 * @param preview
	 * 	if preview, just compute the quantity to add, do not perform the addition
	 * @return
	 */
	public static float addCargoItem(CargoAPI cargo, CargoItemType itemType, Object data, 
			float quantity, float reserve, float limit, boolean preview){
		
		//
		// compute the read add quantity
		//
		if (quantity == 0){
			return 0;
		}
		float current = cargo.getQuantity(itemType, data);
		float realQuantity = MathSupport.getRealAddQuatity(current, quantity, reserve, limit);

		if (!preview){
			if (realQuantity > 0)
				cargo.addItems(itemType, data, realQuantity);
			else if (realQuantity < 0){
				cargo.removeItems(itemType, data, -realQuantity);
			}
		}
		return realQuantity;
	}
	
	/**
	 * add quantity to cargo. quantity may be negative.
	 * returns the real added signed quantity.
	 * @param cargo
	 * @param itemType
	 * @param data
	 * @param quantity
	 * @param reserve
	 * @param limit
	 * @return
	 */
	public static float addCargoItem(CargoAPI cargo, CargoItemType itemType, Object data, 
			float quantity, float reserve, float limit){
		return addCargoItem(cargo, itemType, data, quantity, reserve, limit, false);
	}

	
	/**
	 * set cargo item to given quality.
	 * returns the quantity to add
	 * @param cargo
	 * @param params
	 * @param preview
	 * 	if preview, just compute the quantity to add
	 * @return
	 */
	public static float setCargoItem(CargoAPI cargo, CargoItemQuantityOperationParams params, boolean preview){
		float current = cargo.getQuantity(params.itemType, params.data);
		CargoItemQuantityOperationParams toAdd = params.clone();
		toAdd.quantity = params.quantity - current;
		return addCargoItem(cargo, toAdd, preview);
	}
	
	/**
	 * set cargo item to given quality.
	 * returns the quantity to add
	 * @param cargo
	 * @param type
	 * @param data
	 * @param quantity
	 * @param reserve
	 * @param limit
	 * @return
	 */
	public static float setCargoItem(CargoAPI cargo, CargoItemType itemType, Object data, float quantity, float reserve, float limit, boolean preview){
		float current = cargo.getQuantity(itemType, data);
		float toAdd = quantity - current;
		return addCargoItem(cargo, itemType, data, toAdd, reserve, limit, preview);
	}
	
	/**
	 * set cargo item to given quality.
	 * returns the quantity to add.
	 * @param cargo
	 * @param itemType
	 * @param data
	 * @param quantity
	 * @param preview
	 * 	if preview, just compute the quantity to add
	 * @return
	 */
	public static float setCargoItem(CargoAPI cargo, CargoItemType itemType, Object data, float quantity){
		return setCargoItem(cargo, itemType, data, quantity, 0, Float.MAX_VALUE, false);
	}
	
	//==============================================================================
	// create convenient method for frequently used resources
	//==============================================================================
	
	public static float addCredits(CargoAPI cargo, float quantity, float reserve, float limit){
		return addCredits(cargo, quantity, reserve, limit, false);
	}
	public static float setCredits(CargoAPI cargo, float quantity){
		MutableValue credits = cargo.getCredits();
		float real = quantity - credits.get();
		credits.set(quantity);
		return real;
	}
	public static float addSupplies(CargoAPI cargo, float quantity, float reserve, float limit){
		return addCargoItem(cargo, SUPPLIES.getType(), SUPPLIES.getId(), quantity, reserve, limit, false);
	}
	public static float setSupplies(CargoAPI cargo, float quantity){
		return setCargoItem(cargo, SUPPLIES.getType(), SUPPLIES.getId(), quantity);
	}
	public static float addFuel(CargoAPI cargo, float quantity, float reserve, float limit){
		return addCargoItem(cargo, FUEL.getType(), FUEL.getId(), quantity, reserve, limit, false);
	}
	public static float setFuel(CargoAPI cargo, float quantity){
		return setCargoItem(cargo, FUEL.getType(), FUEL.getId(), quantity);
	}	
	public static float addCrew(CargoAPI cargo, CrewXPLevel level, float quantity, float reserve, float limit){
		return addCargoItem(cargo, CargoItemType.RESOURCES, level.getId(), quantity, reserve, limit, false);
	}
	public static float addCrew(CargoAPI cargo, CrewXPLevel level, float quantity){
		return addCargoItem(cargo, CargoItemType.RESOURCES, level.getId(), quantity, 0, Float.MAX_VALUE);
	}
	public static float setCrew(CargoAPI cargo, CrewXPLevel level, float quantity){
		return setCargoItem(cargo, CargoItemType.RESOURCES, level.getId(), quantity);
	}
	public static float addMarines(CargoAPI cargo, float quantity, float reserve, float limit){
		return addCargoItem(cargo, MARINES.getType(), MARINES.getId(), quantity, reserve, limit, false);
	}
	public static float setMarines(CargoAPI cargo, float quantity){
		return setCargoItem(cargo, MARINES.getType(), MARINES.getId(), quantity);
	}
	
	/**
	 * get cargo quantity
	 * cannot stat ships currently.
	 * @param cargo
	 * @param supplies
	 * @param fuel
	 * @param crew
	 * @param marines
	 */
	public static CargoQuantityParams getCargo(CargoAPI cargo){
		CargoQuantityParams params = new CargoQuantityParams();
		params.credits = cargo.getCredits().get();
		params.supplies = cargo.getSupplies();
		params.fuel = cargo.getFuel();
		params.marines = cargo.getMarines();
		params.greenCrew = cargo.getCrew(CrewXPLevel.GREEN);
		params.regularCrew = cargo.getCrew(CrewXPLevel.REGULAR);
		params.veteranCrew= cargo.getCrew(CrewXPLevel.VETERAN);
		params.eliteCrew = cargo.getCrew(CrewXPLevel.ELITE);
		List<CargoItemQuantity<String>> weapons = cargo.getWeapons();
		for (CargoItemQuantity<String> weapon : weapons) {
			CargoItemQuantityParams iparams = new CargoItemQuantityParams();
			iparams.itemType = CargoItemType.WEAPONS;
			iparams.data = weapon.getItem();
			iparams.quantity = weapon.getCount();
			params.items.add(iparams);
		}
		return params;
	}
	
	/**
	 * <pre>
	 * add some cargo, cannot add ship currently 
	 * parameters:
	 * toAdd: quantity to add, may be negative
	 * reserve: quantity to reserve (inclusive)
	 * limit: limit quantity (inclusive)
	 *  
	 * </pre>
	 * @param params
	 * @params reserve
	 *  the reserve quantity, If null, {@link CargoQuantityParams#MIN} is used.
	 * @params limit
	 * 	the limit quantity, If null, {@link CargoQuantityParams#MAX} is used.
	 * @param interceptor
	 */
	@SuppressWarnings("unchecked")
	public static CargoQuantityParams addCargo(
			CargoAPI cargo,
			CargoQuantityParams quantity,
			CargoQuantityParams reserve,
			CargoQuantityParams limit,
			boolean preview
	){
		if (limit == null){
			limit = CargoQuantityParams.MAX;
		}
		if (reserve == null)
			reserve = CargoQuantityParams.MIN;
		
		CargoQuantityParams real = new CargoQuantityParams();
		
		//
		// handle predefined items
		//
		addPredefinedCargo(cargo, quantity, reserve, limit, real, preview);
		
		//
		// handle custom items
		//
		
		//
		// merge quantity, reserve and limit
		//
		MultiKeyMap itemmap = new MultiKeyMap();
		for (CargoItemQuantityParams params : quantity.items) {
			//
			// ignore the predefined items
			//
			if (!params.isPredefined()){
				CargoItemQuantityOperationParams old = (CargoItemQuantityOperationParams) itemmap.get(params.itemType, params.data);
				if (old == null){
					// create
					CargoItemQuantityOperationParams op = CargoItemQuantityOperationParams.create(params, 0, Float.MAX_VALUE);
					itemmap.put(params.itemType, params.data, op);
				}else{
					// merge
					old.quantity = old.quantity + params.quantity;
				}
			}
		}
		
		for (CargoItemQuantityParams params : reserve.items) {
			CargoItemQuantityOperationParams op = (CargoItemQuantityOperationParams) itemmap.get(params.itemType, params.data);
			if (op != null){
				// use the maximum
				if (params.quantity > op.reserve){
					op.reserve = params.quantity;
				}
			}
		}
		
		for (CargoItemQuantityParams params : limit.items) {
			CargoItemQuantityOperationParams op = (CargoItemQuantityOperationParams) itemmap.get(params.itemType, params.data);
			if (op != null){
				// use the minimum
				if (params.quantity < op.limit)
					op.limit = params.quantity;
			}
		}
		
		//
		// reuse the operation params as quantity params
		//
		real.items.addAll(itemmap.values());
		
		//
		// perform the add operation 
		//
		for (CargoItemQuantityParams op : real.items) {
			op.quantity = addCargoItem(cargo, (CargoItemQuantityOperationParams)op, preview);
		}
		
		return real;
	}
	
	/**
	 * internal method, add predefined cargo
	 * @param cargo
	 * @param quantity
	 * @param reserve
	 * @param limit
	 * @param preview
	 * @return
	 */
	private static void addPredefinedCargo(
			CargoAPI cargo,
			CargoQuantityParams quantity,
			CargoQuantityParams reserve,
			CargoQuantityParams limit,
			CargoQuantityParams real,
			boolean preview
	){
		real.credits = addCredits(cargo, quantity.credits, reserve.credits, limit.credits, preview);
		real.supplies = addCargoItem(cargo, CargoItemQuantityOperationParams.create(PreDefinedCargoItemType.SUPPLIES, quantity.supplies,reserve.supplies,limit.supplies ), preview);
		real.fuel = addCargoItem(cargo, CargoItemQuantityOperationParams.create(PreDefinedCargoItemType.FUEL, quantity.fuel,reserve.fuel,limit.fuel),preview);
		real.marines = (int) addCargoItem(cargo, CargoItemQuantityOperationParams.create(PreDefinedCargoItemType.MARINES, quantity.marines,reserve.marines,limit.marines), preview);
		real.greenCrew = (int) addCargoItem(cargo, CargoItemQuantityOperationParams.create(PreDefinedCargoItemType.GREEN_CREW, quantity.greenCrew,reserve.greenCrew,limit.greenCrew), preview);
		real.regularCrew = (int) addCargoItem(cargo, CargoItemQuantityOperationParams.create(PreDefinedCargoItemType.REGULAR_CREW, quantity.regularCrew,reserve.regularCrew,limit.regularCrew), preview);
		real.veteranCrew = (int) addCargoItem(cargo, CargoItemQuantityOperationParams.create(PreDefinedCargoItemType.VETERAN_CREW, quantity.veteranCrew,reserve.veteranCrew,limit.veteranCrew), preview);
		real.eliteCrew = (int) addCargoItem(cargo, CargoItemQuantityOperationParams.create(PreDefinedCargoItemType.ELITE_CREW, quantity.eliteCrew,reserve.eliteCrew,limit.eliteCrew), preview);
	}

	/**
	 * set cargo to given quantity
	 * @param cargo
	 * @param quantity
	 * @param reserve
	 * @param limit
	 * @param preview
	 * @return
	 */
	public static CargoQuantityParams setCargo(
			CargoAPI cargo,
			CargoQuantityParams quantity,
			CargoQuantityParams reserve,
			CargoQuantityParams limit,
			boolean preview
	){
		CargoQuantityParams params = getCargo(cargo);
		// compute difference
		params.credits = quantity.credits - params.credits;
		params.supplies = quantity.supplies - params.supplies;
		params.fuel = quantity.fuel - params.fuel;
		params.marines = quantity.marines - params.marines;
		params.greenCrew = quantity.greenCrew - params.greenCrew;
		params.regularCrew = quantity.regularCrew - params.regularCrew;
		params.veteranCrew = quantity.veteranCrew - params.veteranCrew;
		params.eliteCrew = quantity.eliteCrew - params.eliteCrew;
		
		MultiKeyMap map = new MultiKeyMap();
		for (CargoItemQuantityParams iparam : quantity.items) {
			map.put(iparam.itemType, iparam.data, iparam.quantity);
		}
		for (CargoItemQuantityParams iparam : params.items) {
			float value = (Float) map.get(iparam.itemType, iparam.data);
			iparam.quantity = value - iparam.quantity;
		}
		return addCargo(cargo, params, reserve, limit, preview);
	}
	
	/**
	 * set cargo to given quantity
	 * @param cargo
	 * @param quantity
	 * @param reserve
	 * @param limit
	 * @return
	 */
	public static CargoQuantityParams setCargo(
			CargoAPI cargo,
			CargoQuantityParams quantity,
			CargoQuantityParams reserve,
			CargoQuantityParams limit
	){
		return setCargo(cargo, quantity, reserve, limit, false);
	}
	
	/**
	 * convert Cargo Item with reserve and limit checked.
	 * returns the real convert quantity 
	 */
	public static float convertCargoItem(
			CargoAPI fromCargo,
			CargoItemType fromItemType, Object fromItemData,
			float fromReserve, float fromLimit,
			CargoAPI toCargo,
			CargoItemType toItemType, Object toItemData,
			float toReserve, float toLimit,
			float quantity
			){
		float preview = -addCargoItem(fromCargo, fromItemType, fromItemData, -quantity, fromReserve, fromLimit, false);
		float real = addCargoItem(toCargo, toItemType, toItemData, preview, toReserve, toLimit, false);
		if (real != preview){
			addCargoItem(fromCargo, fromItemType, fromItemData, preview-real, fromReserve, fromLimit, false);
		}
		return real;
	}
	
	/**
	 * convert cargo item with default reserve and limit.
	 * @return
	 */
	public static float convertCargoItem(
			CargoAPI fromCargo, 
			CargoItemType fromItemType, Object fromItemData,
			CargoAPI toCargo,
			CargoItemType toItemType, Object toItemData,
			float quantity
	){
		return convertCargoItem(fromCargo, fromItemType, fromItemData, 
				0, Float.MAX_VALUE, 
				toCargo, toItemType, toItemData, 
				0, Float.MAX_VALUE
				, quantity);
	}
	
	/**
	 * convenient method to level crew
	 * @param cargo
	 * @param curLevel
	 * @param quantity
	 * @return
	 */
	public static float crewLevelUp(CargoAPI cargo, CrewXPLevel curLevel, int quantity){
		CrewXPLevel nextLevel;
		switch (curLevel) {
		case GREEN:
			nextLevel = CrewXPLevel.REGULAR;
			break;
		case REGULAR:
			nextLevel = CrewXPLevel.VETERAN;
			break;
		case VETERAN:
			nextLevel = CrewXPLevel.ELITE;
			break;
		default:
			nextLevel = null;
		}
		if (nextLevel != null)
			return convertCargoItem(cargo, CargoItemType.RESOURCES, curLevel.getId(), 
				cargo, CargoItemType.RESOURCES, nextLevel.getId(), quantity);
		else
			return 0;
	}
	
	/**
	 * convenient method to level crew
	 * @param cargo
	 * @param curLevel
	 * @param rate
	 * @return
	 */
	public static float crewLevelUpRate(CargoAPI cargo, CrewXPLevel curLevel, float rate){
		return crewLevelUp(cargo, curLevel, (int) (cargo.getCrew(curLevel)*rate));
	}
	
	
	/**
	 * set cargo to value without checking reserve and limit.
	 * It would fails to add mothballedShips when value acquired by CampaignFleetAPI 
	 */
	public static void setCargo(CargoAPI cargo, CargoAPI value){
		cargo.clear();
		addCargo0(cargo, value);
	}
	
	/**
	 * add all resources, weapons, ships from quantity to cargo,
	 * without checking reserve and limit.
	 * It would fails to add mothballedShips when value acquired by CampaignFleetAPI
	 * @param cargo
	 * @param cargo
	 */
	private static void addCargo0(CargoAPI cargo, CargoAPI value){
		cargo.getCredits().set(value.getCredits().get());
		
		for (PreDefinedCargoItemType type : PreDefinedCargoItemType.values()) {
			float quantity = value.getQuantity(type.getType(), type.getId());
			cargo.addItems(type.getType(), type.getId(), quantity);
		}
		
		for (CargoItemQuantity<String> item : value.getWeapons()) {
			float quantity = value.getQuantity(CargoItemType.WEAPONS, item.getItem());
			cargo.addItems(CargoItemType.WEAPONS, item.getItem(), quantity);
		}
		
		FleetDataAPI data = value.getMothballedShips();
		//
		// getMothballedShips will be null if cargo belongs to a fleet
		//
		if (data != null){
			FleetDataAPI ships = cargo.getMothballedShips();
			if (ships != null){
				List<FleetMemberAPI> members = data.getMembersListCopy();
				if (ships != null){
					for (FleetMemberAPI member : members) {
						ships.addFleetMember(member);
					}
				}
			}
		}
	}
	
	/**
	 * take all cargos, including mothballedShips
	 * @param fleet
	 * @param cargo
	 */
	public static void takeAll(CampaignFleetAPI fleet, CargoAPI cargo){
		addCargo0(fleet.getCargo(),cargo);
		FleetDataAPI data = fleet.getFleetData();
		FleetDataAPI ships = cargo.getMothballedShips();
		if (ships != null){
			
			//
			// there is a bug when player take ships from neutral station
			// fleetData will null.
			//
			
			List<FleetMemberAPI> list = ships.getMembersListCopy();
			
			//
			// recreate the memebers
			//
//			FactoryAPI factory = Global.getFactory();
			for (FleetMemberAPI member : list) {
//				ShipVariantAPI variant = member.getVariant();
//				FleetMemberAPI newMember = factory.createFleetMember(member.getType(), variant.getHullVariantId());
				//
				// set weapon and logistics
				//
//				newMember.setVariant(variant, true, true);
//				data.addFleetMember(newMember);
				
				data.addFleetMember(member);
			}
		}
		cargo.clear();
	}
	
	/**
	 * fleet1 take all items and fleets from fleet2
	 * @param fleet
	 * @param another
	 * @param includingFlagship
	 */
	public static void takeAll(CampaignFleetAPI fleet1, CampaignFleetAPI fleet2){
		CargoAPI cargo2 = fleet2.getCargo();
		addCargo0(fleet1.getCargo(), cargo2);
		fleet2.getCargo().clear();
		FleetDataAPI data1 = fleet1.getFleetData();
		FleetDataAPI data2 = fleet2.getFleetData();
		List<FleetMemberAPI> members = data2.getMembersListCopy();
		for (FleetMemberAPI member : members) {
			data2.removeFleetMember(member);
			data1.addFleetMember(member);
		}
		data2.clear();
	}
	
	/**
	 * set cargo to value without checking reserve and limit.
	 * It would fails to add mothballedShips when cargo acquired by CampaignFleetAPI
	 * @param cargo
	 * @param value
	 */
	public static void setCargo(CargoAPI cargo, CampaignFleetAPI value, boolean includeFlagShip){
		cargo.clear();
		addCargo0(cargo, value.getCargo());
		// add ships
		List<FleetMemberAPI> members = value.getFleetData().getMembersListCopy();
		if (!includeFlagShip){
			members.remove(value.getFlagship());
		}
		for (FleetMemberAPI member : members) {
			cargo.getMothballedShips().addFleetMember(member);
		}
	}
	
	/**
	 * clear fleet's all ships and cargo
	 * @param fleet
	 */
	public static void clear(CampaignFleetAPI fleet, boolean includeFlagShip){
		fleet.getCargo().clear();
		FleetDataAPI data = fleet.getFleetData();
		List<FleetMemberAPI> members = data.getMembersListCopy();
		if (!includeFlagShip){
			members.remove(fleet.getFlagship());
		}
		
		for (FleetMemberAPI member : members) {
			data.removeFleetMember(member);
		}
	}
	
	/**
	 * recreate all fleet member. fix a bug when take ship from deleted dummy station
	 * @param fleet
	 */
	public static void renewFleetMembers(CampaignFleetAPI fleet){
		FleetDataAPI data = fleet.getFleetData();
		List<FleetMemberAPI> members = data.getMembersListCopy();
		FactoryAPI factory = Global.getFactory();
		for (FleetMemberAPI member : members) {
			FleetMemberAPI newMember = factory.createFleetMember(member.getType(), member.getVariant().getHullVariantId());
			boolean flagship = member.isFlagship();
			PersonAPI captain = member.getCaptain();
			newMember.setVariant(member.getVariant(), true, true);
			
			//
			// copy status
			//
			FleetMemberStatusAPI status = member.getStatus();
			FleetMemberStatusAPI newStatus = newMember.getStatus();
			newStatus.applyHullFractionDamage(1-status.getHullFraction());
			
			data.addFleetMember(newMember);
			data.removeFleetMember(member);
			
			newMember.setFlagship(flagship);
			newMember.setCaptain(captain);
		}
	}
	
}

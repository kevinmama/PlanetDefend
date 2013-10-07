package starsector.mod.nf.support;

import java.util.HashSet;
import java.util.Set;

import com.fs.starfarer.api.campaign.CargoAPI.CargoItemType;

/**
 * pre defined cargo by 'core' mod. May has special use.
 * @author fengyuan
 *
 */
public enum PreDefinedCargoItemType {
	
	SUPPLIES(CargoItemType.RESOURCES, "supplies"),
	FUEL(CargoItemType.RESOURCES, "fuel"),
	GREEN_CREW(CargoItemType.RESOURCES, "green_crew"),
	REGULAR_CREW(CargoItemType.RESOURCES, "regular_crew"),
	VETERAN_CREW(CargoItemType.RESOURCES, "veteran_crew"),
	ELITE_CREW(CargoItemType.RESOURCES, "elite_crew"),
	MARINES(CargoItemType.RESOURCES, "marines"),
	
	;
	private final CargoItemType type;
	private final String id;
	public CargoItemType getType() {
		return type;
	}
	public String getId() {
		return id;
	}
	
	private PreDefinedCargoItemType(CargoItemType type, String id){
		this.type = type;
		this.id = id;
	}
	
	/**
	 * used to determine a params's type refer to meta item
	 */
	static Set<String> predefinedCargoItemIdSet = new HashSet<String>();
}

package starsector.mod.nf.support;

import com.fs.starfarer.api.campaign.CargoAPI.CargoItemType;

/**
 * parameters for operate given cargo item
 * @author fengyuan
 *
 */
public class CargoItemQuantityParams {
	
	public CargoItemType itemType;
	public Object data;
	public float quantity;
	
	/**
	 * create a new parameter, for clone
	 * @return
	 */
	protected CargoItemQuantityParams create(){
		return new CargoItemQuantityParams();
	}
	
	@Override
	protected CargoItemQuantityParams clone(){
		CargoItemQuantityParams clone = create();
		clone.itemType = itemType;
		clone.data = data;
		clone.quantity = quantity;
		return clone;
	}
	
//	/**
//	 * create parameters for predefined item
//	 * @param type
//	 * @return
//	 */
//	private static final CargoItemQuantityParams create(PreDefinedCargoItemType type){
//		CargoItemQuantityParams params = new CargoItemQuantityParams();
//		params.itemType = type.getType();
//		params.data = type.getId();
//		return params;
//	}
	
//	public static final CargoItemQuantityParams create(PreDefinedCargoItemType type, float quantity){
//		CargoItemQuantityParams params = create(type);
//		params.quantity = quantity;
//		return params;
//	}
	
	public static final boolean isPredefined(CargoItemQuantityParams params){
		return params.itemType == CargoItemType.RESOURCES && PreDefinedCargoItemType.predefinedCargoItemIdSet.contains(params.data);
	}
	
	public final boolean isPredefined(){
		return isPredefined(this);
	}
}

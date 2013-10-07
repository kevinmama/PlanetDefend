package starsector.mod.nf.support;

public class CargoItemQuantityOperationParams extends CargoItemQuantityParams{
	public float reserve = 0;
	public float limit = Float.MAX_VALUE;
	
	@Override
	protected CargoItemQuantityParams create() {
		return new CargoItemQuantityOperationParams();
	}
	
	@Override
	protected CargoItemQuantityOperationParams clone() {
		CargoItemQuantityOperationParams params = (CargoItemQuantityOperationParams) super.clone();
		params.reserve = reserve;
		params.limit = limit;
		return params;
	}
	
	public static CargoItemQuantityOperationParams create(PreDefinedCargoItemType type, float quantity, float reserve, float limit){
		CargoItemQuantityOperationParams params = new CargoItemQuantityOperationParams();
		params.itemType = type.getType();
		params.data = type.getId();
		params.quantity = quantity;
		params.reserve = reserve;
		params.limit = limit;
		return params;
	}
	
	public static CargoItemQuantityOperationParams create(CargoItemQuantityParams params, float reserve, float limit){
		CargoItemQuantityOperationParams p = new CargoItemQuantityOperationParams();
		p.itemType = params.itemType;
		p.data = params.data;
		p.quantity = params.quantity;
		p.reserve = reserve;
		p.limit = limit;
		return p;
	}
}

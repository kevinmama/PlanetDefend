package starsector.mod.nf.support;

import java.util.ArrayList;
import java.util.List;


/**
 * convenient cargo operation parameters. 
 * @author fengyuan
 *
 */
public class CargoQuantityParams {
	
	/**
	 * the default maximum params
	 */
	public static final CargoQuantityParams MAX = new CargoQuantityParams();
	static {
		MAX.credits = Float.MAX_VALUE;
		MAX.supplies = Float.MAX_VALUE;
		MAX.fuel = Float.MAX_VALUE;
		MAX.marines = Integer.MAX_VALUE;
		MAX.greenCrew = Integer.MAX_VALUE;
		MAX.regularCrew= Integer.MAX_VALUE;
		MAX.veteranCrew = Integer.MAX_VALUE;
		MAX.eliteCrew = Integer.MAX_VALUE;
	}
	
	/**
	 * the default minimum params
	 */
	public static final CargoQuantityParams MIN = new CargoQuantityParams();
	
	
	public float credits = 0;
	public float supplies = 0;
	public float fuel = 0;
	public int marines = 0;
	public int greenCrew = 0;
	public int regularCrew = 0;
	public int veteranCrew = 0;
	public int eliteCrew = 0;
	
	/**
	 * meta item (eg: supplies, fuel, etc...) will be ignored
	 */
	public List<CargoItemQuantityParams> items = new ArrayList<CargoItemQuantityParams>();

	@Override
	public String toString() {
		return "CargoQuantityParams [credits=" + credits + ", supplies="
				+ supplies + ", fuel=" + fuel + ", marines=" + marines
				+ ", greenCrew=" + greenCrew + ", regularCrew=" + regularCrew
				+ ", veteranCrew=" + veteranCrew + ", eliteCrew=" + eliteCrew
				+ ", items=" + items + "]";
	}
}

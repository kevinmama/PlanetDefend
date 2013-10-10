package starsector.mod.pld.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * represent a collection of fleets
 * @author fengyuan
 *
 */
public class PLDArmy extends PLDObject{
	
	/**
	 * the flag fleet, commonly the MotherShip
	 */
	private PLDFleet flagFleet;
	private Set<PLDFleet> fleets = new HashSet<PLDFleet>();
	private String name;
	
	PLDArmy(String name, PLDFleet fleet) {
		this.name = name;
		fleets.add(fleet);
		flagFleet = fleet;
		validateArmyFields();
	}
	
	PLDArmy(String name, 
			PLDFleet flagFleet,
			List<PLDFleet> fleets) {
		this.name = name;
		this.fleets.addAll(fleets);
		this.flagFleet = flagFleet;
		validateArmyFields();
	}

	@Override
	protected String getName() {
		return name;
	}
	
	public Collection<PLDFleet> getFleetsCopy(){
		return new ArrayList<PLDFleet>(fleets);
	}
	
	public PLDFleet getFlagFleet(){
		return flagFleet;
	}

	public void setFlagFleet(PLDFleet flagFleet) {
		this.flagFleet = flagFleet;
		validateArmyFields();
	}
	
	private void validateArmyFields(){
		if (flagFleet == null || !fleets.contains(flagFleet))
			throw new IllegalStateException();
	}
	
	public void addFleet(PLDFleet fleet){
		fleets.add(fleet);
	}
	
	/**
	 * remove a fleet from army,
	 * dispose If remove the only flag fleet 
	 * @param fleet
	 */
	public void removeFleet(PLDFleet fleet){
		if (fleet == flagFleet){
			if (fleets.size() > 1){
				fleets.remove(fleet);
				flagFleet = fleets.iterator().next();
			}else{
				dispose();
			}
		}else{
			fleets.remove(fleet);
		}
	}
	
	/**
	 * is fleet in army
	 * @param fleet
	 * @return
	 */
	public boolean contains(PLDFleet fleet){
		return fleets.contains(fleet);
	}
	
}

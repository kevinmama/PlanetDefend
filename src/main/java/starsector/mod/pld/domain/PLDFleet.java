package starsector.mod.pld.domain;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;


/**
 * Base Fleet Object of PLD
 * @author fengyuan
 *
 */
public class PLDFleet extends PLDObject{
	
	/**
	 * where the fleet belong to, usally where it spawned
	 */
	protected PLDStation base;
	protected CampaignFleetAPI fleet;
	
	PLDFleet(CampaignFleetAPI fleet) {
		this.fleet = fleet;
	}
	
	public String getName(){
		return fleet.getName();
	}

	/**
	 * get the base station of this fleet
	 * @return
	 */
	public PLDStation getBase() {
		return base;
	}

	/**
	 * change the base station of this fleet.
	 * It would set the PreferredResupplyLocation to the base
	 * @param base
	 */
	public void setBase(PLDStation base) {
		this.base = base;
		fleet.setPreferredResupplyLocation(base.getStation());
	}

	public CampaignFleetAPI getFleet() {
		return fleet;
	}
	
}

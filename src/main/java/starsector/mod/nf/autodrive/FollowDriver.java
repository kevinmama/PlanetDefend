package starsector.mod.nf.autodrive;

import org.lwjgl.util.vector.Vector2f;

import starsector.mod.nf.support.MathSupport;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;

/**
 * drive to follow target
 * @author fengyuan
 *
 */
public class FollowDriver extends AutoDriver{
	
	private SectorEntityToken target;
	float distance;
	
	/**
	 * constructor
	 * @param fleet
	 * @param target
	 * 	the follow target
	 * @param distance
	 */
	public FollowDriver(CampaignFleetAPI fleet, SectorEntityToken target, float distance) {
		super(fleet);
		setName(FollowDriver.class.getSimpleName());
		this.target = target;
	}

	@Override
	protected void drive() {
		Vector2f loc = target.getLocation();
		if (!MathSupport.isInRange(fleet.getLocation(), target.getLocation(), distance)){
			fleet.setMoveDestination(loc.x, loc.y);
		}
	}

}

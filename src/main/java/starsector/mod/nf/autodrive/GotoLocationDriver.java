package starsector.mod.nf.autodrive;

import org.lwjgl.util.vector.Vector2f;

import starsector.mod.nf.support.MathSupport;

import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;

/**
 * auto drive to given location and trigger script
 * @author fengyuan
 *
 */
public class GotoLocationDriver extends AutoDriver{
	
	/**
	 * is the destination static?
	 */
	private boolean staticLocation;
	private Vector2f sDest;				// static dest
	private SectorEntityToken dDest;	// dynamic dest
	private float range = 0;		// just go in the range of target
	private Script onCompletion;
	
	public GotoLocationDriver(CampaignFleetAPI fleet, Vector2f location, int range, Script onCompletion) {
		super(fleet);
		sDest = location;
		staticLocation = true;
		this.range = range;
		this.onCompletion = onCompletion;
	}
	
	public GotoLocationDriver(CampaignFleetAPI fleet, SectorEntityToken location, int range, Script onCompletion) {
		super(fleet);
		dDest = location;
		staticLocation = false;
		this.range = range;
		this.onCompletion = onCompletion;
	}
	
	@Override
	protected void drive() {
		Vector2f dest;
		
		if (!staticLocation){
			dest = this.dDest.getLocation();
		}else{
			dest = this.sDest;
		}
		
		Vector2f loc = fleet.getLocation();
		if (MathSupport.isInRange(loc, dest, range)){
			dispose();
			if (onCompletion != null){
				onCompletion.run();
			}
		}else{
			fleet.setMoveDestination(dest.getX(), dest.getY());
		}
	}
}

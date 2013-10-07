package starsector.mod.nf.support;

import java.util.Iterator;
import java.util.List;

import org.lazywizard.lazylib.MathUtils;

import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;

/**
 * functions support handle campaign objects
 * @author fengyuan
 *
 */
public final class CampaignSupport {
	
	//================================================
	// fleet support
	//================================================
	
	/**
	 * Get the nearest asteroid, return null if havn't.
	 * check every 5 asteroid.
	 * @param entity
	 * @return
	 */
	public static SectorEntityToken getNearestAsteroid(SectorEntityToken entity){
		return getNearestAsteroid(entity, 5);
	}
	
	/**
	 * get the nearest asteroid, return null if havn't.
	 * @param fleet
	 * @param step
	 * 	check every $step asteroid to speed up the checking.
	 *  Idea comes from Extrelin.
	 * @return
	 */
	public static SectorEntityToken getNearestAsteroid(SectorEntityToken entity, int step){
		
		if (step <= 0){
			throw new IllegalArgumentException();
		}
		
		// find the nearest asteroid
		SectorEntityToken target = null;
		LocationAPI location = entity.getContainingLocation();
		List<SectorEntityToken> asteroids = location.getAsteroids();
		float dist = Float.MAX_VALUE;
		
		//
		// check every $step asteroid, since they are very closed 
		//
		Iterator<SectorEntityToken> iter = asteroids.iterator();
		int idx = 0;
		while (iter.hasNext()){
			SectorEntityToken asteroid = iter.next();
			if (idx++ % step == 0){
				float distance = MathUtils.getDistanceSquared(entity, asteroid);
				if (distance < dist){
					dist = distance;
					target = asteroid;
				}
			}
		}
		return target;
	}
	
	/**
	 * check if targets in range
	 * @param target1
	 * @param target2
	 * @param range
	 * @return
	 */
	public static boolean isInRange(SectorEntityToken target1, SectorEntityToken target2, float range){
		return MathUtils.getDistanceSquared(target1, target2) <= range*range;
	}
	
}

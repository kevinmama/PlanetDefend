package starsector.mod.nf.support;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;

public class OrbitalEntityGeneratationParams implements Parameters{
	
	/**
	 * the concern system
	 */
	public StarSystemAPI system;
	
	/**
	 * the center entity
	 */
	public SectorEntityToken focus;

	/**
	 * base radius, used for computing real orbit radius
	 */
	public float baseOrbitRadius = 1500;
	
	/**
	 * base orbit days, used for computing real orbit radius
	 */
	public float baseOrbitDays = 300;
	
	/**
	 * relative error may add to some fields
	 */
	public float relativeError = 0.1f;

	/**
	 * the generated orbit radius
	 */
	public float realOrbitRadius;
	
}

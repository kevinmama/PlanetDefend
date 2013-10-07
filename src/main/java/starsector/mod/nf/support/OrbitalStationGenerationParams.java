package starsector.mod.nf.support;

/**
 * orbital station generation parmeters
 * @author fengyuan
 *
 */
public class OrbitalStationGenerationParams extends OrbitalEntityGeneratationParams{
	
	/**
	 * the station name
	 */
	public String name;
	
	/**
	 * the station's faction Id 
	 */
	public String factionId;
	
	/**
	 * can station build on gas gaint planet
	 */
	public boolean onGasGaint = false;

	/**
	 * can station build on moon
	 */
	public boolean onMoon = false;
	
	/**
	 * can station build on star
	 */
	public boolean onStar = false;

	
	/**
	 * the generated angle
	 */
	public float realAngle;
	
	/**
	 * the generated orbit days
	 */
	public float realOrbitDays;

	@Override
	public String toString() {
		return "OrbitalStationGenerationParams [name=" + name + ", factionId="
				+ factionId + ", onGasGaint=" + onGasGaint + ", onMoon="
				+ onMoon + ", onStar=" + onStar + ", realAngle=" + realAngle
				+ ", realOrbitDays=" + realOrbitDays + ", system=" + system
				+ ", focus=" + focus + ", baseOrbitRadius=" + baseOrbitRadius
				+ ", baseOrbitDays=" + baseOrbitDays + ", relativeError="
				+ relativeError + ", realOrbitRadius=" + realOrbitRadius + "]";
	}
	
}

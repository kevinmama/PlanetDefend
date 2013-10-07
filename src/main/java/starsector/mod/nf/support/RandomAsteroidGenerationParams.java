package starsector.mod.nf.support;

public class RandomAsteroidGenerationParams extends RandomOrbitEntityGenerationParams{
	
	/**
	 * the number of asteroid per belt.
	 */
	public int baseAsteroidNumPerBelt = 4;
	
	/**
	 * the width of asteroid belt
	 */
	public int baseWidth = 300;
	
	/**
	 * the generated number of asteroid
	 */
	public int realNumAsteroids = 100;
	
	/**
	 * the generated width
	 */
	public float realWidth;
	
	/**
	 * the generated minimum orbit days
	 */
	public float realMinOrbitDays;
	
	/**
	 * the generated maximum orbit days
	 */
	public float realMaxOrbitDays;

	@Override
	public String toString() {
		return "RandomAsteroidGenerationParams [baseAsteroidNumPerBelt="
				+ baseAsteroidNumPerBelt + ", baseWidth=" + baseWidth
				+ ", realNumAsteroids=" + realNumAsteroids + ", realWidth="
				+ realWidth + ", realMinOrbitDays=" + realMinOrbitDays
				+ ", realMaxOrbitDays=" + realMaxOrbitDays + ", num=" + num
				+ ", possibleMinOrbitRadius=" + possibleMinOrbitRadius
				+ ", possibleMaxOrbitRadius=" + possibleMaxOrbitRadius
				+ ", possibleOrbitRadiusDistribution="
				+ possibleOrbitRadiusDistribution + ", system=" + system
				+ ", focus=" + focus + ", baseOrbitRadius=" + baseOrbitRadius
				+ ", baseOrbitDays=" + baseOrbitDays + ", relativeError="
				+ relativeError + ", realOrbitRadius=" + realOrbitRadius + "]";
	}
	
}

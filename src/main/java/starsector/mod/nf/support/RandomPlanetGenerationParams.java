package starsector.mod.nf.support;

import java.util.Collection;


public class RandomPlanetGenerationParams extends RandomOrbitEntityGenerationParams{
	
	/**
	 * available planet names
	 */
	public Collection<String> availableNames;
	
	/**
	 * available planet types
	 */
	public Collection<String> availableTypes;
	
	/**
	 * only used in planet generation
	 */
	public float baseRadius = 1500;

	
	/**
	 * generated planet name
	 */
	public String realName;
	
	/**
	 * generated planet type
	 */
	public String realType;
	
	/**
	 * generated planet radius
	 */
	public float realRadius;
	
	/**
	 * generated angle
	 */
	public float realAngle;
	
	/**
	 * generated orbit days
	 */
	public float realOrbitDays;

	@Override
	public String toString() {
		return "RandomPlanetGenerationParams [availableNames=" + availableNames
				+ ", availableTypes=" + availableTypes + ", baseRadius="
				+ baseRadius + ", realName=" + realName + ", realType="
				+ realType + ", realRadius=" + realRadius + ", realAngle="
				+ realAngle + ", realOrbitDays=" + realOrbitDays + ", num="
				+ num + ", possibleMinOrbitRadius=" + possibleMinOrbitRadius
				+ ", possibleMaxOrbitRadius=" + possibleMaxOrbitRadius
				+ ", possibleOrbitRadiusDistribution="
				+ possibleOrbitRadiusDistribution + ", system=" + system
				+ ", focus=" + focus + ", baseOrbitRadius=" + baseOrbitRadius
				+ ", baseOrbitDays=" + baseOrbitDays + ", relativeError="
				+ relativeError + ", realOrbitRadius=" + realOrbitRadius + "]";
	}
	
}

package starsector.mod.nf.support;


/**
 * parameters for generate sector randomly
 * @author fengyuan
 *
 */
public class RandomOrbitEntityGenerationParams extends OrbitalEntityGeneratationParams{
	
	/**
	 * num of generated entity
	 */
	public int num;
	
	/**
	 * the low limit of orbit radius
	 */
	public float possibleMinOrbitRadius = 1000;
	
	/**
	 * the high limit of orbit radius
	 */
	public float possibleMaxOrbitRadius = 10000;
	
	/**
	 * the distribution of orbit Radius
	 */
	public Distribution possibleOrbitRadiusDistribution = Distribution.LINEAR;
	
}

package starsector.mod.nf.support;

import static starsector.mod.nf.support.MathSupport.addRelativeError;
import static starsector.mod.nf.support.MathSupport.generateRandomVectorWithinInterval;
import static starsector.mod.nf.support.MathSupport.rdgen;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;

import com.fs.starfarer.api.campaign.OrbitalStationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;



/**
 * help to generate sector
 * @author fengyuan
 *
 */
public final class SectorGenerationSupport {
	
	private static Logger log = Logger.getLogger(SectorGenerationSupport.class, AppenderType.LOG4J);
	
	
	/**
	 * generate random asteroid belt.
	 */
	public static final void generateRandomAsteroidBelt(RandomAsteroidGenerationParams params, OperationInterceptor<RandomAsteroidGenerationParams, Void> interceptor){
		float[] orbitRadiusVector = generateRandomVectorWithinInterval(params.num, params.possibleMinOrbitRadius, params.possibleMaxOrbitRadius, params.possibleOrbitRadiusDistribution);
		for (int i = 0; i < orbitRadiusVector.length; i++) {
			params.realOrbitRadius = orbitRadiusVector[i];
			float multiplier = params.realOrbitRadius / params.baseOrbitRadius;
			params.realNumAsteroids = (int) addRelativeError(params.baseAsteroidNumPerBelt * multiplier, params.relativeError);
			float orbitDays = addRelativeError(params.baseOrbitDays * multiplier, params.relativeError);
			params.realMinOrbitDays = addRelativeError(orbitDays, params.relativeError);
			params.realMaxOrbitDays = addRelativeError(orbitDays, params.relativeError);
			params.realWidth = addRelativeError(params.baseWidth, params.relativeError);
			if (params.realMinOrbitDays > params.realMaxOrbitDays){
				float tmp = params.realMinOrbitDays;
				params.realMinOrbitDays = params.realMaxOrbitDays;
				params.realMaxOrbitDays = tmp;
			}
			if (interceptor == null || !interceptor.intercept(params)){
				log.info("generate asteroid belt " + params);
				params.system.addAsteroidBelt(params.focus, params.realNumAsteroids, params.realOrbitRadius, params.realWidth, params.realMinOrbitDays, params.realMaxOrbitDays);
				if (interceptor != null){
					//
					// since we don't have generated object, simplely call the method with argument 'null'
					//
					interceptor.intercept((Void)null);
				}
			}
		}
	}
	
	/**
	 * generate random planets. 
	 * Generator will circularly pick names from available names and randomly pick type from available types.
	 * 
	 * @param params
	 * @param interceptor
	 * @return
	 */
	public static final List<PlanetAPI> generateRandomPlanet(RandomPlanetGenerationParams params, OperationInterceptor<RandomPlanetGenerationParams, PlanetAPI> interceptor){
		List<PlanetAPI> planets = new LinkedList<PlanetAPI>();
		float[] orbitRadiusVector = generateRandomVectorWithinInterval(params.num, params.possibleMinOrbitRadius, params.possibleMaxOrbitRadius, params.possibleOrbitRadiusDistribution);
		
		Iterator<String> nameListIter = null;
		if (params.availableNames != null && !params.availableNames.isEmpty())
			nameListIter = params.availableNames.iterator();
		
		String[] availableTypeVector = null;
		if (params.availableTypes != null && !params.availableTypes.isEmpty()){
			availableTypeVector = params.availableTypes.toArray(new String[0]);
		}
		
		for (int i = 0; i < orbitRadiusVector.length; i++) {
			
			//
			// pick name from available names circularly.
			//
			
			if (nameListIter != null){
				if (!nameListIter.hasNext()){
					nameListIter = params.availableNames.iterator();
				}
				params.realName = nameListIter.next();
			}
			
			//
			// pick type randomly
			//
			if (availableTypeVector != null){
				params.realType = availableTypeVector[rdgen.nextInt(0, availableTypeVector.length-1)];
			}
			
			params.realAngle = (float) rdgen.nextUniform(1, 360);
			params.realOrbitRadius = orbitRadiusVector[i];
			float multiplier = params.realOrbitRadius / params.baseOrbitRadius;
			params.realOrbitDays = addRelativeError(params.baseOrbitDays*multiplier, params.relativeError);
			params.realRadius = addRelativeError(params.baseRadius, params.relativeError);
			
			if (interceptor == null || !interceptor.intercept(params)){
				log.info("generate planet " + params);
				PlanetAPI planet = params.system.addPlanet(params.focus, params.realName, params.realType, params.realAngle, params.realRadius, params.realOrbitRadius, params.realOrbitDays);
				planets.add(planet);
				if (interceptor != null){
					interceptor.intercept(planet);
				}
			}
		}
		return planets;
	}
	
	/**
	 * generate orbitalStation with given parameters
	 * @return
	 */
	public static OrbitalStationAPI generateOrbitalStation(OrbitalStationGenerationParams params, OperationInterceptor<OrbitalStationGenerationParams, OrbitalStationAPI> interceptor){
		
		SectorEntityToken focus = params.focus;
		if (focus instanceof PlanetAPI){
			PlanetAPI planet = (PlanetAPI) focus;
			if (planet.isStar() && !params.onStar)
				return null;
			if (planet.isGasGiant() && !params.onGasGaint)
				return null;
			if (planet.isMoon() && !params.onMoon)
				return null;
		}
		
		params.realAngle = (float) rdgen.nextUniform(1, 360);
		params.realOrbitRadius = addRelativeError(params.baseOrbitRadius, params.relativeError);
		params.realOrbitDays = addRelativeError(params.baseOrbitDays, params.relativeError);
		OrbitalStationAPI station = null;
		if (interceptor == null || !interceptor.intercept(params)){
			String name = params.name != null ? params.name : focus.getName() + " Station";
			log.info("generate station: " + params);
			station = (OrbitalStationAPI) params.system.addOrbitalStation(params.focus, params.realAngle, params.realOrbitRadius, params.realOrbitDays, name, params.factionId);
			if (interceptor != null){
				interceptor.intercept(station);
			}
		}
		return station;
	}
	
}

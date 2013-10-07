package starsector.mod.nf.support;

import java.util.Arrays;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.lwjgl.util.vector.Vector2f;

/**
 * mathematics functions 
 * @author fengyuan
 *
 */
public class MathSupport {
	
	public static final RandomDataGenerator rdgen = new RandomDataGenerator();
	
	/**
	 * get mean and sd of Normal distribution whose all sample is in given interval approximation.
	 * within sd, possible is 0.68.
	 * within 3*sd, possible is 0.99
	 * @param min
	 * @param max
	 * @return
	 */
	public static float[] getMeanAndSDOfNormalDistribution(float min, float max){
		float mean = (min + max)/2;
		float sd = (max - min)/6;
		return new float[]{mean,sd};
	}
	
	/**
	 * generate random numbers within interval of given distribution
	 * @param size
	 * 	size of vector
	 * @param min
	 * 	approximate minimum value
	 * @param max
	 * 	approximate maximum value
	 * @param distribution
	 * @return
	 */
	public static float[] generateRandomVectorWithinInterval(int size, float min, float max, Distribution distribution){
		
		if (min > max){
			throw new IllegalArgumentException(	String.format("min(%f) is greater than max(%f)", min, max));
		}
		
		float[] vector = new float[size];
		
		//
		// same value, no needs to compute
		//
		if (min == max){
			Arrays.fill(vector, min);
			return vector;
		}

		
		switch (distribution) {
		case LINEAR:{
			if (size > 1){
				float multiplier = (max - min)/(size-1);
				for (int i = 0; i < size; i++) {
					vector[i] = min + i*multiplier;
				}
			}else{
				vector[0] = (min + max) / 2;
			}
			break;
		}
		case GAUSSIAN:{
			float[] meanAndSD = MathSupport.getMeanAndSDOfNormalDistribution(min, max);
			for (int i = 0; i < size; i++) {
				vector[i] = (float) rdgen.nextGaussian(meanAndSD[0], meanAndSD[1]);
			}
			break;
		}
		case UNIFORM:{
			for (int i = 0; i < size; i++) {
				vector[i] = (float) rdgen.nextUniform(min, max);
			}
			break;
		}
		default:
			throw new UnsupportedOperationException("distribution " + distribution + " is not supported.");
		}
		return vector;
	}
	
	/**
	 * add an relative error to value
	 * @param value
	 * @param error
	 * 	relative error, between 0(inclusive) and 1(exclusive).
	 * @return
	 */
	public static float addRelativeError(float value, float error){
		if (Math.abs(error) >= 1)
			throw new IllegalArgumentException();
		if (error == 0)
			return value;
		return (float) rdgen.nextGaussian(value, value*error/3);
	}
	
	
	/**
	 * compute the real quantity to add, keeping the quantity lower bound and upper bound.
	 * quantity may be negative.
	 * @param current
	 * 	the current quantity
	 * @param quantity
	 * 	quantity to add
	 * @param reserve
	 * 	lower bound (inclusive)
	 * @param limit
	 * 	upper bound (inclusive)
	 * @return
	 */
	public static float getRealAddQuatity(float current, float quantity, float reserve, float limit){
		
		if (reserve > limit){
			throw new IllegalArgumentException();
		}
		
		if (quantity > 0){
			if (current >= limit){
				quantity = 0;
			}else {
				/**
				 * compute in double to avoid overflow
				 */
				double upperBound = (double)limit - current;
				if (quantity > upperBound){
					quantity = (float) upperBound;
				}
			}
		}else{
			if (current <= reserve){
				quantity = 0;
			}else{
				double lowerBound = (double)reserve - current;
				if (quantity < lowerBound){
					quantity = (float) lowerBound;
				}
			}
		}
		return quantity;
	}
	
	/**
	 * round the float point vector to integer
	 * @param vector
	 * @return
	 */
	public static int[] round(float[] vector){
		if (vector == null)
			return null;
		int[] round = new int[vector.length];
		for (int i = 0; i < round.length; i++) {
			round[i] = Math.round(vector[i]);
		}
		return round;
	}
	
	/**
	 * is two target in range?
	 * @param v1
	 * @param v2
	 * @param range
	 * @return
	 */
	public static boolean isInRange(Vector2f v1, Vector2f v2, float range){
		return (v1.x-v2.x)*(v1.x-v2.x) + (v1.y-v2.y)*(v1.y-v2.y) <= range*range;  
	}
	
}

package starsector.mod.nf.support;


/**
 * give a chance to user to intercept the operated data
 * @author fengyuan
 *
 */
public interface OperationInterceptor<ParametersType extends Parameters, OperationTargetType> {
	
	/**
	 * intercept before operation.
	 * return true if you don't want to generate.
	 * You can modify the realXXX parameters to control the operation.
	 */
	boolean intercept(ParametersType params);
	
	/**
	 * intercept after operation.
	 * You can do some custom operation.
	 * @param sectorEntityToken
	 */
	void intercept(OperationTargetType object);
	
}

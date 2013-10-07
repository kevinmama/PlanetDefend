package starsector.mod.nf.support;

/**
 * default implementation of {@link OperationInterceptor}
 * @author fengyuan
 *
 * @param <ParamsType>
 * @param <GeneratedObjectType>
 */
public class SectorGenerationAdapter<ParamsType extends Parameters,GeneratedObjectType> implements OperationInterceptor<ParamsType, GeneratedObjectType>{

	@Override
	public boolean intercept(ParamsType params) {
		return false;
	}

	@Override
	public void intercept(GeneratedObjectType sectorEntityToken) {
	}

}

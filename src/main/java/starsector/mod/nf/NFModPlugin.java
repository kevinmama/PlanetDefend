package starsector.mod.nf;

import com.fs.starfarer.api.BaseModPlugin;

public class NFModPlugin extends BaseModPlugin{

	@Override
	public void onApplicationLoad() throws Exception {
		NF.init();
	}
	
}

package starsector.mod.pld;

import starsector.mod.nf.NFModPlugin;
import starsector.mod.nf.log.Logger;

import com.fs.starfarer.api.Global;

public class PLDModPlugin extends NFModPlugin{
	
	Logger log = Logger.getLogger(PLDModPlugin.class);
	
	@Override
	public void onApplicationLoad() throws Exception {
		log.info("onApplicationLoad");
		super.onApplicationLoad();
	}
	
	@Override
	public void onNewGame() {
		log.info("onNewGame:");
		new CompatibleGenerator().generate(Global.getSector());
		super.onNewGame();
	}
	
	@Override
	public void afterGameSave() {
		log.info("afterGameSave:");
		super.afterGameSave();
	}
	
	@Override
	public void onGameLoad() {
		log.info("onGameLoad:");
		super.onGameLoad();
	}
	
}

package starsector.mod.pld;

import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;
import starsector.mod.pld.domain.PLDObjectFactory;
import starsector.mod.pld.domain.PLDRegistry;


/**
 * Planet Defend. holds global things
 * @author fengyuan
 *
 */
public class PLD{
	
	private static Logger log = Logger.getLogger(PLD.class, AppenderType.MESSAGE);
	
	private static PLD INS;
	private PLDRegistry registry;
	private PLDObjectFactory factory;
	
	/**
	 * get the global registry
	 * @return
	 */
	public static final PLDRegistry getRegistry(){
		return INS.registry;
	}
	
	/**
	 * get the PLD object factory. All domain object of PLD should created by this factory.
	 * The factory create objects and register it to PLDRegistry.
	 */
	public static final PLDObjectFactory getFactory(){
		return INS.factory;
	}
	
	
	/**
	 * create the PLD singleton
	 * @return
	 */
	static PLD create(){
		INS = new PLD();
		INS.registry = new PLDRegistry();
		INS.factory = new PLDObjectFactory(INS.registry);
		PLDSettings.reload();
		return INS;
	}
	
	/**
	 * reload the PLD static reference and PLD settings.
	 */
	void reload(){
		if (INS != this){
			INS = this;
			PLDSettings.reload();
			log.info("PLD reloaded");
		}
	}
	
	
}

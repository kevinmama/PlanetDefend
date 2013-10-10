package starsector.mod.nf;

import starsector.mod.nf.event.CoreEventType;

import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;


/**
 * client code implements this interface to initialize campaign.
 * 
 * @author fengyuan
 *
 */
public abstract class NFSectorGeneratorPlugin implements SectorGeneratorPlugin{
	
	/**
	 * initialize global things. Here Global.getSector() will return null.
	 * @param nf
	 */
	public abstract void beforeGenerateSector(NF nf);
	
	/**
	 * you can register plugins and scripts to sectorAPI
	 * @param nf
	 */
	public abstract void afterGenerateSector(NF nf);
	
	@Override
	public final void generate(SectorAPI sector) {
		
		//
		// initialize framework objects
		//
		NF nf = NF.create();
		
		//
		// initialize campaign objects
		//
		beforeGenerateSector(nf);
		
		
		generateSector(nf);
		
		//
		// initialize sector related object
		//
		nf.afterGenerateSector(sector);
		afterGenerateSector(nf);
		
		//
		// raise sector generated event
		//
		NF.getEventbus().dispatch(CoreEventType.SECTOR_GENERATED);
	}
	
	/**
	 * generate systems, planets, etc...
	 * @param nf
	 */
	public abstract void generateSector(NF nf);
	
}

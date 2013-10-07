package starsector.mod.pld;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;

/**
 * keep static global object update
 * @author fengyuan
 *
 */
public class PLDDaemon extends BaseEventListener{
	
	private PLD pld;
	
	public PLDDaemon(PLD pld) {
		setName("PLDDaemon");
		listen(CoreEventType.SECTOR_RELOAD);
		this.pld = pld;
	}
	
	@Override
	public void handle(Event event) {
		pld.reload();
	}

}

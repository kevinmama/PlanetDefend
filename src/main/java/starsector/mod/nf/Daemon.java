package starsector.mod.nf;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.log.Logger;

/**
 * daemon listener, check if static reference of NebularFantasy is gone.
 * @author fengyuan
 *
 */
class Daemon extends BaseEventListener{

	private static Logger log = Logger.getLogger(Daemon.class);
	
	private NF nebularFantasy;
	
	public Daemon(NF nebularFantasy) {
		this.nebularFantasy = nebularFantasy;
		setName("NebularFantasyListener");
		listen(CoreEventType.HEARTBEAT_FRAME);
	}
	
	@Override
	public void handle(Event event) {
		switch ((CoreEventType)event.getEventType()) {
		case HEARTBEAT_FRAME:
			if (NF.INS != nebularFantasy){
				log.info("detect NEBULAR_FANTASY changed");
				NFSettings.reload();
				nebularFantasy.reload();
				eventbus.dispatch(CoreEventType.SECTOR_RELOAD);
			}
			break;
		default:
			break;
		}
	}

}

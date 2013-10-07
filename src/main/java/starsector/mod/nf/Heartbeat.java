package starsector.mod.nf;

import static starsector.mod.nf.event.CoreEventType.HEARTBEAT_CYCLE;
import static starsector.mod.nf.event.CoreEventType.HEARTBEAT_DAY;
import static starsector.mod.nf.event.CoreEventType.HEARTBEAT_HOUR;
import static starsector.mod.nf.event.CoreEventType.HEARTBEAT_MONTH;
import static starsector.mod.nf.event.CoreEventType.HEARTBEAT_WEEK;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.EventBus;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import com.fs.starfarer.api.campaign.SectorAPI;



/**
 * generate heartbeat event.
 * bug: when game loaded, 'hour' will reset to zero.
 * @author fengyuan
 *
 */
class Heartbeat implements EveryFrameScript{

	/**
	 * timestamp when heartbeat start
	 */
	private long initialTimeStamp;
	/**
	 * hour, day, week, month, cycle
	 */
	private int[] lastTimeVector;
	
	/**
	 * use EveryFrameScript to save eventbus and its related instance
	 */
	private EventBus eventbus;
	
	public Heartbeat(EventBus eventbus) {
		lastTimeVector = new int[5];
		for (int i = 0; i < lastTimeVector.length; i++) {
			lastTimeVector[i] = -1;
		}
		this.eventbus = eventbus;
		initialTimeStamp = Global.getSector().getClock().getTimestamp();
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public boolean runWhilePaused() {
		return true;
	}

	@Override
	public void advance(float amount) {
		eventbus.dispatchEvents();
		SectorAPI sector = Global.getSector();
		eventbus.dispatch(CoreEventType.HEARTBEAT_FRAME);
		if (!Global.getSector().isPaused()){
			eventbus.dispatch(CoreEventType.HEARTBEAT_ADVANCE);
			CampaignClockAPI clock = sector.getClock();
			if (performHeartbeat(0, clock.getHour(), HEARTBEAT_HOUR)
				&& performHeartbeat(1, clock.getDay(), HEARTBEAT_DAY)
				&& performHeartbeat(2, ((int)clock.getElapsedDaysSince(initialTimeStamp))/7, HEARTBEAT_WEEK)
				&& performHeartbeat(3, clock.getMonth(), HEARTBEAT_MONTH)
				&& performHeartbeat(4, clock.getCycle(), HEARTBEAT_CYCLE)){
				;
			}
		}
	}
	
	/**
	 * check if time update and raise heartbeat event
	 */
	private boolean performHeartbeat(int vectorIdx, int value, CoreEventType heartbeatEventType){
		if (lastTimeVector[vectorIdx] != value){
			lastTimeVector[vectorIdx] = value;
			eventbus.dispatch(heartbeatEventType);
			
//			if (heartbeatEventType == EventType.HEARTBEAT_HOUR)
//				LogSink.getLogSink(Heartbeat.class).info(
//					"("+lastTimeVector[0]+","+lastTimeVector[1]+","+lastTimeVector[2]+","+lastTimeVector[3]+")");
			
			return true;
		}else{
			return false;
		}
	}
}

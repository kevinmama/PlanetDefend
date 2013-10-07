package starsector.mod.nf.autodrive;

import org.lwjgl.input.Keyboard;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.event.KeyPressEvent;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;

/**
 * can let AI take control of player's fleet.
 * all key except 'KEY_LSHIFT' would stop auto driving.
 * @author fengyuan
 *
 */
public abstract class AutoDriver extends BaseEventListener{
	
	/**
	 * ignore some key/mouse event, for a delay
	 */
	private boolean listenKeyPress = false;
	protected CampaignFleetAPI fleet;

	public AutoDriver(CampaignFleetAPI fleet) {
		this.fleet = fleet;
		setName(AutoDriver.class.getSimpleName());
		listen(
			CoreEventType.KEY_PRESS,
			CoreEventType.MOUSE_CLICK,
			CoreEventType.HEARTBEAT_FRAME,
			CoreEventType.HEARTBEAT_HOUR
		);
	}
	
	@Override
	public void handle(Event event) {
		Enum<?> eventType = event.getEventType();
		if (eventType instanceof CoreEventType){
			switch((CoreEventType)eventType){
			// press any key to stop auto driving
			case KEY_PRESS:
				if (((KeyPressEvent)event).getKeyCode() == Keyboard.KEY_LSHIFT){
					return;
				}
			case MOUSE_CLICK:
				if (listenKeyPress)
					dispose();
				break;
			case HEARTBEAT_FRAME:
				if (!fleet.isAlive())
					dispose();
				else
					drive();
				break;
			case HEARTBEAT_HOUR:
				// there is still a little probability stop auto drive at once.
				// just ignore
				listenKeyPress = true;
				break;
			default:
			}
		}
		
	}
	
	/**
	 * auto drive action per frame
	 */
	protected abstract void drive();
	
}

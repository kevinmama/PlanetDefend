package starsector.mod.nf;

import static starsector.mod.nf.event.CoreEventType.MOUSE_CLICK;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.event.KeyPressEvent;




/**
 * generate Keyboard/Mouse event
 * @author fengyuan
 *
 */
class InputEventSource extends BaseEventListener{
	
	private int curDownKey = Keyboard.CHAR_NONE;
	private int curDownButton = -1;
	
	public InputEventSource() {
		setName(InputEventSource.class.getSimpleName());
		listen(CoreEventType.HEARTBEAT_FRAME);
	}

	@Override
	public void handle(Event event) {
		//
		// if keyboard is down, record it
		//
		if (Keyboard.getEventKeyState()){
			curDownKey = Keyboard.getEventKey();
		}else{
			// keyboard is release, so check its key
			// if it is the last down key, show generate a key press event
			if (curDownKey == Keyboard.getEventKey()){
				if (curDownKey != Keyboard.CHAR_NONE){
					curDownKey = Keyboard.CHAR_NONE;
					KeyPressEvent keyPressEvent = new KeyPressEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter());
					eventbus.dispatch(keyPressEvent);
				}
			}
		}
		
		//
		// listen mouse click
		//
		if (Mouse.getEventButtonState()){	// press
			curDownButton = Mouse.getEventButton();
		}else{
			// release mouse
			if (curDownButton >= 0 && curDownButton == Mouse.getEventButton()){
				curDownButton = -1;
				eventbus.dispatch(MOUSE_CLICK);
			}
			
		}
	}

}

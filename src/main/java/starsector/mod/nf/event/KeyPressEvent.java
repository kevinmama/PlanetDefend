package starsector.mod.nf.event;


public class KeyPressEvent extends BaseEvent{
	
	private int keyCode;
	private char keyChar;
	public KeyPressEvent(int keyCode, char keyChar) {
		super(CoreEventType.KEY_PRESS);
		this.keyCode = keyCode;
		this.keyChar = keyChar;
	}
	public int getKeyCode() {
		return keyCode;
	}
	public char getKeyChar() {
		return keyChar;
	}
}

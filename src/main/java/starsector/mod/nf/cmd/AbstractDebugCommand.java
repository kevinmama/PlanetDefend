package starsector.mod.nf.cmd;

import starsector.mod.nf.NFSettings;

public abstract class AbstractDebugCommand implements KeyboardCommand{
	@Override
	public boolean isActive() {
		return NFSettings.debugging;
	}
}

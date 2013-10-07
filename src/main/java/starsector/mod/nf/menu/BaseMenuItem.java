package starsector.mod.nf.menu;

/**
 * default menu item implementation.
 * @author fengyuan
 *
 * @param <Context>
 */
public class BaseMenuItem implements MenuItem{
	
	private boolean visible = true;

	@Override
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	@Override
	public void onSelect(Object context) {
	}

}

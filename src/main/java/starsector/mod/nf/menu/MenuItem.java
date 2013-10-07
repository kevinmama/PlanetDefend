package starsector.mod.nf.menu;

public interface MenuItem {
	
	/**
	 * is menu visible?
	 * @return
	 */
	boolean isVisible();
	
	/**
	 * trigger the item with context.
	 * @param context
	 */
	void onSelect(Object context);
	
}

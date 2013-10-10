package starsector.mod.nf.menu;

import java.util.Collection;

public class BaseDlgMenu extends BaseDlgMenuItem implements DlgMenu{
	
	private BaseMenu<DlgMenuItem> impl = new BaseMenu<DlgMenuItem>();
	
	public BaseDlgMenu(String text) {
		super(text);
	}
	
	public BaseDlgMenu(String text, BaseDlgMenu parent){
		super(text, parent);
	}
	
	public BaseDlgMenu(String text, BaseDlgMenu parent, int keyCode){
		super(text,parent, keyCode);
	}
	
	/**
	 * create proxy, reusing the impl's onSelect and onMouseOver function
	 * @param item
	 */
	public static BaseDlgMenu createProxy(
			String text,
			BaseDlgMenu parent,
			int keyCode,
			final DlgMenuItem impl){
		return new BaseDlgMenu(text, parent, keyCode){
			@Override
			public void onSelect(Object context) {
				impl.onSelect(context);
			}
			@Override
			public void onMouseOver() {
				impl.onMouseOver();
			}
		};
	}

	public BaseDlgMenu addItem(DlgMenuItem item) {
		impl.addItem(item);
		return this;
	}

	public Collection<DlgMenuItem> getItems() {
		return impl.getItems();
	}

}

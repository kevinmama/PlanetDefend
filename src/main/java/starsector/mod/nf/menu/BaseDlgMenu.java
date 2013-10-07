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

	public BaseDlgMenu addItem(DlgMenuItem item) {
		impl.addItem(item);
		return this;
	}

	public Collection<DlgMenuItem> getItems() {
		return impl.getItems();
	}

}

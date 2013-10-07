package starsector.mod.nf.menu;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * default menu implementation
 * @author fengyuan
 *
 */
public class BaseMenu<I extends MenuItem> extends BaseMenuItem implements Menu<I>{
	
	private Set<I> items = new LinkedHashSet<I>();
	
	public Menu<I> addItem(I item){
		this.items.add(item);
		return this;
	}
	
	@Override
	public Collection<I> getItems(){
		return items;
	}
	
}

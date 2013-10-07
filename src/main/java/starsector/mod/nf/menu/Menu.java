package starsector.mod.nf.menu;

import java.util.Collection;

/**
 * common menu interface
 * @author fengyuan
 *
 * @param <Context>
 */
public interface Menu<I extends MenuItem> extends MenuItem{

	public abstract Collection<I> getItems();

	public abstract boolean isVisible();

}
package starsector.mod.nf.menu;

import java.util.Collection;
import java.util.Stack;

/**
 * @author fengyuan
 *
 * @param <Context>
 */
public class BacktraceMenuVisitor<M extends Menu<I>, I extends MenuItem> implements MenuVisitor{
	
	/**
	 * menu visit stack
	 */
	private Stack<M> stack = new Stack<M>();
	
	public BacktraceMenuVisitor(M top) {
		stack.push(top);
	}
	
	/**
	 * get current's sub menu,
	 * returns empty collection if current isn't a menu. 
	 * @return
	 */
	public Collection<I> getSubItems(){
		return stack.peek().getItems();
	}
	
	/**
	 * trigger a menu, and trace it
	 * @param item
	 * @param context
	 * @return
	 * 	true if select successfully
	 */
	public void select(M item, Object context){
		stack.push(item);
		item.onSelect(context);
	}
	
	/**
	 * trigger a menu item
	 * @param item
	 */
	public void select(I item, Object context){
		//
		// try to select menu first
		//
		if (item instanceof Menu){
			try{
				@SuppressWarnings("unchecked")
				M menu = (M) item;
				select(menu, context);
				return;
			}catch(ClassCastException e){
			}
		}
		
		item.onSelect(context);
	}
	
	/**
	 * back to the parent menu, and return it
	 * @return
	 */
	public M back(){
		stack.pop();
		return stack.peek();
	}
	
	/**
	 * returns the current item
	 * @return
	 */
	public M cur(){
		return stack.peek();
	}
	
	/**
	 * back to the top menu
	 */
	public M top(){
		return stack.firstElement();
	}
	
}

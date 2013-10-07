package starsector.mod.nf.support;

import java.util.ArrayList;
import java.util.List;

public class CollectionSupport {
	
	/**
	 * select sublist by given indexes
	 * @param input
	 * @param indexes
	 * @return
	 */
	public static <T> List<T> select(List<T> input, int[] indexes){
		ArrayList<T> list = new ArrayList<T>(indexes.length);
		for (int i : indexes) {
			list.add(input.get(i));
		}
		return list;
	}

}

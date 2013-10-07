package starsector.mod.nf.support;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import com.fs.starfarer.api.Global;

/**
 * help to interactive with settings
 * @author fengyuan
 *
 */
public class SettingSupport {
	/**
	 * read lines from file
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static List<String> readLines(String filename) throws IOException, JSONException{
		String text = Global.getSettings().loadText(filename);
		String[] split = text.split("\r\n|\n");
		LinkedList<String> list = new LinkedList<String>();
		for (String string : split) {
			list.add(string);
		}
		return list;
	}
}

package httpclient.common;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nilif on 2016/5/3.
 */

public class JsonUtil {

   public static Map<String, String> JsontoMap(String object){
		try {
		    JSONTokener jsonParser = new JSONTokener(object);
		    // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
		    // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
		    JSONObject person = (JSONObject) jsonParser.nextValue();
		    // 接下来的就是JSON对象的操作了
		    Iterator<String> keyIter= person.keys();
			String key;
			String value ;
			Map<String, String> valueMap = new HashMap<String, String>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = person.get(key).toString();
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (JSONException ex) {
		    // 异常处理代码
		}
		return null;
	}

    }

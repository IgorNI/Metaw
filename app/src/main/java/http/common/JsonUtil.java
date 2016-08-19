package http.common;
/**
 * 
 * 此类用于Jason转Map以及其他数据结构转Json
 * 
 * */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.*;
public class JsonUtil {
//	public static Map<String, Object> JsontoMap(Object object){
//		try {
//			JSONObject jsonObject = JSONObject.fromObject(object);
//			
//			@SuppressWarnings("unchecked")
//			Iterator<String> keyIter= jsonObject.keys();
//			String key;
//			Object value ;
//			Map<String, Object> valueMap = new HashMap<String, Object>();
//			while (keyIter.hasNext()) {
//				key = keyIter.next();
//				value = jsonObject.get(key);
//				valueMap.put(key, value);
//			}
//			return valueMap;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public static Map<String, Object> StringToMap(String object){
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


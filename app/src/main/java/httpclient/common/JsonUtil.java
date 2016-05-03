package httpclient.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nilif on 2016/5/3.
 */
public class JsonUtil {

    // public static JSONArray ArrayToJson() {
    //
    // String[] arr = { "asd", "dfgd", "asd", "234" };
    // return JSONArray.fromObject(arr);
    //
    // }

    // 对象转换成JSON
    public JSONArray ObjectToJSON(Object object) {
        return JSONArray.fromObject(object);
    }

    // Map转换成json， 是用jsonObject
    public static JSONObject testMapToJSON(Map<String, Object> map) {
        return JSONObject.fromObject(map);
    }

    // List转换成JSON
    public static JSONArray ListToJSON(List<Object> list) {
        return JSONArray.fromObject(list);
    }

    //将json格式的字符串解析成Map对象
    public static HashMap<Object, Object> JsontoMap(Object object) {
        HashMap<Object, Object> data = new HashMap<Object, Object>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.fromObject(object);
        Iterator<?> it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            Object key = String.valueOf(it.next());
            Object value = (Object) jsonObject.get(key);
            data.put(key, value);
        }
        return data;
    }

    }

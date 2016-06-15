package httpclient.common;

//

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by nilif on 2016/5/3.
 */

public class JsonUtil {
    // 对象转换成JSON
    public net.sf.json.JSONArray ObjectToJSON(Object object) {
        return net.sf.json.JSONArray.fromObject(object);
    }

    // Map转换成json， 是用jsonObject
    public static net.sf.json.JSONObject testMapToJSON(Map<String, Object> map) {
        return net.sf.json.JSONObject.fromObject(map);
    }

    // List转换成JSON
    public static net.sf.json.JSONArray ListToJSON(List<Object> list) {
        return net.sf.json.JSONArray.fromObject(list);
    }

    //将json格式的字符串解析成Map对象
    public static HashMap<Object, Object> JsontoMap(Object object) {
        HashMap<Object, Object> data = new HashMap<>();
        JSONObject parse = JSON.parseObject(object.toString());
        for (Map.Entry<String, Object> entry : parse.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }
        return data;
    }
}

package httpclient.service;

import httpclient.common.JsonUtil;
import httpclient.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nilif on 2016/5/3.
 */

public class PatientService {
    /**
     * 用户登录
     *
     * @param url
     *            服务器接口URL
     * @param param
     *            请求参数，需要mobile和password。
     * @return 返回一个Map，其中message代表请求情况，success表示成功，其他情况见函数体
     */
    public static Map<Object, Object> Login(String url,
                                            HashMap<String, String> param) {
        String message;
        Map<Object, Object> data = new HashMap<Object, Object>();
        try {
            message = new HttpRequest().doPost(url, param);// 服务器返回的JSON
            Map<Object, Object> jsonMap = JsonUtil.JsontoMap(message);// JSON转Map
            // message代表请求情况
            if ("MobileOrPasswrodIEmpty".equals(jsonMap.get("message"))) {
                data.put("message", "用户名或密码为空");
            } else if ("MobileDoesNotExist".equals(jsonMap.get("message"))) {
                data.put("message", "用户不存在");
            } else if ("IncorrectPasswrod".equals(jsonMap.get("message"))) {
                data.put("message", "密码错误");
            } else if ("Success".equals(jsonMap.get("message"))) {
                // 将服务器Json的data（可能需要的数据）部分转换为Map
                data = JsonUtil.JsontoMap(jsonMap.get("data"));
                data.put("message", "sucess");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(data);
        return data;
    }

    /**
     * 添加数据
     *
     * @param url
     *            服务器接口URL
     * @param params
     *            请求参数，需要
     *            patientMobile(String),sensorData1(double),sensorData2(double
     *            ),sensorData3(double),sensorData4(double),time(long)。
     * @return 返回一个Map，其中message代表请求情况，success表示成功，其他情况见函数体
     */
    public static Map<Object, Object> AddData(String url, String params) {
        String message = new HttpRequest().doPostJason(url, params);// 服务器返回的JSON
        Map<Object, Object> jsonMap = JsonUtil.JsontoMap(message);// JSON转Map
        Map<Object, Object> data = new HashMap<Object, Object>();
        if ("DataIsEmpty".equals(jsonMap.get("message"))) {
            data.put("message", "传感器数据为空");
        } else if ("MobileIsEmpty".equals(jsonMap.get("message"))) {
            data.put("message", "用户为空");
        } else if ("InvalMobile".equals(jsonMap.get("message"))) {
            data.put("message", "电话号码不合法");
        } else if ("MobileDoseNotExists".equals(jsonMap.get("message"))) {
            data.put("message", "用户不存在");
        } else if ("SensorDataIsLessThan0".equals(jsonMap.get("message"))) {
            data.put("message", "传感器数据应该大于0");
        } else if ("Success".equals(jsonMap.get("message"))) {
            data = JsonUtil.JsontoMap(jsonMap.get("data"));
            data.put("message", "sucess");
        }
        System.out.println(jsonMap.get("message"));
        return data;
    }
    /**
     * 用户注册
     *
     * @param url
     *            服务器接口URL
     * @param params
     *            至少需要mobile和password，其他参数见截图
     * @return 返回一个Map，其中message代表请求情况，success表示成功，其他情况见函数体
     */
    public static Map<Object, Object> Register(String url, String params) {
        String message = new HttpRequest().doPostJason(url, params);
        Map<Object, Object> jsonMap = JsonUtil.JsontoMap(message);
        Map<Object, Object> data = new HashMap<Object, Object>();
        if ("MobileIsEmpty".equals(jsonMap.get("message"))) {
            data.put("message", "电话号码为空");
        } else if ("PasswordIsEmpty".equals(jsonMap.get("message"))) {
            data.put("message", "密码为空");
        } else if ("InvalidMobile".equals(jsonMap.get("message"))) {
            data.put("message", "电话号码不合法");
        } else if ("MobileExists".equals(jsonMap.get("message"))) {
            data.put("message", "电话号码已经存在");
        } else if ("Success".equals(jsonMap.get("message"))) {
            data = JsonUtil.JsontoMap(jsonMap.get("data"));
            data.put("message", "sucess");
        }
        System.out.println(data);
        return data;
    }
    /**
     * 获取用户信息
     *
     * @param url
     *            服务器接口URL
     * @param param
     *            mobile
     * @return 返回一个Map，其中message代表请求情况，success表示成功，其他情况见函数体
     */
    public static Map<Object, Object> getPatientInfoByMobile(String url,
                                                             String param) {
        Map<Object, Object> data = new HashMap<Object, Object>();
        try {
            String message = new HttpRequest().doGet(url, param);
            Map<Object, Object> jsonMap = JsonUtil.JsontoMap(message);
            if ("PatientMobileDoesNotExists".equals(jsonMap.get("message"))) {
                data.put("message", "用户不存在");
            } else if ("InvalidMobile".equals(jsonMap.get("message"))) {
                data.put("message", "请输入正确的电话号码");
            } else if ("PatientMobileIsEmpty".equals(jsonMap.get("message"))) {
                data.put("message", "电话号码为空");
            } else if ("Success".equals(jsonMap.get("message"))) {
                data = JsonUtil.JsontoMap(jsonMap.get("data"));
                data.put("message", "sucess");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(data);
        return data;
    }
    /**
     * 修改用户信息
     *
     * @param url
     *            服务器接口URL
     * @param params
     *           至少需要patientId，其他参数见截图
     * @return 返回一个Map，其中message代表请求情况，success表示成功，其他情况见函数体
     */
    public static Map<Object, Object> updatePatientInfo(String url,
                                                        String params) {
        String message = new HttpRequest().doPostJason(url, params);
        Map<Object, Object> jsonMap = JsonUtil.JsontoMap(message);
        Map<Object, Object> data = new HashMap<Object, Object>();
        if ("PatientIsEmpty".equals(jsonMap.get("message"))) {
            data.put("message", "用户为空");
        } else if ("MobileIsEmpty".equals(jsonMap.get("message"))) {
            data.put("message", "电话号码为空");
        } else if ("InvalidMobile".equals(jsonMap.get("message"))) {
            data.put("message", "电话号码不合法");
        } else if ("PatientDoesNotExists".equals(jsonMap.get("message"))) {
            data.put("message", "用户不存在");
        } else if ("MobileExists".equals(jsonMap.get("message"))) {
            data.put("message", "电话号码已注册");
        } else if ("PatientIdIsEmpty".equals(jsonMap.get("message"))){
            data.put("message", "用户Id为空");
        }
        else if ("Success".equals(jsonMap.get("message"))) {
            data = JsonUtil.JsontoMap(jsonMap.get("data"));
            data.put("message", "sucess");
        }
        System.out.println(data);
        return data;
    }
}
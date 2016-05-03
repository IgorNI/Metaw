package httpclient.test;

import httpclient.http.HttpUrl;
import httpclient.service.PatientService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nilif on 2016/5/3.
 */
public class Test {

    // public void onClickListener() {// 登陆函数使用例子
    // if(用户名为空){
    // 弹出“用户名不能为空”
    // return；
    // }
    // if(密码为空){
    // 弹出“密码不能为空”
    // }
    // if(用户名不为电话号码){
    // 弹出“请输入正确的电话号码”
    // }
    // HashMap<String, String> dataMap = new HashMap<String, String>();
    // dataMap.put("mobile", "15757115927");
    // dataMap.put("password", "123456");
    // Map<Object, Object> map = PatientService.Login(HttpUrl.PATIENT_LOGIN,
    // dataMap);
    // if (!"Success".equals(map.get("message"))) {
    // 弹出map.get("message");
    // }else{
    // 登陆成功进入其他页面
    // 。。。。
    // 。。。
    // }
    // }

    public static void main(String[] args) {
        /**
         * PatientService.Registe()用户注册
         * 至少需要mobile和passwrod,mobile、passwrod不能为空，mobile格式必须正确,每个mobile只能注册一次
         * 还可以添加 userName（String类型）用户名、 patientName（String）真实名、
         * province（String）省 city（String）市 、county（String）县 、details(string)具体地址
         * 你可以试一下格式不正确或者为空的情况; 返回一个Map，Map有一个主键为message，当message等于sucess时注册成功
         * HttpUrl.PATIENT_REGISTER注册URl，现在host为127.0.0.1你改一下再用
         * */
        String params = "{\"mobile\":\"15733333333\",\"password\":\"123456\"}";
        PatientService.Register(HttpUrl.PATIENT_REGISTER, params);

        /**
         * PatientService.Login()用户登陆，
         * 需要mobile和passwrod,mobile、passwrod不能为空，mobile格式必须正确 你可以试一下格式不正确或者为空的情况
         * 返回一个Map，Map有一个主键为message，当message等于sucess时登陆成功
         * HttpUrl.PATIENT_LOGIN注册URl，现在host为127.0.0.1你改一下再用
         * */
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("mobile", "15757115927");
        dataMap.put("password", "1 23456");
        // PatientService.Login(HttpUrl.PATIENT_LOGIN, dataMap);//

        /**
         * PatientService.getPatientInfoByMobile()获取用户信息，
         * 需要mobile和passwrod,mobile、passwrod不能为空，mobile格式必须正确 你可以试一下格式不正确或者为空的情况
         * 返回一个Map，Map有一个主键为message，当message等于sucess时登陆成功
         * */
        PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO,
                "15757115922");

        /**
         * PatientService.updatePatientInfo()更改用户信息， 至少需要patientId, 还可以添加
         * mobile（String类型）电话号码、passwrod（String类型）密码 userName（String类型）用户名、
         * patientName（String）真实名、 province（String）省 city（String）市
         * county（String）县 、details(string)具体地址
         * */
        Map<Object, Object> user = PatientService.getPatientInfoByMobile(
                HttpUrl.PATIENT_GETINFO, "15757115922");// 首先获取patientId
        System.out.println(user.get("patientId"));
        params = "{\"patientId\":\"" + user.get("patientId")
                + "\",\"mobile\":\"15757115922\"}";
        PatientService.updatePatientInfo(HttpUrl.PATIENT_UPDATE, params);// 然后根据patientId修改数据

        /**
         * PatientService.AddData()上传数据
         * 数据属性：sensorData1、sensorData2、sensorData3、sensorData4传感器1234数据
         * 数据类型为double大于0、patientMobile（String）病人电话号码 、time数据采集的时间（long）
         * */
        params = "{\"sensorData1\":\""
                + 22.0
                + "\",\"sensorData2\":\"10\",\"sensorData3\":\"22.0\",\"sensorData4\":\"22.0\",\"patientMobile\":\"15757115927\"}";
        ;
        // PatientService.AddData(HttpUrl.PATIENT_ADDDATA, params);
    }
}

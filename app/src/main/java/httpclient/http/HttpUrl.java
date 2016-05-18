package httpclient.http;

/**
 * Created by nilif on 2016/5/3.
 */

public class HttpUrl {
    public static final String HOST = "http://121.42.164.101:8080";// 服务器地址
    public static final String PATIENT_ADDDATA = HOST
            + "/member/patient/addData";// 添加数据的接口
    public static final String PATIENT_GETINFO = HOST
            + "/member/patient/getPatientByMobile/?mobile=";// 获取用户信息的接口
    public static final String PATIENT_LOGIN = HOST + "/member/patient/login";// 登陆的接口
    public static final String PATIENT_REGISTER = HOST
            + "/member/patient/register";// 注册的接口
    public static final String PATIENT_UPDATE = HOST
            + "/member/patient/update";// 注册的接口
    public static final String NLF_DATA = HOST
            + "/member/nlfData/addData";
}



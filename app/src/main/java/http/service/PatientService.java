package http.service;

import java.util.HashMap;
import java.util.Map;

import http.common.JsonUtil;
import http.http.HttpRequest;

public class PatientService {
	/**
	 * 鐢ㄦ埛鐧诲綍
	 * 
	 * @param url
	 *            鏈嶅姟鍣ㄦ帴鍙RL
	 * @param param
	 *            璇锋眰鍙傛暟锛岄渶瑕乵obile鍜宲assword銆?
	 * @return 杩斿洖涓?涓狹ap锛屽叾涓璵essage浠ｈ〃璇锋眰鎯呭喌锛宻uccess琛ㄧず鎴愬姛锛屽叾浠栨儏鍐佃鍑芥暟浣?
	 */
	public static Map<String, String> Login(String url,
			HashMap<String, String> param) {
		String message ;// 鏈嶅姟鍣ㄨ繑鍥炵殑JSON
		Map<String, String> data = new HashMap<String, String>();
		try {
			message = new HttpRequest().doPost(url, param);// 鏈嶅姟鍣ㄨ繑鍥炵殑JSON
			Map<String, String> jsonMap = JsonUtil.JsontoMap(message);// JSON杞琈ap
			// message浠ｈ〃璇锋眰鎯呭喌
			if ("MobileOrPasswrodIEmpty".equals(jsonMap.get("message"))) {
				data.put("message", "鐢ㄦ埛鍚嶆垨瀵嗙爜涓虹┖");
			} else if ("MobileDoesNotExist".equals(jsonMap.get("message"))) {
				data.put("message", "鐢ㄦ埛涓嶅瓨鍦?");
			} else if ("IncorrectPasswrod".equals(jsonMap.get("message"))) {
				data.put("message", "瀵嗙爜閿欒");
			} else if ("Success".equals(jsonMap.get("message"))) {
				// 灏嗘湇鍔″櫒Json鐨刣ata锛堝彲鑳介渶瑕佺殑鏁版嵁锛夐儴鍒嗚浆鎹负Map
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
	 * 娣诲姞鏁版嵁
	 * 
	 * @param url
	 *            鏈嶅姟鍣ㄦ帴鍙RL
	 * @param params
	 *            璇锋眰鍙傛暟锛岄渶瑕?
	 *            patientMobile(String),sensorData1(double),sensorData2(double
	 *            ),sensorData3(double),sensorData4(double),time(long)銆?
	 * @return 杩斿洖涓?涓狹ap锛屽叾涓璵essage浠ｈ〃璇锋眰鎯呭喌锛宻uccess琛ㄧず鎴愬姛锛屽叾浠栨儏鍐佃鍑芥暟浣?
	 */
	public static Map<String, String> AddData(String url, String params) {
		String message = new HttpRequest().doPostJason(url, params);// 鏈嶅姟鍣ㄨ繑鍥炵殑JSON
		Map<String, String> jsonMap = JsonUtil.JsontoMap(message);// JSON杞琈ap
		Map<String, String> data = new HashMap<String, String>();
		if ("DataIsEmpty".equals(jsonMap.get("message"))) {
			data.put("message", "浼犳劅鍣ㄦ暟鎹负绌?");
		} else if ("MobileIsEmpty".equals(jsonMap.get("message"))) {
			data.put("message", "鐢ㄦ埛涓虹┖");
		} else if ("InvalMobile".equals(jsonMap.get("message"))) {
			data.put("message", "鐢佃瘽鍙风爜涓嶅悎娉?");
		} else if ("MobileDoseNotExists".equals(jsonMap.get("message"))) {
			data.put("message", "鐢ㄦ埛涓嶅瓨鍦?");
		} else if ("SensorDataIsLessThan0".equals(jsonMap.get("message"))) {
			data.put("message", "浼犳劅鍣ㄦ暟鎹簲璇ュぇ浜?0");
		} else if ("Success".equals(jsonMap.get("message"))) {
			data = JsonUtil.JsontoMap(jsonMap.get("data"));
			data.put("message", "sucess");
		}
		System.out.println(jsonMap.get("message"));
		return data;
	}
	/**
	 * 鐢ㄦ埛娉ㄥ唽
	 * 
	 * @param url
	 *            鏈嶅姟鍣ㄦ帴鍙RL
	 * @param params
	 *            鑷冲皯闇?瑕乵obile鍜宲assword锛屽叾浠栧弬鏁拌鎴浘
	 * @return 杩斿洖涓?涓狹ap锛屽叾涓璵essage浠ｈ〃璇锋眰鎯呭喌锛宻uccess琛ㄧず鎴愬姛锛屽叾浠栨儏鍐佃鍑芥暟浣?
	 */
	public static Map<String, String> Register(String url, String params) {
		String message = new HttpRequest().doPostJason(url, params);
		Map<String, String> jsonMap = JsonUtil.JsontoMap(message);
		Map<String, String> data = new HashMap<String, String>();
		if ("MobileIsEmpty".equals(jsonMap.get("message"))) {
			data.put("message", "鐢佃瘽鍙风爜涓虹┖");
		} else if ("PasswordIsEmpty".equals(jsonMap.get("message"))) {
			data.put("message", "瀵嗙爜涓虹┖");
		} else if ("InvalidMobile".equals(jsonMap.get("message"))) {
			data.put("message", "鐢佃瘽鍙风爜涓嶅悎娉?");
		} else if ("MobileExists".equals(jsonMap.get("message"))) {
			data.put("message", "鐢佃瘽鍙风爜宸茬粡瀛樺湪");
		} else if ("Success".equals(jsonMap.get("message"))) {
			data = JsonUtil.JsontoMap(jsonMap.get("data"));
			data.put("message", "sucess");
		}
		System.out.println(data);
		return data;
	}
	/**
	 * 鑾峰彇鐢ㄦ埛淇℃伅
	 * 
	 * @param url
	 *            鏈嶅姟鍣ㄦ帴鍙RL
	 * @param param
	 *            mobile
	 * @return 杩斿洖涓?涓狹ap锛屽叾涓璵essage浠ｈ〃璇锋眰鎯呭喌锛宻uccess琛ㄧず鎴愬姛锛屽叾浠栨儏鍐佃鍑芥暟浣?
	 */
	public static Map<String, String> getPatientInfoByMobile(String url,
			String param) {
		Map<String, String> data = new HashMap<String, String>();
		try {
			String message = new HttpRequest().doGet(url, param);
			Map<String, String> jsonMap = JsonUtil.JsontoMap(message);
			if ("PatientMobileDoesNotExists".equals(jsonMap.get("message"))) {
				data.put("message", "鐢ㄦ埛涓嶅瓨鍦?");
			} else if ("InvalidMobile".equals(jsonMap.get("message"))) {
				data.put("message", "璇疯緭鍏ユ纭殑鐢佃瘽鍙风爜");
			} else if ("PatientMobileIsEmpty".equals(jsonMap.get("message"))) {
				data.put("message", "鐢佃瘽鍙风爜涓虹┖");
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
	 * 淇敼鐢ㄦ埛淇℃伅
	 * 
	 * @param url
	 *            鏈嶅姟鍣ㄦ帴鍙RL
	 * @param params
	 *           鑷冲皯闇?瑕乸atientId锛屽叾浠栧弬鏁拌鎴浘
	 * @return 杩斿洖涓?涓狹ap锛屽叾涓璵essage浠ｈ〃璇锋眰鎯呭喌锛宻uccess琛ㄧず鎴愬姛锛屽叾浠栨儏鍐佃鍑芥暟浣?
	 */
	public static Map<String, String> updatePatientInfo(String url,
			String params) {
		String message = new HttpRequest().doPostJason(url, params);
		Map<String, String> jsonMap = JsonUtil.JsontoMap(message);
		Map<String, String> data = new HashMap<String, String>();
		if ("PatientIsEmpty".equals(jsonMap.get("message"))) {
			data.put("message", "鐢ㄦ埛涓虹┖");
		} else if ("MobileIsEmpty".equals(jsonMap.get("message"))) {
			data.put("message", "鐢佃瘽鍙风爜涓虹┖");
		} else if ("InvalidMobile".equals(jsonMap.get("message"))) {
			data.put("message", "鐢佃瘽鍙风爜涓嶅悎娉?");
		} else if ("PatientDoesNotExists".equals(jsonMap.get("message"))) {
			data.put("message", "鐢ㄦ埛涓嶅瓨鍦?");
		} else if ("MobileExists".equals(jsonMap.get("message"))) {
			data.put("message", "鐢佃瘽鍙风爜宸叉敞鍐?");
		} else if ("PatientIdIsEmpty".equals(jsonMap.get("message"))){
			data.put("message", "鐢ㄦ埛Id涓虹┖");
		}
		else if ("Success".equals(jsonMap.get("message"))) {
			data = JsonUtil.JsontoMap(jsonMap.get("data"));
			data.put("message", "sucess");
		}
		System.out.println(data);
		return data;
	}
}

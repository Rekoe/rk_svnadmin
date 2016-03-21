package com.rekoe.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.nutz.lang.util.NutMap;

/**
 * 工具类
 */
public class CommonUtils {

	/**
	 * 正则表达式：验证用户名
	 */
	public static final String REGEX_USERNAME = "^[a-zA-Z0-9]{6,16}$";

	/**
	 * 正则表达式：验证密码
	 */
	public static final String REGEX_PASSWORD = "{6,16}$";

	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 校验用户名
	 * 
	 * @param username
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUsername(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	/**
	 * 校验密码
	 * 
	 * @param password
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isPassword(String password) {
		return Pattern.matches(REGEX_PASSWORD, password);
	}

	private final static Random rand = new Random();

	public static int nextInt(final int min, final int max) {
		int tmp = Math.abs(rand.nextInt());
		return tmp % (max - min + 1) + min;

	}

	/**
	 * 组装全局字符串替换mao
	 */
	public static Map<String, String> populateWordsMap(String str) {
		if (StringUtils.isNotBlank(str)) {
			Map<String, String> wordsMap = new HashMap<String, String>();
			String[] keyValueArr = str.split(",");
			String[] tempArr = null;
			for (String keyValue : keyValueArr) {
				if (keyValue.contains("=")) {
					tempArr = keyValue.split("=");
					wordsMap.put(tempArr[0], tempArr[1]);
				} else {
					wordsMap.put(keyValue, "REKOE");
				}
			}
			return wordsMap;
		}
		return null;
	}

	/**
	 * 组装代理服务器列表
	 */
	public static List<Map<String, Integer>> populateProxyServer(String proxyServerStr) {
		List<Map<String, Integer>> resultList = new ArrayList<Map<String, Integer>>();
		String[] proxyArr = proxyServerStr.split(",");
		String[] tempArr = null;
		Map<String, Integer> proxyMap = null;
		for (String proxy : proxyArr) {
			tempArr = proxy.split(":");
			if (tempArr.length == 2 && !StringUtils.isBlank(tempArr[0]) && !StringUtils.isBlank(tempArr[1])) {
				proxyMap = new HashMap<String, Integer>();
				proxyMap.put(StringUtils.trim(tempArr[0]), Integer.parseInt(StringUtils.trim(tempArr[1])));
				resultList.add(proxyMap);
			}
		}
		return resultList;
	}

	public static boolean isInnerIP(String ipAddress) {
		boolean isInnerIp = false;
		long ipNum = getIpNum(ipAddress);
		/**
		 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
		 * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
		 **/
		long aBegin = getIpNum("10.0.0.0");
		long aEnd = getIpNum("10.255.255.255");
		long bBegin = getIpNum("172.16.0.0");
		long bEnd = getIpNum("172.31.255.255");
		long cBegin = getIpNum("192.168.0.0");
		long cEnd = getIpNum("192.168.255.255");
		isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ipAddress.equals("127.0.0.1");
		return isInnerIp;
	}

	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}

	public static NutMap getRequestParametersMap(final HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, String[]> map = request.getParameterMap();
		NutMap paramsMap = NutMap.NEW();
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			String values[] = entry.getValue();
			paramsMap.put(key, values[0].toString());
		}
		return paramsMap;
	}
}
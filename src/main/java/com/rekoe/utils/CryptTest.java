package com.rekoe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ArrayUtils;
import org.nutz.repo.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES对称加密 RSA非对称加密 Hex加密 私钥签名，公钥验证
 */
public class CryptTest {

	private static final String UTF8 = "UTF-8";

	public static void main(String[] args) throws Exception {
		String data = "123456";

		/**
		 * AES对称加密
		 */
		String key = "$i%2i9343kl3#df5"; // AES 16位的密钥
		String aesEncoded = AesUtils.AESEncrypt(key, data);
		String aesDecoded = AesUtils.AESDecrypt(key, aesEncoded);
		System.out.println("\naesEncoded: " + aesEncoded + "\naesDecoded: " + aesDecoded);

		/**
		 * RSA非对称加密
		 */
		Map<String, String> keys = RSAUtils.generateKey("appKey");
		String publicKey = keys.get("publicKey");
		String privateKey = keys.get("privateKey");

		String rsaEncoded = RSAUtils.publicEncrypt(publicKey, data);
		String rsaDecoded = RSAUtils.privateDecrypt(privateKey, rsaEncoded);
		System.out.println("\nrsaEncoded: " + rsaEncoded + "\nrsaDecoded: " + rsaDecoded);

		String rsaEncodedBySegment = RSAUtils.publicEncryptBySegment(publicKey, data);
		String rsaDecodedBySegment = RSAUtils.privateDecryptBySegment(privateKey, rsaEncodedBySegment);
		System.out.println("\nrsaEncodedBySegment: " + rsaEncodedBySegment + "\nrsaDecodedBySegment: " + rsaDecodedBySegment);

		/**
		 * Hex加密
		 */
		String hexEncoded = HexUtils.byte2hex(data.getBytes(UTF8));
		String hexDecoded = new String(HexUtils.hex2byte(hexEncoded), UTF8);
		System.out.println("\nhexEncoded: " + hexEncoded + "\nhexDecoded: " + hexDecoded);

		/**
		 * 数字签名
		 */
		String signature = SignUtils.sign(data, privateKey);
		boolean result = SignUtils.verify(data, signature, publicKey);
		System.out.println("\nsignature: " + signature + "\nresult: " + result);
	}

}

/**
 * AES对称加密
 */
class AesUtils {

	private static Logger log = LoggerFactory.getLogger(AesUtils.class);

	private static final String AESTYPE = "AES/ECB/PKCS5Padding";

	public static String AESEncrypt(String keyStr, String plainText) {
		byte[] encrypt = null;
		try {
			SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			encrypt = cipher.doFinal(plainText.getBytes());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new String(Base64.encodeToString(encrypt, false));
	}

	public static String AESDecrypt(String keyStr, String encryptData) {
		byte[] decrypt = null;
		try {
			SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypt = cipher.doFinal(Base64.decode(encryptData));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new String(decrypt);
	}
}

/**
 * RSA加密明文最大长度117字节，解密要求密文最大长度为128字节，所以在加密和解密的过程中需要分块进行。
 * 由于非对称加密速度极其缓慢，一般数据量较大时不使用它来加密而是使用对称加密，非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 */
class RSAUtils {

	// RSA最大加密明文大小
	private static final int MAX_ENCRYPT_BLOCK = 117;
	// RSA最大解密密文大小，分段时不是最大会报错 Caused by: javax.crypto.IllegalBlockSizeException:
	// Data must not be longer than 117 bytes
	private static final int MAX_DECRYPT_BLOCK = 128;

	private static final String UTF8 = "UTF-8";

	private static final String RSA_ALGORITHM = "RSA";

	private static Logger log = LoggerFactory.getLogger(AesUtils.class);

	public static String publicEncrypt(String strPublicKey, String content) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		byte[] keyBytes = base64Decode(strPublicKey);
		byte[] encryptData = publicEncrypt(getPublicKey(keyBytes), content.getBytes(UTF8));
		return base64Encode(encryptData);
	}

	public static byte[] publicEncrypt(PublicKey publicKey, byte[] content) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (publicKey == null) {
			throw new IllegalArgumentException("加密公钥为空");
		}
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(content);
	}

	public static String privateDecrypt(String strPrivateKey, String encryptData) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		byte[] keyBytes = base64Decode(strPrivateKey);
		byte[] content = privateDecrypt(getPrivateKey(keyBytes), base64Decode(encryptData));
		return new String(content, UTF8);
	}

	public static byte[] privateDecrypt(PrivateKey privateKey, byte[] encryptData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (privateKey == null) {
			throw new IllegalArgumentException("解密私钥为空");
		}
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(encryptData);
	}

	// 生成appkey对应的公钥，私钥
	public static Map generateKey(String appKey) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			kpg.initialize(1024);
			KeyPair keyPair = kpg.genKeyPair();
			String publicKey = base64Encode(keyPair.getPublic().getEncoded());
			String privateKey = base64Encode(keyPair.getPrivate().getEncoded());
			Map map = new HashMap();
			map.put("publicKey", publicKey);
			map.put("privateKey", privateKey);
			System.out.println(map);
			return map;
		} catch (Exception e) {
			log.error(appKey + "生成加密异常！");
		}
		return null;
	}

	public static PublicKey getPublicKey(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		return keyFactory.generatePublic(keySpec);
	}

	public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		return keyFactory.generatePrivate(keySpec);
	}

	private static String base64Encode(byte[] data) {
		if (data == null)
			return "";
		return Base64.encodeToString(data, false);
	}

	private static byte[] base64Decode(String base64String) throws IOException {
		if (base64String == null)
			return new byte[] {};
		return Base64.decode(base64String);
	}

	// 公钥加密，加密参数过长，采用分段方式
	public static String publicEncryptBySegment(String strPublicKey, String content) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
		byte[] keyBytes = base64Decode(strPublicKey);
		PublicKey publicKey = getPublicKey(keyBytes);
		if (publicKey == null) {
			throw new IllegalArgumentException("加密公钥为空");
		}
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return base64Encode(doFinalBySegment(cipher, content.getBytes(UTF8), MAX_ENCRYPT_BLOCK));
	}

	// 私钥解密，解密参数过长，采用分段方式
	public static String privateDecryptBySegment(String strPrivateKey, String content) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, InvalidKeyException {
		byte[] keyBytes = base64Decode(strPrivateKey);
		PrivateKey privateKey = getPrivateKey(keyBytes);
		if (privateKey == null) {
			throw new IllegalArgumentException("解密私钥为空");
		}
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(doFinalBySegment(cipher, base64Decode(content), MAX_DECRYPT_BLOCK), UTF8);
	}

	public static byte[] doFinalBySegment(Cipher cipher, byte[] data, int segmentSize) throws BadPaddingException, IllegalBlockSizeException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i = 0; i < data.length; i += segmentSize) {
			byte[] result = cipher.doFinal(ArrayUtils.subarray(data, i, i + segmentSize));
			out.write(result);
		}
		return out.toByteArray();
	}
}

/**
 * 数字摘要：采用单项Hash函数将需要加密的明文“摘要”成一串固定长度（128位）的密文，又称数字指纹
 * 数字签名：对传送数据生成摘要并使用私钥进行加密，遵循私钥签名，公钥验证的方式
 * 1.为了保证http请求数据的安全性和防篡改性。我们通常要对请求参数进行一些加密。 http请求参数:
 * (1)appkey：合作方平台标识，接口协议中通常会提供一个appKey作为唯一的标识。 (2)appSecret：作为接入密钥。
 * (3)time：Unix时间戳（10位） 2.对请求参数生成签名 sign = md5(待签名字符串) 待签名字符串为:
 * 将参数按参数名排序，拼接成键值对，如appKey=test_id&appSecret=A272ADAA3637&time=1423212323&username=lily
 * 3.将明文参数和签名一起发送给对方
 * appKey=test_id&appSecret=A272ADAA3637&time=1423212323&username=lily&sign=签名串
 */
class SignUtils {

	private static final String MD5_ALGORITHM = "MD5";
	private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
	private static final String UTF8 = "UTF-8";

	public static String sign(String data, String privateKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
		byte[] content = data.getBytes(UTF8);
		// 先做摘要
		MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
		md.update(content);
		byte[] digest = md.digest();
		// 进行签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(RSAUtils.getPrivateKey(Base64.decode(privateKey.getBytes())));
		signature.update(digest);
		byte[] signByte = signature.sign();
		String signText = Base64.encodeToString(signByte, false);
		System.out.println(signText);
		return signText;
	}

	public static boolean verify(String data, String signText, String publicKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		byte[] content = data.getBytes(UTF8);
		// 先做摘要
		MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
		md.update(content);
		byte[] digest = md.digest();
		// 进行验签
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(RSAUtils.getPublicKey(Base64.decode(publicKey.getBytes())));
		signature.update(digest);
		boolean result = signature.verify(Base64.decode(signText));
		return result;
	}
}

/**
 * Hex加密，hex十六进制和acsii码之间的转换，可用在url参数解决特殊字符的问题，将加密数据再加密成16进制
 */
class HexUtils {
	private static final int INT_VALUE_2 = 2;
	private static char[] c = "0123456789ABCDEF".toCharArray();

	public static String byte2hex(byte[] b) {
		if (b == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			sb.append(c[v / 0x10]).append(c[v % 0x10]);
		}
		return sb.toString();
	}

	public static byte[] hex2byte(String s) {
		if (s == null) {
			return null;
		}
		byte[] b = s.getBytes();
		if ((b.length % INT_VALUE_2) != 0) {
			throw new IllegalArgumentException();
		}
		byte[] b2 = new byte[b.length / INT_VALUE_2];
		for (int n = 0; n < b.length; n += INT_VALUE_2) {
			String item = new String(b, n, INT_VALUE_2);
			b2[n / INT_VALUE_2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
}
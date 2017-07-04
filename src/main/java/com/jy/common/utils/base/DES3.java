package com.jy.common.utils.base;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DES3 {
	/**
	 * 定义 加密算法,可用DES,DESede,Blowfish
	 */
	private static final String Algorithm = "DESede"; 
	/**
	 * 为加密密钥，长度为24字节
	 */
	public static String ENCYP_KEY= "_4rtyUng_9iOong_platform";
	final static byte[] keyBytes = ENCYP_KEY.getBytes();

	/**
	 * @param src 明文
	 * @return
	 */
	public static String encryptMode(String src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return byte2data(c1.doFinal(src.getBytes()));
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}
	private static String byte2data(byte[] bytes){
		return new String(Base64.encodeBase64(bytes));
	}

	/**
	 * @param src 密文
	 * @return
	 */
	public static String decryptMode(String src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return new String(c1.doFinal(data2byte(src)));
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}
	private static byte[] data2byte(String data) throws IOException{
		return Base64.decodeBase64(data.getBytes());
	}

	/**
	 * 转换成十六进制字符串
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) {

		/**
		 * 添加新安全算法,如果用JCE就要把它添加进去
		 */
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		
		
		String szSrc = "6or%uH#3,jy,20170526112100";//"po8$%#2@201103111314";
		System.out.println("加密前的字符串:" + szSrc);

		String encoded = encryptMode(szSrc);
		System.out.println("加密后的字符串1:" + encoded);
		System.out.println("加密后的字符串2:uj6uiU61j1s1muQn1CSeyF37IoYuL0KS");
		//String encoded = "0+QXr9Exl8VWHVGz6lSDhsCoD+JUuc3p91z48qD8b8DKDIFKrq1hxOBlQKUgxUvrJUQgjnMpNQq1M8+vjuYx7w==";
		String srcBytes = decryptMode(encoded);
		System.out.println("解密后的字符串:" + srcBytes);
	}
}

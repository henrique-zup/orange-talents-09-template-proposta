package br.com.zupacademy.henriquecesar.propostas.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jasypt.util.text.AES256TextEncryptor;

public class Criptografia {
	
	public static String criptografar(String string) {
		AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
		aesEncryptor.setPassword("${app.secret}");
		return aesEncryptor.encrypt(string);
	}

	
	public static String descriptografar(String string) {
		AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
		aesEncryptor.setPassword("${app.secret}");
		return aesEncryptor.decrypt(string);
	}

	
	public static String hash(String string) {
		try {
			MessageDigest messageDigest;
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(string.getBytes());
			return new String(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}

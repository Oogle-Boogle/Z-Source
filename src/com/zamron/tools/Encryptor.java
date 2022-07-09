package com.zamron.tools;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

	private static SecretKeySpec secretKey;
	private static byte[] key;

	public static String globalKey = "uHyowSN7^QmDss!!PP"; // NEVER FUCKING CHANGE THIS

	public static void setKey(String myKey)
	{
		MessageDigest sha = null;
		try {
			key = myKey.getBytes(StandardCharsets.UTF_8);
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String strToEncrypt, String secret)
	{
		try
		{
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
		}
		catch (Exception e)
		{
			System.out.println("Error while encrypting: " + e);
		}
		return null;
	}

	public static String decrypt(String strToDecrypt, String secret)
	{
		try
		{
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		}
		catch (Exception e)
		{
			System.out.println("Error while decrypting: " + e);
		}
		return null;
	}

	//This is a test method to prove the concept.
	/*public static void main(String[] args)
	{
		final String secretKey = "my super amazing key";

		String originalString = "rspshub.com";
		String encryptedString = Encryptor.encrypt(originalString, secretKey) ;
		String decryptedString = Encryptor.decrypt(encryptedString, secretKey) ;

		System.out.println(originalString);
		System.out.println(encryptedString);
		System.out.println(decryptedString);
	}*/
}

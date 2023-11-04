package com.cineevent.userservice.security;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.log4j.Log4j2;

/**
 * Encryptor to encrypt password using SHA-256
 * @author NripendraThakur
 *
 */
@Log4j2
public final class PasswordEncryptor {

	private PasswordEncryptor() {
		throw new IllegalStateException("Utility class");
	}

	public static String getEncryptedPassword(String password){
		try {
			byte[] hash = getSHA(password);
			if(hash == null) {
				return null;
			}
			return toHexString(getSHA(password));
		} catch (NoSuchAlgorithmException e) {
			log.error("Error Encrypting password",e);
		}
		return null;
	}

	private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
		/* MessageDigest instance for hashing using SHA256 */
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		/*
		 * digest() method called to calculate message digest of an input and return
		 * array of byte
		 */
		if(input == null) {
			return null;
		}
		return md.digest(input.getBytes(StandardCharsets.UTF_8));
	}

	private static String toHexString(byte[] hash) {
		/* Convert byte array of hash into digest */
		BigInteger number = new BigInteger(1, hash);

		/* Convert the digest into hex value */
		StringBuilder hexString = new StringBuilder(number.toString(16));

		/* Pad with leading zeros */
		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}

	public static boolean isPasswordValid(String inputPassword, String passwordInDB) {
		String encryptedValueOfInputPassword = getEncryptedPassword(inputPassword);
		if(encryptedValueOfInputPassword == null) {
			return false;
		}
		return encryptedValueOfInputPassword.equals(passwordInDB);
	}

}

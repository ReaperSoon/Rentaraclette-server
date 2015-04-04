package fr.rentaraclette.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SecurityService extends AbstractService {

	private static SecurityService instance = null;

	public SecurityService() {
		instance = this;
	}

	public static SecurityService getInstance() {
		if (instance == null)
			instance = new SecurityService();
		return instance;
	}

	/* Generate a random String to use it as salt */
	public String generateSalt() {
		String salt = UUID.randomUUID().toString().replaceAll("-", "");
		
		return salt;
	}

	/* Generate MD5 hash from a String */
	public String md5(String input) {
		String md5 = null;

		if (null == input)
			return null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes(), 0, input.length());
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}
}

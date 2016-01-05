package jacJarSoft.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
	private PasswordUtil() {;}
	
	public static String getPasswordMd5Hash(String password) {
		try {
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	        byte[] hashedBytes = digest.digest(password.getBytes("UTF-8"));
	 
	        return new String(hashedBytes,"US-ASCII");
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
	        throw new RuntimeException("Could not generate hash from String", ex);
	    }		
	}

}

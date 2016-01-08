package jacJarSoft.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
	private PasswordUtil() {;}
	
	public static String getPasswordMd5Hash(String password) {
		if (password == null)
			return "";
		try {
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	        byte[] hashedBytes = digest.digest(password.getBytes("ASCII"));
	 
	        return new String(hashedBytes,"ASCII");
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
	        throw new RuntimeException("Could not generate hash from String", ex);
	    }		
	}

}

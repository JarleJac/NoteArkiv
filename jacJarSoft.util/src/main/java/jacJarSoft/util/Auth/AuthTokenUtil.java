package jacJarSoft.util.Auth;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AuthTokenUtil {

	private static Logger logger = Logger.getLogger(AuthTokenUtil.class.getName());
	private static String ENC_KEY = "MayThe4thBeWithU";
	
	public static String createToken(String userId, String tokenPart) {
		logger.fine("Creating auth token for " + userId + "/" + tokenPart);
		AuthTokenInfo tokenInfo = new AuthTokenInfo(userId, tokenPart);
		String key = tokenInfo.getTokenString();
		logger.fine("Token created: " + key );
		String encryptedToken;
		try {
			encryptedToken = encrypt(key);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new AuthException("Unable to encrypt AuthToken",e);
		} 
		logger.fine("Encrypted: " + encryptedToken );
		return AuthTokenInfo.AuthSchema + " " + encryptedToken;
	}
	public static AuthTokenInfo getTokenInfo(String authString) {
		String encryptedToken = AuthTokenInfo.getTokenFromAuthString(authString);
		String tokenString;
		try {
			tokenString = decrypt(encryptedToken);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new AuthException("Unable to decrypt AuthToken",e);
		}
		AuthTokenInfo tokenInfo = AuthTokenInfo.fromTokenString(tokenString);
		return tokenInfo;
	}
	private static String decrypt(String encryptedToken) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Decoder decoder = Base64.getDecoder();
		byte[] encrypted = decoder.decode(encryptedToken);

		Cipher cipher = createCipher(Cipher.DECRYPT_MODE);
		String token = new String(cipher.doFinal(encrypted));

		return token;
	}

	private static String encrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = createCipher(Cipher.ENCRYPT_MODE);
		byte[] encrypted = cipher.doFinal(data.getBytes());

		Encoder encoder = Base64.getEncoder();

		// the encrypted String
		String enc = encoder.encodeToString(encrypted);
		return enc;
	}
	private static Cipher createCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Key aesKey = new SecretKeySpec(ENC_KEY.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(mode, aesKey);
		return cipher;
	}
}

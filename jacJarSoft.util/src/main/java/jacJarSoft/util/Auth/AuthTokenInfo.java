package jacJarSoft.util.Auth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AuthTokenInfo {
	public static final String AuthSchema = "JJSAUTH";
	private String user;
	private String tokenPart;
	private Date creationDateTime;
	private Date expiresDateTime;
	private String uuid;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public AuthTokenInfo(String user, String tokenPart, long minutes) {
		this.user = user;
		this.tokenPart = tokenPart;
		creationDateTime = new Date();
		uuid = UUID.randomUUID().toString().toUpperCase();
		expiresDateTime = new Date(creationDateTime.getTime() + TimeUnit.MINUTES.toMillis(minutes));
	}
	private AuthTokenInfo() {
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTokenPart() {
		return tokenPart;
	}

	public void setTokenPart(String tokenPart) {
		this.tokenPart = tokenPart;
	}

	public Date getCreationDateTime() {

		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Date getExpiresDateTime() {
		return expiresDateTime;
	}

	public void setExpiresDateTime(Date expiresDateTime) {
		this.expiresDateTime = expiresDateTime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTokenString() {
		String token = uuid + "|" + tokenPart + "|" + user + "|" + df.format(creationDateTime) + "|"
				+ df.format(expiresDateTime);
		return token;

	}

	@Override
	public String toString() {
		return AuthSchema + " " + getTokenString();
	}

	public static String getTokenFromAuthString(String authString) {
		if (authString.indexOf(AuthSchema) != 0)
			throw new IllegalArgumentException("Illegal authString");
		return authString.substring(AuthSchema.length() + 1);
	}

	public static AuthTokenInfo fromTokenString(String token) {
		String[] parts = token.split("\\|"); // Split on |
		if (parts.length != 4 && parts.length != 5)
			throw new IllegalArgumentException("Illegal token string");
		AuthTokenInfo tokenInfo = new AuthTokenInfo();
		tokenInfo.setUuid(parts[0]);
		tokenInfo.setTokenPart(parts[1]);
		tokenInfo.setUser(parts[2]);
		try {
			tokenInfo.setCreationDateTime(tokenInfo.df.parse(parts[3]));
			if (parts.length > 4)
				tokenInfo.setExpiresDateTime(tokenInfo.df.parse(parts[4]));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Illegal dateTime in token string", e);
		}
		return tokenInfo;
	}
	
	public boolean isExpired() {
		if (expiresDateTime != null) {
			long diffInMillis = new Date().getTime() - expiresDateTime.getTime();
			if (diffInMillis >= 0)
				return true;
		}
		return false;
	}

}

package jacJarSoft.util.Auth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AuthTokenInfo {
	public static final String AuthSchema = "JJSAUTH";
	private String user;
	private String tokenPart;
	private Date creationDateTime;
	private String uuid;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	public AuthTokenInfo(String user, String tokenPart) {
		this.user = user;
		this.tokenPart = tokenPart;
		creationDateTime = new Date();
		uuid = UUID.randomUUID().toString().toUpperCase();
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTokenString() {
		String token = uuid +
		        "|" + tokenPart +
		        "|" + user +
		        "|" + df.format(creationDateTime);
		return token;
	
	}
	@Override
	public String toString() {
		return AuthSchema + " " + getTokenString();
	}
	public static String getTokenFromAuthString(String authString) {
		if (authString.indexOf(AuthSchema) != 0)
			throw new IllegalArgumentException("Illegal authString");
	    return authString.substring(AuthSchema.length() +1);		
	}
	public static AuthTokenInfo fromTokenString(String token) {
		String[] parts = token.split("\\|"); //Split on |
		if (parts.length != 4)
			throw new IllegalArgumentException("Illegal token string");
		AuthTokenInfo tokenInfo = new AuthTokenInfo(parts[2], parts[1]);
		tokenInfo.setUuid(parts[0]);
		try {
			tokenInfo.setCreationDateTime(tokenInfo.df.parse(parts[3]));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Illegal dateTime in token string",e);
		}
		return tokenInfo;
	}
	

}

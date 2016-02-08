package jacJarSoft.util.Auth;

@SuppressWarnings("serial")
public class AuthException extends RuntimeException {
	public AuthException(String msg, Throwable t) {
		super(msg,t);
	}
	public AuthException(String msg) {
		this(msg,null);
	}

}

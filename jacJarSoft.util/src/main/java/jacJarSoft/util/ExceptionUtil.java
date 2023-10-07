package jacJarSoft.util;

public class ExceptionUtil {
	private ExceptionUtil() {/*only static methods*/}
	
	/**
	 * Get a RuntimeException. Either cast the param e or wrap it in one
	 * @param e
	 * @return
	 */
	public  static RuntimeException getRuntimeException(Exception e) {
		if (e instanceof RuntimeException)
			return (RuntimeException) e;
		else
			return new RuntimeException(e);
	}
	

}

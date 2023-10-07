package jacJarSoft.util;

public class StringUtils {

	private StringUtils() {}
	
	public static boolean hasValue(String s) {
		return !isEmpty(s);
	}

	public static  boolean isEmpty(String s) {
		if (s == null || s.equals(""))
			return true;
		return false;
	}
}

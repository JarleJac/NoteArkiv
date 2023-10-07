package jacJarSoft.noteArkiv.internal;

@SuppressWarnings("serial")
public class ValidationErrorException extends RuntimeException {
	public ValidationErrorException(String msg) {
		super(msg);
	}
}

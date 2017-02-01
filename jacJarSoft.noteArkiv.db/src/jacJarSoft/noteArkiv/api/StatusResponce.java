package jacJarSoft.noteArkiv.api;

public class StatusResponce {
	private int code;
	private String message;

	public StatusResponce(String msg) {
		this(0,msg);
	}
	public StatusResponce(int code, String msg) {
		this.code = code;
		message = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

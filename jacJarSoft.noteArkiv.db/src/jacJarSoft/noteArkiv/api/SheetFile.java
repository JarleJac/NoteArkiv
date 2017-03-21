package jacJarSoft.noteArkiv.api;

import jacJarSoft.noteArkiv.model.NoteFile;

public class SheetFile extends NoteFile {

	public SheetFile()
	{
		
	}
	public SheetFile(NoteFile other)
	{
		super(other);
	}
	private String mimeType;
	private boolean isHtml5Audio;
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
		calcIsHtml5Audio();
	}

	private void calcIsHtml5Audio() {
		isHtml5Audio = (mimeType.equals("audio/mpeg") ||
						mimeType.equals("audio/ogg") ||
						mimeType.equals("audio/wav"));
	}
	public boolean isHtml5Audio() {
		return isHtml5Audio;
	}
}

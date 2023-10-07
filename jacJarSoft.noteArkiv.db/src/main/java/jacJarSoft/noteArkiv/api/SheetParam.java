package jacJarSoft.noteArkiv.api;

import jacJarSoft.noteArkiv.model.Note;

public class SheetParam {

	private Note sheet;
	private boolean inCurrent;

	public Note getSheet() {
		return sheet;
	}

	public void setSheet(Note sheet) {
		this.sheet = sheet;
	}

	public boolean isInCurrent() {
		return inCurrent;
	}

	public void setInCurrent(boolean inCurrent) {
		this.inCurrent = inCurrent;
	}
}

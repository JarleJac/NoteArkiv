package jacJarSoft.noteArkiv.api;

import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.model.SheetList;

public class ListSheet {
	private SheetList list;
	private Note sheet;
	public SheetList getList() {
		return list;
	}
	public Note getSheet() {
		return sheet;
	}
	public ListSheet(SheetList list, Note sheet) {
		this.list = list;
		this.sheet = sheet;
	}
}

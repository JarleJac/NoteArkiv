package jacJarSoft.noteArkiv.db.util;

import org.junit.Test;
import static org.junit.Assert.*;

import jacJarSoft.noteArkiv.db.util.SheetFileUtil;
import jacJarSoft.noteArkiv.model.NoteFile;

public class SheetFileUtilTest {
	@Test
	public void testGetFileName() {
		NoteFile sheetFile = new NoteFile();
		sheetFile.setFileId(3);
		sheetFile.setName("testFile.pdf");
		String fileName = SheetFileUtil.getFileName(sheetFile);
		assertEquals("testFile_00003.pdf", fileName);

		sheetFile.setFileId(15);
		sheetFile.setName("my.file.name.txt");
		fileName = SheetFileUtil.getFileName(sheetFile);
		assertEquals("my.file.name_00015.txt", fileName);
	}

}

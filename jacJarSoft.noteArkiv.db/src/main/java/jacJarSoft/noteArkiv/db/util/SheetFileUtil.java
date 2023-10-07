package jacJarSoft.noteArkiv.db.util;

import org.apache.commons.io.FilenameUtils;

import jacJarSoft.noteArkiv.model.NoteFile;

public class SheetFileUtil {
	public static String getFileName(NoteFile sheetFile) {
		String baseName = FilenameUtils.getBaseName(sheetFile.getName());
		String extension = FilenameUtils.getExtension(sheetFile.getName());
		String fileIdPath = String.format("%1$05d", sheetFile.getFileId());
		return baseName + "_" + fileIdPath + "." + extension;
	}

}

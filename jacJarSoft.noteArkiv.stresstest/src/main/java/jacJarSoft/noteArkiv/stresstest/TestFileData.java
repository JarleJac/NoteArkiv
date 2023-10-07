package jacJarSoft.noteArkiv.stresstest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import jacJarSoft.util.FileUtil;

public class TestFileData {
	private String path;
	private String name;
	private byte[] data;
	public static TestFileData createFromPath(String filePath) {
		TestFileData fileData = new TestFileData();
		File f = new File(filePath);
		Path p = Paths.get(filePath);
		fileData.name = p.getFileName().toString();
		fileData.path = p.getParent().toString();
		try {
			fileData.data = FileUtil.readBytesFromFile(fileData.path, fileData.name, (int)f.length());
		} catch (IOException e) {
			throw new RuntimeException("Unable tio read file " + filePath,e);
		}
		return fileData;
	}
	public String getPath() {
		return path;
	}
	public String getName() {
		return name;
	}
	public byte[] getData() {
		return data;
	}
	public int getSize() {
		return data.length;
	}
}

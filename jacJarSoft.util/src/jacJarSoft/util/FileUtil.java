package jacJarSoft.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {
	private static Logger logger = Logger.getLogger(FileUtil.class.getName());
	/**
	 * Writes the bytes to a file. Creates any directories necessary.
	 * @param pathString The directory to create the file in
	 * @param fileName The name of the file
	 * @param bytes the File content
	 * @throws IOException If any error occur
	 */
	public static void writeBytesToFile(String pathString, String fileName, byte[] bytes) throws IOException {
		Path path = Paths.get(pathString);
		writeBytesToFile(path, fileName, bytes);		
	}
	/**
	 * Writes the bytes to a file. Creates any directories necessary.
	 * @param path full path of file
	 * @param bytes the File content
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void writeBytesToFile(Path path, String fileName, byte[] bytes) throws IOException {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Writing file " + fileName +" to " + path.toString());
		Files.createDirectories(path);
		Path filePath = Paths.get(path.toString(), fileName);
		try (FileOutputStream stream = new FileOutputStream(filePath.toFile())) {
		    stream.write(bytes);
		}
	}
	/**
	 * Read bytes from file. Report error if the file size is not as expected
	 * @param pathString The directory to read the file from
	 * @param fileName The name of the file
	 * @param size the expected size of the file
	 * @return the file content as a byte array
	 * @throws IOException if any errors occur or if the length of the file doesn't match the expected length
	 */
	public static byte[] readBytesFromFile(String pathString, String fileName, int size) throws IOException {
		Path path = Paths.get(pathString);
		return readBytesFromFile(path, fileName, size);
	}
	/**
	 * 
	 * @param path full path of file
	 * @param size the expected size of the file
	 * @return the file content as a byte array
	 * @throws IOException if any errors occur or if the length of the file doesn't match the expected length
	 */
	public static byte[] readBytesFromFile(Path path, String fileName, int size) throws IOException {
		Path filePath = Paths.get(path.toString(), fileName);
		byte[] bytes = new byte[size];
		try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filePath.toFile()),size+100)) {
			int bytesRead = 0;
			int lastRead = 0;
			while (bytesRead < size && lastRead >= 0) {
				lastRead = stream.read(bytes, bytesRead, size);
				if (lastRead > 0)
					bytesRead += lastRead;
			}
			if (stream.available() > 0)
				throw new IOException("Incorrect length of file " + path.toString() + ". The length is longer than the expected " + size + ".");
			if (bytesRead != size)
				throw new IOException("Incorrect length of file " + path.toString() + ". Expected " + size + ", found " + bytesRead);
		}
		return bytes;
	}
	public static void deleteFile(Path path, String fileName) throws IOException {
		Path filePath = Paths.get(path.toString(), fileName);
		Files.delete(filePath);
	}
}

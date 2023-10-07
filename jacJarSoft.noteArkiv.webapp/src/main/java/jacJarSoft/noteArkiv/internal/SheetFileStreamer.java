package jacJarSoft.noteArkiv.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import jacJarSoft.noteArkiv.model.NoteFile;

public class SheetFileStreamer  implements StreamingOutput {
	private static Logger logger = Logger.getLogger(SheetFileStreamer.class.getName());
	private File sheetFileDataAsFile;
	private NoteFile noteFile;

	public SheetFileStreamer(NoteFile noteFile, File sheetFileDataAsFile) {
		this.noteFile = noteFile;
		this.sheetFileDataAsFile = sheetFileDataAsFile;
		logger.fine("Streaming " + noteFile.getName() + ". Full");
	}

	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		try (	FileInputStream fileInputStream = new FileInputStream(sheetFileDataAsFile);
				FileChannel inputChannel = fileInputStream.getChannel();
				WritableByteChannel outputChannel = Channels.newChannel(output)) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
		}
//		catch (IOException e) {
//			if (e.getMessage().contains("An existing connection was forcibly closed by the remote host"))
//				logger.warning("Connection closed by remote host caught and ignored: " + noteFile.getName());
//			else
//				throw e;
//		}
	}

	public Object getLenth() {
		return noteFile.getFileSize();
	}

}

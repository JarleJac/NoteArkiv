package jacJarSoft.noteArkiv.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import jacJarSoft.noteArkiv.model.NoteFile;

public class RangeBasedSheetFileStreamer implements StreamingOutput {

	private static Logger logger = Logger.getLogger(RangeBasedSheetFileStreamer.class.getName());

    private int length;
	private int from;
	private int to;

	private RandomAccessFile raf;
	private final static int chunk_size = 1024 * 1024 * 3;
    final byte[] buf = new byte[chunk_size];

	private String responseRange;

	private File sheetFileDataAsFile;


    public RangeBasedSheetFileStreamer(int length, RandomAccessFile raf) {
        this.length = length;
        this.raf = raf;
    }

    public RangeBasedSheetFileStreamer(String range, NoteFile noteFile, File sheetFileDataAsFile) {
        this.sheetFileDataAsFile = sheetFileDataAsFile;
		long fileSize = noteFile.getFileSize();
		calcRange(range, fileSize);

		logger.fine("Streaming " + noteFile.getName() + ". Range: " + responseRange);
	}

	private void calcRange(String range, long fileSize) {
		String[] ranges = range.split("=")[1].split("-");
        from = Integer.parseInt(ranges[0]);
        to = chunk_size + from;
		if (to >= fileSize) {
            to = (int) (fileSize - 1);
        }
        if (ranges.length == 2) {
            to = Integer.parseInt(ranges[1]);
        }
        length = to - from + 1;
        responseRange = String.format("bytes %d-%d/%d", from, to, fileSize);
	}

	@Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		raf = new RandomAccessFile(sheetFileDataAsFile, "r");
		raf.seek(from);
        try {
            while( length != 0) {
                int read = raf.read(buf, 0, buf.length > length ? length : buf.length);
                outputStream.write(buf, 0, read);
                length -= read;
            }
        } finally {
            raf.close();
        }
    }

    public int getLenth() {
        return length;
    }

	public String getResponseRange() {
		return responseRange;
	}

}
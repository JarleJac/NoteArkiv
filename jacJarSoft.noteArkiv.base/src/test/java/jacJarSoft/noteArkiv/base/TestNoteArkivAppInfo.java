package jacJarSoft.noteArkiv.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import jacJarSoft.util.Version;

public class TestNoteArkivAppInfo {

	@Test
	public void testVersion() {
		Version version = NoteArkivAppInfo.getVersion();
		assertEquals(2, version.getMajor());
	}
}

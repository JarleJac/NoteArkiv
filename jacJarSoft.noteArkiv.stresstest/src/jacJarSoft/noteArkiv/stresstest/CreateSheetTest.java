package jacJarSoft.noteArkiv.stresstest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;

import jacJarSoft.util.FileUtil;

public class CreateSheetTest  extends AbstractTest{
	private static Logger logger = Logger.getLogger(CreateSheetTest.class.getName());
	
	public CreateSheetTest(String baseUrl, TestInstance testInstance) {
		super(baseUrl, testInstance);
	}

	@Override
	protected void runTheTests(CloseableHttpClient httpClient) {
		JSONObject createNoteParams = getNewNoteParams("Title from " + Thread.currentThread().getName(),"Jarle","Whatever");
		JSONObject createNoteResult = executeJsonHttpPost(httpClient, createJsonHttpPost("noteservice/note",createNoteParams));
		JSONObject sheet = createNoteResult.getJSONObject("sheet");
		int noteId = sheet.getInt("noteId");
		logger.fine("Added sheet " + noteId);
		
		TestFileData testFile1 = getTestController().getTestFile1();
		TestFileData testFile2 = getTestController().getTestFile2();
		
		JSONObject addFileResult = executeAddFile(httpClient, noteId, testFile1);
		addFileResult = executeAddFile(httpClient, noteId, testFile2);
		addFileResult.toString();
	}

	private JSONObject executeAddFile(CloseableHttpClient httpClient, int noteId, TestFileData testFile) {
		HttpEntity entity = MultipartEntityBuilder
			    .create()
			    .addTextBody("name", testFile.getName())
			    .addTextBody("description", Thread.currentThread().getName())
			    .addTextBody("size", String.valueOf(testFile.getSize()))
			    .addBinaryBody("file", testFile.getData())
			    .build();
		String urlPart = "noteservice/note/"+noteId+"/file";
		JSONObject addFileResult = executeJsonHttpPost(httpClient, createHttpPost(urlPart, entity));
		return addFileResult;
	}
	private JSONObject getNewNoteParams(String title, String composedBy, String arrangedBy) {
		JSONObject  sheet = new JSONObject(); 
		sheet.putOpt("title", title);
		sheet.putOpt("composedBy", composedBy);
		sheet.putOpt("arrangedBy", arrangedBy);
		JSONObject  sheetParam = new JSONObject(); 
		sheetParam.put("sheet", sheet);
		return sheetParam;
	}

}

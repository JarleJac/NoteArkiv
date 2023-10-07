package jacJarSoft.noteArkiv.stresstest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractTest {
	private static Logger logger = Logger.getLogger(AbstractTest.class.getName());
	
	private String baseUrl;
	private String authToken;

	private TestInstance testInstance;

	private TestController testController;

	public AbstractTest(String baseUrl, TestInstance testInstance) {
		this.baseUrl = baseUrl;
		this.testInstance = testInstance;
		setTestController(testInstance.getTestController());
		
	}
	public void runTest() {
		logger.fine("Test started: " + Thread.currentThread().getName());
		try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			
			logon(httpClient);
			logger.fine("Logon OK, received authToken: " + authToken);

			runTheTests(httpClient);
			testInstance.setOk(true);
		} catch (Exception e) {
			testInstance.setOk(false);
			logger.log(Level.SEVERE,"Error running Test",e);
		}
		logger.fine("Thread ended: " + Thread.currentThread().getName());
		
	}
	private void logon(CloseableHttpClient httpClient) {
		JSONObject logonParams = new JSONObject();
		logonParams.put("user", "sysadmin");
		logonParams.put("password", "Binders");
		
		JSONObject logonResult = executeJsonHttpPost(httpClient, createJsonHttpPost("appservice/logon",logonParams));
		authToken = logonResult.getString("authToken");
		
	}
	protected HttpPost createJsonHttpPost(String urlPart, JSONObject json) {
		HttpEntity entity = null;
		try {
			entity = new StringEntity(json.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to create StringEntity from JSON: " + json.toString(),e);
		}
		HttpPost httpPost = createHttpPost(urlPart, entity);
		httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		return httpPost;
	}
	protected HttpPost createHttpPost(String urlPart, HttpEntity entity) {
		HttpPost httpPost = new HttpPost(baseUrl + urlPart);
		httpPost.setEntity(entity);
		if (authToken != null)
			httpPost.addHeader("Authorization", authToken);
		return httpPost;
	}
	protected JSONObject executeJsonHttpPost(CloseableHttpClient httpClient, HttpPost httpPost) {
		JSONObject jsonResult;
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			logger.fine("status: " + statusLine.toString());
			if (statusLine.getStatusCode() != 200)
				throw new RuntimeException("Error result from Http POST: " + statusLine.toString());

			InputStream contentStream = httpResponse.getEntity().getContent();
			String result = new BufferedReader(new InputStreamReader(contentStream))
			  .lines().collect(Collectors.joining("\n"));
			logger.fine("data: " + result);
			jsonResult = new JSONObject(result);
		} catch (UnsupportedOperationException | JSONException | IOException e) {
			throw new RuntimeException("Exception occured during Http POST: " + e.getMessage(),e);
		}
		return jsonResult;
	}
	protected abstract void runTheTests(CloseableHttpClient httpClient);
	protected TestController getTestController() {
		return testController;
	}
	private void setTestController(TestController testController) {
		this.testController = testController;
	}
}

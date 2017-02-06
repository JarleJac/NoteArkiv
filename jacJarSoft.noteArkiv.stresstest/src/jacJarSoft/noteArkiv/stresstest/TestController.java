package jacJarSoft.noteArkiv.stresstest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.Logger;

public class TestController {
	private static Logger logger = Logger.getLogger(TestController.class.getName());
	
	private Properties props;
	private String baseUrl;
	private int numThreads;
	private int numCreateSheet;
	private Queue<TestInstance> testToRun;
	private List<TestInstance> testThatAreRun = new ArrayList<>();

	private String computerName;

	private TestFileData testFile1;
	private TestFileData testFile2;

	public TestController(Properties props) {
		this.props = props;
		computerName = System.getenv("COMPUTERNAME");
	}

	public void runTest() {
		init();

		Thread threads[] = new Thread[numThreads];
		for (int i=0; i< numThreads; i++) {
			TestRunnerThread runner = new TestRunnerThread(this);
			runner.setName("TestRunnerThread on " + computerName + " no " + (i+1));
			threads[i] = runner;
			runner.start();
		}
		for (int i=0; i< numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				logger.warning("Interrupted: " + e.getMessage());
			}
		}
		report();
	}

	private void report() {
		int countOk = 0;
		int countTests = 0;
		int countError = 0;
		for (TestInstance instance :testThatAreRun) {
			countTests++;
			if (instance.isOk())
				countOk++;
			else
				countError++;
		}
		logger.info("Ran " + countTests + " tests. " + countOk + " was ok and " + countError + " failed.");
	}

	private void init() {
		setBaseUrl(props.getProperty("jacJarSoft.stresstest.baseUrl"));
		numThreads = Integer.parseInt(props.getProperty("jacJarSoft.stresstest.numThreads"));
		numCreateSheet = Integer.parseInt(props.getProperty("jacJarSoft.stresstest.numCreateSheet"));
		String file1 = props.getProperty("jacJarSoft.stresstest.file1");
		setTestFile1(TestFileData.createFromPath(file1));
		String file2 = props.getProperty("jacJarSoft.stresstest.file2");
		setTestFile2(TestFileData.createFromPath(file2));
		
		testToRun = new LinkedList<>();
		for (int i=0; i<numCreateSheet;i++) {
			TestInstance instance = new TestInstance(TestInstance.TestType.CREATE_SHEET, this);
			testToRun.add(instance);
		}
	}

	protected String getBaseUrl() {
		return baseUrl;
	}

	private void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public synchronized AbstractTest getNextTest() {
		TestInstance instance = testToRun.poll();
		if (instance == null)
			return null;
		testThatAreRun.add(instance);
		return instance.createTest();
	}

	public TestFileData getTestFile1() {
		return testFile1;
	}

	private void setTestFile1(TestFileData testFile1) {
		this.testFile1 = testFile1;
	}

	public TestFileData getTestFile2() {
		return testFile2;
	}

	private void setTestFile2(TestFileData testFile2) {
		this.testFile2 = testFile2;
	}
	
}

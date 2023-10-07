package jacJarSoft.noteArkiv.stresstest;

import java.util.logging.Logger;

public class TestRunnerThread extends Thread {
	private static Logger logger = Logger.getLogger(TestRunnerThread.class.getName());
	private TestController testController;
	

	public TestRunnerThread(TestController testController) {
		this.testController = testController;
	}
	@Override
	public void run() {
		int counter = 0;
		logger.info("Thread started: " + this.getName());
		AbstractTest nextTest;
		while ((nextTest = testController.getNextTest()) != null) {
			counter++;
			nextTest.runTest();
		}
		logger.info("Thread ended: " + this.getName() + " handeled " + counter + " tests");
	}
}

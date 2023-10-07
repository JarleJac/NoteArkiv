package jacJarSoft.noteArkiv.stresstest;

class TestInstance {
	public enum TestType {CREATE_SHEET};
	private boolean ok;
	private boolean testCreated;
	private TestType type;
	private TestController testController;
	public TestInstance(TestType type, TestController testController) {
		this.testController = testController;
		this.setType(type);
	}
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public TestType getType() {
		return type;
	}
	private void setType(TestType type) {
		this.type = type;
	}
	AbstractTest createTest() {
		if (testCreated)
			throw new IllegalStateException("Test already created for this Instance");
		switch (type) {
		case CREATE_SHEET:
			return new CreateSheetTest(testController.getBaseUrl(), this);
		}
		throw new IllegalStateException("Unknown test type: " + type);
	}
	public TestController getTestController() {
		return testController;
	}
}
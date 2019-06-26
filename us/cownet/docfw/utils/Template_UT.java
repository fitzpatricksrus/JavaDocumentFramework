/*
 * $Id: Template_UT.java /main/2 1998/07/16 16:58:56 jwebber $
 * $Source: /project/wamali/code/com/mot/wamali/util/Template_UT.java $
 *
 * MOTOROLA CONFIDENTIAL PROPRIETARY
 *
 * Copyright 1998 Motorola Australia Pty. Ltd.
 * All Rights Reserved
 *
 * This is unpublished proprietary source code
 * of Motorola Australia Pty. Ltd.
 *
 * The copyright notice does not evidence any actual
 * or intended publication of such source code.
 */
package us.cownet.docfw.utils;

import java.text.DecimalFormat;

/**
 * Template class for automated unit tests.  This provides some common
 * methods and a common structure for executing unit tests.
 *
 * @author jwebber
 * @version $Revision: /main/2 $
 */
public abstract class Template_UT {

	/** Used to format test sequence numbers for reporting. */
	protected static DecimalFormat form = new DecimalFormat("#000");


	// ---- Constructor ----

	/**
	 * Build an instance of this class.  You will almost certainly want
	 * to override this.
	 */
	public Template_UT() {
		super();
	}


	// ---- Individual "Normal" Tests ----

	// Put your test methods in here.


	// ---- Helper Methods ----

	/**
	 * Report a test result in the correct format.
	 *
	 * @param o object under test
	 * @param sequence test case sequence ID
	 * @param passed whether the test passed or failed
	 */
	protected static final void reportTestCase(Object o,
			int sequence, boolean passed) {
		System.out.println(o.getClass().getName()
				+ "_"
				+ form.format(sequence)
				+ ": "
				+ (passed ? "passed" : "FAILED"));
	}

	/**
	 * Report a test case as having not been run.
	 *
	 * @param o object under test
	 * @param sequence test case sequence ID
	 */
	protected static final void reportTestCase(Object o, int sequence) {
		System.out.println(o.getClass().getName()
				+ "_"
				+ form.format(sequence)
				+ ": "
				+ "NOT RUN");
	}


	// ---- Automated Test Execution ----

	/**
	 * Report a summary of the test cases run.
	 */
	protected static final void reportSummary(Statistics s) {
		System.out.println();
		System.out.println("There were " + s.successes + " successful test"
				+ ((s.successes == 1) ? "" : "s") + ", "
				+ s.failures + " failed test"
				+ ((s.failures == 1) ? "" : "s") + " and "
				+ s.notRuns + " test"
				+ ((s.notRuns == 1) ? "" : "s") + " not run.");
	}

	/**
	 * Main method.  All unit test classes should override this; the code
	 * here is intended as an example.
	 */
	static public void main(String[] args) {
/*
        Object testObject = new Object();
        Template_UT me = new Template_UT();
        Template_UT.Statistics s;

        me.runTests(c, 1, null, null, s);
        reportSummary()

        // Delay termination so Visual Cafe's console doesn't disappear.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
*/
	}

	/**
	 * Call this to abort a test on failure.
	 */
	public void fail() throws TestFailureException {
		throw new TestFailureException();
	}

	/**
	 * Call this to abort a test on failure, with a message.
	 */
	public void fail(String errorMessage) throws TestFailureException {
		throw new TestFailureException(errorMessage);
	}

	/**
	 * Obtain the number of tests which can be executed with this class.
	 */
	public abstract int getNumTests();

	/**
	 * Execute a single test case.  This uses <code>testNum</code> to
	 * work out which test case to execute.
	 *
	 * @param o the object to test.
	 * @param testNum absolute test number (range 0..getNumTests()).
	 * @param rest additional test-specific information.
	 * @throws TestFailureException if a test fails.
	 */
	public abstract void testCase(Object o, int testNum, Object rest)
			throws TestFailureException;


	// ---- Main Function ----

	/**
	 * Run all test cases enclosed in this class, in sequence.  It is
	 * probably not necessary to override this.
	 *
	 * @param o the object to test.
	 * @param sequence sequence number of the first test to perform.
	 * @param skipTests array indicating tests to skip.  If null,
	 * all tests are run.
	 * @param rest addition test-specific information.
	 * @param s statistics which are updated from this run.
	 * @return the sequence number of the next test to run.
	 * @see #testCase
	 */
	public int runTests(Object o, int sequence, boolean[] skipTests,
			Object rest, Statistics s) {

		boolean testPassed = false;

		for (int i = 0; i < getNumTests(); i++) {
			if (skipTests == null
					|| i >= skipTests.length
					|| !skipTests[i]) {
				testPassed = true;
				try {
					testCase(o, i, rest);
					s.successes++;
				} catch (Throwable e) {
					testPassed = false;
					System.err.println("----------------------------------------------------------------------------");
					System.err.println(o.getClass().getName()
							+ "_"
							+ form.format(sequence)
							+ ": "
							+ "EXCEPTION THROWN:");
					e.printStackTrace();
					System.err.println();
					s.failures++;
				} finally {
					reportTestCase(o, sequence, testPassed);
				}
			} else {
				reportTestCase(o, sequence);
				s.notRuns++;
			}
			sequence += 1;
		}

		return sequence;
	}


	// ---- Statistics class ----

	/**
	 * This class contains statistics about a test run.
	 */
	public static class Statistics {

		/** Number of successful tests */
		public int successes;

		/** Number of failure tests */
		public int failures;

		/** Number of tests not run */
		public int notRuns;
	}
}

package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import event.Signal;
import file.SolutionFile;

public class Run {

	final static String dateiSimulatorParam = "datei";
	final static String fullTimingSimulatorParam = "fullTiming";

	/**
	 * 
	 * @param args
	 *            args[0]; datei ODER fullTimimg, args[1]: Testfall, args[2]:
	 *            Ausgabe
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	static public void main(String[] args) throws FileNotFoundException,
			URISyntaxException {
		// String circuitFileName = "exampleFiles/beispiel1o.cir";
		// String eventFileName = "exampleFiles/beispiel1o.events";
		// File circuitFile = new File(ClassLoader.getSystemResource(
		// circuitFileName).toURI());
		// File eventFile = new
		// File(ClassLoader.getSystemResource(eventFileName)
		// .toURI());
		//
		// DateiSimulator t = new DateiSimulator(circuitFile, eventFile);
		//
		// t.simulate();

		if (args.length < 2) {
			logErrorMessage(wrongArgMessage);
			return;
		}
			try {
			int testFall = Integer.parseInt(args[1]);
			boolean logToSystemOut = false;
			if (args.length > 2 && "logOutput".equals(args[2])) {
				logToSystemOut = true;
			}
			if (fullTimingSimulatorParam.equals(args[0])) {
				FullTimingSimulator.run(testFall, logToSystemOut);
			} else if (dateiSimulatorParam.equals(args[0])) {
DateiSimulator.run();
			} else {
				logErrorMessage(wrongSimulatorMessage);
			}

		} catch (java.lang.NumberFormatException e) {
			logErrorMessage(wrongTestCaseMessage);
		}	
		}

	private static void logErrorMessage(String message) {
		System.out.println(message);

	}

	final static String wrongArgMessage = "Sie müssen mindestens 2 Argumente angeben: 1. '"
			+ dateiSimulatorParam
			+ "' oder '"
			+ fullTimingSimulatorParam
			+ "'; 2. Testfallnummer";

	final static String wrongSimulatorMessage = "Erstes Argument entweder '"
			+ dateiSimulatorParam + "' oder '" + fullTimingSimulatorParam
			+ "' sein";

	final static String wrongTestCaseMessage = "Zweites Argument muss eine Zahl zwischen 1 und 3 sein";
}
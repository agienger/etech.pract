package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Circuit;
import main.EventProvider;
import event.Event;
import event.EventQueue;
import event.Signal;
import event.SignalKind;
import file.CircuitState;
import file.Solution;
import file.SolutionFile;

public class DateiSimulator {

	private static final boolean OUTPUT = false;
	private static final boolean VERIFY = true;

	// EventQueue für diesen Simulator, wird im Konstruktor initialisiert
	private EventQueue queue;
	public static ArrayList<ArrayList<String>> solutionList = new ArrayList<ArrayList<String>>();

	public DateiSimulator(File circuitFile, File eventFile) {

		queue = new EventQueue();
		Event.setEventQueue(queue);

		// Schaltung aufbauen
		buildCircuit(circuitFile);
		// Ruhezustand berechnen
		findSteadyState();
		// EventQueue mit Eingabe-Events fuellen
		setInputEvents(eventFile);

	}

	private void findSteadyState() {
		for (Signal signal : Signal.getSignalList()) {
			if (signal.getSignalKind().equals(SignalKind.INPUT)) {
				signal.setValue(false);
				if (signal.getName().equals("nichtreset")) {
					signal.setValue(true);
				}
			}
		}
		String firstOutputLine = "Zeit \t";
		for (Signal signal : Signal.getSignalList()) {
			SignalKind kind = Signal.getSignalFromList(signal.getName())
					.getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				firstOutputLine += signal.getName() + "\t";
			}
		}
		if (OUTPUT) {
			System.out.println(firstOutputLine);
		}
		DateiSimulator.logCurrentState(0);

	}

	private void setInputEvents(File eventFile) {
		new EventProvider(eventFile);
	}

	private void buildCircuit(File file) {
		new Circuit(file);
	}

	// }

	/**
	 * Diese Methode fuehrt die eigentliche Simulation durch. Dazu wird
	 * geprueft, ob in der EventQueue noch weitere Events vorhanden sind. Ist
	 * dies der Fall, dann wird das nächste anstehende Event behandelt. Dazu
	 * muss das Event die Methode propagate() zur Verfügung stellen, die dann
	 * das betroffene Signal informiert.
	 */
	public void simulate() {
		while (queue.hasMore()) {
			Event e = queue.getFirst();
			e.propagate();
		}
	}

	public static void logCurrentState(int time) {
		ArrayList<String> states = new ArrayList<String>();
		for (Signal sig : Signal.getSignalList()) {
			SignalKind kind = Signal.getSignalFromList(sig.getName())
					.getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				states.add(Integer.toString(sig.getValue() == true ? 1 : 0));
			}
		}
		Solution.addSolution(new CircuitState(time, states));
	}

	static public void main(String[] args) throws FileNotFoundException,
			URISyntaxException {
		
		

		List<String> examples = Arrays.asList( "beispiel1o", "beispiel1o2", "beispiel-latch",
				"beispiel-latch2", "beispiel-flipflop", "beispiel-flipflop2",
				"_blume", "_blume2" );
		if (args[0].toLowerCase().equals("all")) {
			for (String testFall : examples) {
				run(testFall);
			}
		}
		else if(examples.contains(args[0])) {
			run(args[0]);
		}
		else {
			System.out.println("Eingabeparameter muss entweder 'ALL' oder einer der folgenden Beispiele sein:");
			System.out.println(examples);
		}
		
	}

	private static void run(String testFall) throws URISyntaxException {
		String circuitFileName = "circuits/" + testFall + ".cir";
		int lastIndex = testFall.lastIndexOf("2");
		String eventTestFall = testFall;
		if (testFall.endsWith("2")) {
			eventTestFall = testFall.substring(0, lastIndex);
		}
		String eventFileName = "events/" + eventTestFall + ".events";
		File solutionFile = null;
		ArrayList<String[]> solRows = null;
		Solution.clear();
		File circuitFile = new File(ClassLoader.getSystemResource(
				circuitFileName).toURI());
		File eventFile = new File(ClassLoader.getSystemResource(
				eventFileName).toURI());

		DateiSimulator t = new DateiSimulator(circuitFile, eventFile);

		t.simulate();

		int counter = 0;
		if (VERIFY) {
			solutionFile = new File(ClassLoader.getSystemResource(
					"solutions/" + testFall + ".erg").toURI());
			solRows = SolutionFile.getSolutionRowsFromFile(solutionFile);
			org.junit.Assert.assertEquals(
					"Fehler: unterschiedliche Listgrößen in Testfall "
							+ testFall, solRows.size(),
					Solution.solutions.size());
		}
		for (CircuitState solution : Solution.solutions) {
			String solRow = Integer.toString(solution.getTime());
			org.junit.Assert.assertEquals(solRow, solRows.get(counter)[0]);
			for (int i = 0; i < solution.getStates().size(); i++) {
				solRow += "\t" + solution.getStates().get(i);
				if (VERIFY) {
					org.junit.Assert.assertEquals("Fehler in "
							+ (counter + 1) + ".ten Zeile und " + (i + 1)
							+ ".ten Spalte.", solution.getStates().get(i),
							solRows.get(counter)[i + 1]);
				}
			}
			counter++;
			if (OUTPUT) {
				System.out.println(solRow);
			}
		}
		if (VERIFY) {
			System.out.println("Test für Schaltung " + testFall
					+ " war erfolgreich :-)");
		}
	}
}
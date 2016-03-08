package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import verify.Result;
import verify.SolutionFile;
import circuit.Circuit;
import circuit.CircuitState;
import circuit.Event;
import circuit.EventProvider;
import circuit.EventQueue;
import circuit.Signal;
import circuit.SignalKind;

/**
 * @author Lion Gienger
 *
 */
public class DateiSimulator {

	/**
	 * Die {@link circuit.EventQueue} für diesen Simulator. Jedes erzeugte Event
	 * wird in die {@link circuit.EventQueue} an die entsprechende Stelle
	 * geschrieben. Nach Abarbeitung des Events wird dieses aus der Queue
	 * gelöscht.
	 */
	private EventQueue queue;
	/**
	 * Liste aller Signale, die in {@link circuit.Circuit} erzeugt werden. Zum
	 * einen wird diese benötigt, um alle Eingabe Signale in der
	 * {@link #findSteadyState()} Methode zu initialisieren, zum anderen
	 * verwenden wir die Liste für die Ausgabe der Ergebnisse (alle Input und
	 * Output Signale) und zuer Verifikation dieser gegen die erwarteten
	 * Ergbnisse, die wir aus der Ergebnisdatei erahlten.
	 */
	private ArrayList<Signal> signalList;

	private Circuit circuit;
	private File circuitFile;
	private File eventFile;
	private static File solutionFile;
	private static boolean output = true;

	/**
	 * Im Konstruktor werden zunächst für den Testfall {@code testFall} die
	 * Files geladen und dann die beiden Member {@link #queue} und
	 * {@link signal List} intialisiert. Danach werden Instanzen von
	 * {@link circuit.Circuit} erzeugt. also die Schaltung aus dem circuitFile
	 * aufgebaut, der stabile Ausgangszustand berechnet in der Methode
	 * {@link #findSteadyState()}. Schließlich werden die initialen Input Events
	 * über eine Instanz von {@link circuit.EventProvider} erzeugt.
	 * 
	 * @param testFall
	 *            Der zu simulierende Testfall
	 * 
	 */

	public DateiSimulator(String testFall) throws URISyntaxException {

		Result.clear();
		String circuitFileName = "circuits/" + testFall + ".cir";
		int lastIndex = testFall.lastIndexOf("2");
		String eventTestFall = testFall;
		if (testFall.endsWith("2")) {
			eventTestFall = testFall.substring(0, lastIndex);
		}
		String eventFileName = "events/" + eventTestFall + ".events";
		circuitFile = new File(getClass().getClassLoader()
				.getResource(circuitFileName).toURI());
		eventFile = new File(getClass().getClassLoader()
				.getResource(eventFileName).toURI());
		if (output == false) {
			solutionFile = new File(getClass().getClassLoader()
					.getResource("solutions/" + testFall + ".erg").toURI());
		}
		queue = new EventQueue();
		Event.setEventQueue(queue);
		circuit = new Circuit(circuitFile);
		signalList = circuit.getSignalList();
		Signal.setSignalList(signalList);
		findSteadyState();
		new EventProvider(eventFile, signalList);
	}

	/**
	 * Setzt zum Zeitpunkt 0 alle Input Signale auf {@code false}, außer
	 * {@code nichtreset}, das auf {@code true} gesetzt wird. Der erste Status
	 * der Signale wird auch ausgegeben und abgespeichert (für die
	 * Verifikation).
	 */
	private void findSteadyState() {
		for (Signal signal : signalList) {
			if (signal.getSignalKind().equals(SignalKind.INPUT)) {
				signal.setValue(false);
				if (signal.getName().equals("nichtreset")) {
					signal.setValue(true);
				}
			}
		}
		outputInitialState();
		Result.storeCurrentState(0, signalList);

	}

	/**
	 * Gibt den initialen Status der In- und Output Signalen auf System.out aus,
	 * falls {@code true} ist.
	 */
	private void outputInitialState() {
		if (output) {
			String firstOutputLine = "Zeit \t";
			for (Signal signal : signalList) {
				SignalKind kind = Signal.getSignalFromList(signalList,
						signal.getName()).getSignalKind();
				if (kind.equals(SignalKind.INPUT)
						|| kind.equals(SignalKind.OUTPUT)) {
					firstOutputLine += signal.getName() + "\t";
				}
			}
			System.out.println(firstOutputLine);
		}
	}

	/**
	 * Diese Methode startet die Simulation durch. Solange in der {@link #queue}
	 * noch Events stehen wird das erste Element der List genommen und daran die
	 * Methode {@link circuit.Event#propagate()} ausgeführt.
	 * 
	 */
	public void simulate() {
		while (queue.hasMore()) {
			Event e = queue.getFirst();
			e.propagate();
		}
	}

	private static void setOutput(boolean out) {
		output = out;
	}

	/**
	 * Die {@code main} Methode startet die Simulation der Schaltung(en),
	 * abhängig vom Einagbewert.
	 * 
	 * @param args
	 *            Es wird ein Eingabewert {@code args[0]} ausgewertet, Wenn der
	 *            auf {@code all} gesetzt ist, werden alle Beispiele aus der
	 *            Liste {@code examples} ausgeführt. Ansonsten wird ein Beispiel
	 *            aus der Liste {@code examples} erwartet und berechnet.
	 * 
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	static public void main(String[] args) throws FileNotFoundException,
			URISyntaxException {

		List<String> examples = Arrays.asList("beispiel1o", "beispiel1o2",
				"beispiel-latch", "beispiel-latch2", "beispiel-flipflop",
				"beispiel-flipflop2", "_blume", "_blume2");
		if (args.length == 2 && args[1].toLowerCase().equals("verify")) {
			setOutput(false);
		}
		if (args.length == 0 || args[0].toLowerCase().equals("all")) {
			for (String testFall : examples) {
				run(testFall);
			}
		} else {
			run(args[0]);
		}

	}

	private static void run(String beispielName ) throws URISyntaxException {

		DateiSimulator t = new DateiSimulator(beispielName);
		t.simulate();
		ArrayList<String[]> solRows = null;
		int counter = 0;
		if (output == false) {
			solRows = SolutionFile.getSolutionRowsFromFile(solutionFile);
			org.junit.Assert.assertEquals(
					"Fehler: unterschiedliche Listgrößen in Testfall "
							+ beispielName, solRows.size(),
					Result.results.size());
		}
		for (CircuitState solution : Result.results) {
			String solRow = Integer.toString(solution.getTime());
			if (output == false) {
				org.junit.Assert.assertEquals(solRow, solRows.get(counter)[0]);
			}
			for (int i = 0; i < solution.getStates().size(); i++) {
				solRow += "\t" + solution.getStates().get(i);
				if (output == false) {
					org.junit.Assert.assertEquals("Fehler in " + (counter + 1)
							+ ".ten Zeile und " + (i + 1) + ".ten Spalte.",
							solution.getStates().get(i),
							solRows.get(counter)[i + 1]);
				}
			}
			counter++;
			if (output == true) {
				System.out.println(solRow);
			}
		}
		if (output == false) {
			System.out.println("Test für Schaltung " + beispielName
					+ " war erfolgreich :-)");
		}
	}
}

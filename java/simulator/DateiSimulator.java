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
	 * Die {@link circuit.EventQueue} für diesen Simulator. 
	 * Jedes erzeugte Event wird in die {@link circuit.EventQueue} an die entsprechende Stelle 
	 * geschrieben.
	 * Nach Abarbeitung des Events wird dieses aus der Queue gelöscht. 
	 */
	private static EventQueue queue;
	/**
	 * Statische Liste aller Signale, die in {@link circuit.Circuit} erzeugt werden.
	 * Zum einen wird diese benötigt, um alle Eingabe Signale in der {@link findSteadyState()} Methode 
	 * zu initialisieren, zum anderen verwenden wir die Liste für die Ausgabe der Ergebnisse
	 * (alle Input und Output Signale) und zuer Verifikation dieser gegen die erwarteten Ergbnisse, die wir 
	 * aus der Ergebnisdatei erahlten. 
	 */
	private static ArrayList<Signal> signalList;

	/**
	 * Im Konstruktor werden die beiden statischen Variablen {@link #queue} und {@link signalList} intialisiert.
	 * Danach werden in den Methoden {@link buildCircuit(File)} die Schaltung aus dem circuitFile aufgebaut, 
	 * der stabile Ausgangszustand berechnet in der Methode {@link findSteadyState()} 
	 * und schließlich werden die initialen Input Events in der Methode {@link setInputEvents(File)} gesetzt.
	 * 
	 * @param circuitFile Die Datei, in welcher die Schaltkreis-Defintion gepflegt ist (.cir Datei)
	 * @param eventFile Die Datei, in welcher die Events gepgflegt sind (.event Datei)
	 * 
	 */
	public DateiSimulator(File circuitFile, File eventFile) {

		queue = new EventQueue();
		Event.setEventQueue(queue);
		signalList = new ArrayList<Signal>();
		Circuit.setSignalList(signalList);

		buildCircuit(circuitFile);
		findSteadyState();
		setInputEvents(eventFile);

	}

	/**
	 * Setzt zum Zeitpunkt 0 alle Input Signale auf {@code false}, außer
	 * {@code nichtreset}, das auf {@code true} gesetzt wird. Der erste Status der Signale wird auch ausgegeben 
	 * und abgespeichert (für die Verifikation).
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
		storeCurrentState(0);

	}

	/**
	 * Gibt den initialen Status der In- und Output Signalen auf System.out aus, falls 
	 * {@link output()} {@code true} zurück gibt.
	 */
	private void outputInitialState() {
		if (output()) {
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
	 * Erzeugt eine Instanz von {@link circuit.EventProvider}
	 * @param eventFile Datei, in der die initialen Input Events gespeichert sind
	 */
	private void setInputEvents(File eventFile) {
		new EventProvider(eventFile);
	}

	/**
	 * Erzeugt eine Instanz von {@link circuit.Circuit}
	 * @param circuitFile Datei, in welcher die Schaltung definiert ist
	 */
	private void buildCircuit(File circuitFile) {
		new Circuit(circuitFile);
	}

	/**
	 * Diese Methode startet die Simulation durch. Solange in der {@link #queue} 
	 * noch Events stehen wird das erste Element der List genommen und daran die Methode 
	 * {@link circuit.Event.proagate()} ausgeführt.
	 * 
	 */
	public void simulate() {
		while (queue.hasMore()) {
			Event e = queue.getFirst();
			e.propagate();
		}
	}

	/**
	 * Speichert den Status der Schaltung zum Zeitpunkt {@link time} in die statische Liste {@link verify.Result#results}, siehe {@see verify.Result}
	 * @param time Zeitpunkt der Schaltung
	 */
	public static void storeCurrentState(int time) {
		ArrayList<String> states = new ArrayList<String>();
		for (Signal sig : signalList) {
			SignalKind kind = Signal.getSignalFromList(signalList,
					sig.getName()).getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				states.add(Integer.toString(sig.getValue() == true ? 1 : 0));
			}
		}
		Result.addState(new CircuitState(time, states));
	}

	/**
	 * @return Wenn die System property {@code OUTPUT} auf {@code true} gesetzt ist, wird {@code true}
	 * zurückgegeben, ansonsten {@code false}.
	 */
	private static boolean output() {
		String value = System.getProperty("OUTPUT");
		if (value!= null && value.toLowerCase().equals("true")) {
			return true;
		}
		return false;
	}

	/**
	 * @return Wenn die System property {@code VERIFY} auf {@code true} gesetzt ist, wird {@code true}
	 * zurückgegeben, ansonsten {@code false}.
	 */
	private static boolean verify() {
		String value = System.getProperty("VERIFY");
		if (value!= null && value.toLowerCase().equals("false")) {
			return false;
		}
		return true;
	}

	/**
	 * Die {@code main} Methode startet die Simulation der Schaltung(en), abhängig vom Einagbewert.
	 * @param args Es wird ein Eingabewert {@code args[0]} ausgewertet, Wenn der auf {@code all} 
	 * gesetzt ist, werden alle Beispiele aus der Liste {@code examples} ausgeführt. Ansonsten wir ein 
	 * Beispiel aus der Liste erwartet und berechnet.
	 * 
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	static public void main(String[] args) throws FileNotFoundException,
			URISyntaxException {

		List<String> examples = Arrays.asList("beispiel1o", "beispiel1o2",
				"beispiel-latch", "beispiel-latch2", "beispiel-flipflop",
				"beispiel-flipflop2", "_blume", "_blume2");
		if (args[0].toLowerCase().equals("all")) {
			for (String testFall : examples) {
				run(testFall);
			}
		} else if (examples.contains(args[0])) {
			run(args[0]);
		} else {
			System.out
					.println("Eingabeparameter muss entweder 'ALL' oder einer der folgenden Beispiele sein:");
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
		Result.clear();
		File circuitFile = new File(ClassLoader.getSystemResource(
				circuitFileName).toURI());
		File eventFile = new File(ClassLoader.getSystemResource(eventFileName)
				.toURI());

		DateiSimulator t = new DateiSimulator(circuitFile, eventFile);

		t.simulate();

		int counter = 0;
		if (verify()) {
			solutionFile = new File(ClassLoader.getSystemResource(
					"solutions/" + testFall + ".erg").toURI());
			solRows = SolutionFile.getSolutionRowsFromFile(solutionFile);
			org.junit.Assert.assertEquals(
					"Fehler: unterschiedliche Listgrößen in Testfall "
							+ testFall, solRows.size(),
					Result.results.size());
		}
		for (CircuitState solution : Result.results) {
			String solRow = Integer.toString(solution.getTime());
			org.junit.Assert.assertEquals(solRow, solRows.get(counter)[0]);
			for (int i = 0; i < solution.getStates().size(); i++) {
				solRow += "\t" + solution.getStates().get(i);
				if (verify()) {
					org.junit.Assert.assertEquals("Fehler in " + (counter + 1)
							+ ".ten Zeile und " + (i + 1) + ".ten Spalte.",
							solution.getStates().get(i),
							solRows.get(counter)[i + 1]);
				}
			}
			counter++;
			if (output()) {
				System.out.println(solRow);
			}
		}
		if (verify()) {
			System.out.println("Test für Schaltung " + testFall
					+ " war erfolgreich :-)");
		}
	}
}
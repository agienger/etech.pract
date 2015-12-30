package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import main.Circuit;
import main.EventProvider;
import event.Event;
import event.EventQueue;
import event.Signal;
import event.SignalKind;
import event.SignalList;
import gatter.Gatter;

public class DateiSimulator {

	// EventQueue fuer diesen Simulator, wird im Konstruktor initialisiert
	private EventQueue queue;

	public static HashMap<String, Signal> signals;
	private HashMap<String, Gatter> gates;

	public static HashMap<String, Signal> inputs;

	public static HashMap<String, Signal> outputs;

	/**
	 * Konstruktor, der die zu simulierende Schaltung aufbaut, den Ruhezustand
	 * ermittelt und die Eingabe-Events erzeugt. Simuliert wird je nach Argument
	 * eine der drei vorgegebenen Schaltungen 1 = Einfacher Multiplexer 4 zu 1 2
	 * = Einfacher synchroner, ruecksetzbarer Zaehler mit 4 Bit 3 = Komplexe
	 * Schaltung mit einem Zaehler vielen Latches und einigen Multiplexern
	 */
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
		for (Signal signal : Circuit.getSignalList()) {
			if (signal.getSignalKind().equals(SignalKind.INPUT)) {
				signal.setValue(false);
			}
		}
		String firstOutputLine = "Zeit \t";
		for (Signal signal : Circuit.getSignalList()) {
			SignalKind kind = SignalList.getSignalFromList(
					Circuit.getSignalList(), (signal.getName()))
					.getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				firstOutputLine += signal.getName() + "\t";
			}
		}
		System.out.println(firstOutputLine);
		SignalList.logState(Circuit.getSignalList(), 0);

	}

	private void setInputEvents(File eventFile) {
		// DateiLeser fileReader = new DateiLeser(eventFile.getPath());
		//
		// while (fileReader.nochMehrZeilen()) {
		// String line = fileReader.gibZeile();
		// if (line.startsWith("#") || line.replaceAll("\\s","").equals("")) {
		// continue;
		// }
		// String [] eventArray = new String[3];
		// eventArray = line.split("\\s+");
		// Signal s = signals.get(eventArray[1]);
		// int startTime = Integer.parseInt(eventArray[0]);
		// boolean value = eventArray[2].equals("1") ? true: false;
		// new Event(s,startTime,value);
		// }
		new EventProvider(eventFile, Circuit.getSignalList());
	}

	private void buildCircuit(File file) {
		Circuit circuit = new Circuit(file);

		// signals = circuit.getSignals();
		// inputs = circuit.getInputs();
		// outputs = circuit.getOutputs();
		// gates = inputFile.getGates();
	}

	// private void findSteadyState() {
	// for (Signal inputSignal: inputs.values()) {
	// inputSignal.setValue(false);
	// }
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

	static public void main(String[] args) throws FileNotFoundException,
			URISyntaxException {

		String circuitFileName = "exampleFiles/beispiel1o.cir";
		String eventFileName = "exampleFiles/beispiel1o.events";
		File circuitFile = new File(ClassLoader.getSystemResource(
				circuitFileName).toURI());
		File eventFile = new File(ClassLoader.getSystemResource(eventFileName)
				.toURI());

		DateiSimulator t = new DateiSimulator(circuitFile, eventFile);

		t.simulate();

		ArrayList<Integer> times = new ArrayList<Integer>(SignalList.solutionMap.keySet());
		Collections.sort(times);
		for (int time : times) {
			String solution = time + "\t";
			for (String value : SignalList.solutionMap.get(time)) {
				solution += value + "\t";
			}
			System.out.println(solution);
		}

	}

}
package event;

import gatter.Gatter;

import java.util.ArrayList;

import main.Circuit;

/**
 * @author Lion Gienger August 2015
 *
 */
public class Signal {
	/**
	 * gatterList: Liste der Gatter, in welches dieses Signal muendet signaName:
	 * die ID des Signals signalValue: der Wert des Signals
	 */
	private static boolean logStateToSystemOut;
	private String signalName;
	private boolean signalValue;
	private ArrayList<Gatter> gatterList;
	private int setCounterPre = 0;
	private SignalKind signalKind;
	private boolean hasChanged;
	private boolean previousValue;

	/**
	 * Constructor for objects of class Signal
	 *
	 * setzt den Namen des Signals - initialisiert die Gatterliste
	 * 
	 * @param name
	 *            : name of the signal
	 */
	public Signal(String name) {
		setName(name);
		gatterList = new ArrayList<Gatter>();
	}

	/**
	 * @return der Wert des Signals
	 */
	public boolean getValue() {
		return signalValue;
	}

	/**
	 * @return der Name des Signals
	 */
	public String getName() {
		return signalName;
	}

	/**
	 * @param kind
	 *            Setzt den Namen des Signals
	 */
	public void setName(String signalName) {
		this.signalName = signalName;
	}

	/**
	 * Schaltungsaufbau: Diese Methode wird aus dem Gatter (Nand Klasse) beim
	 * Aufbau der Schaltung gerufen. Sie fuegt das jeweilige Gatter der
	 * Gatterlist hinzu.
	 * 
	 * @param gatter
	 *            : Das Gatter das zur Gatterliste zugefuegt wird.
	 */
	public void connectTo(Gatter gatter) {
		gatterList.add(gatter);
	}

	/**
	 * An dieser Stelle wird der Wert des Signals geaendert. In einer Schleife
	 * ueber die angeschlossenen Gatter wird dann jeweils pro Gatter das
	 * Ausgabesignal berechnet. Die Aenderung des Ausgangssignals in den
	 * angeschlossenen Gattern wiederun triggert die weitere Berechnungen
	 * 
	 * @param value
	 */
	public void setValue(boolean value) {
		signalValue = value;

		if ((Event.getEventQueue().getRunTime() == 0) && setCounterPre < 10) {
			setCounterPre++;
			for (Gatter gatter : gatterList) {
				gatter.getClass();
				gatter.setOutputValue();
			}
		}
		int time = Event.getEventQueue().getRunTime();
		if (time > 0) {
			SignalKind kind = SignalList.getSignalFromList(Circuit.getSignalList(),
					this.getName()).getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				SignalList.logState(Circuit.getSignalList(), time);
			}
		}
	}

	// /**
	// * Prueft ob das Signal nQ oder Q ist, wenn ja werden die Werte
	// geschrieben
	// */
	// private void logState(int time) {
	// SignalKind kind = SignalList.getSignalFromList(Circuit.getSignalList(),
	// (this.getName())).getSignalKind();
	// if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
	// String solution = time + "\t";
	// for (Signal signal : Circuit.getSignalList()) {
	// kind = SignalList.getSignalFromList(Circuit.getSignalList(),
	// (signal.getName())).getSignalKind();
	// if (kind.equals(SignalKind.INPUT)
	// || kind.equals(SignalKind.OUTPUT)) {
	// solution += Integer.toString(signal.getValue() == true ? 1
	// : 0) + "\t";
	// }
	// }
	// System.out.println(solution);
	// }
	// if (isOutPutSignal()) {
	// String solutionLine = time + ": " + this.getName() + " = "
	// + Integer.toString(this.getValue() == true ? 1 : 0);
	// FullTimingSimulator.addCalculatedSolution(solutionLine);
	// if (logStateToSystemOut) {
	// System.out.println(solutionLine);
	// }
	// }
	// }

	private boolean isOutPutSignal() {
		boolean isKnownOutput = (this.getName().startsWith("Out")
				|| this.getName().startsWith("memOut") || this.getName()
				.startsWith("CntAddr")) && !this.getName().equals("memOut3");
		return (isKnownOutput);
		// return (isKnownOutput ||
		// DateiSimulator.outputs.containsKey(this.getName()));
	}

	public static void setLogStateToSystemOut(boolean logStateToSystemOut) {
		Signal.logStateToSystemOut = logStateToSystemOut;
	}

	public ArrayList<Gatter> getGatterList() {
		return gatterList;
	}

	public SignalKind getSignalKind() {
		return signalKind;
	}

	public void setSignalKind(SignalKind signalKind) {
		this.signalKind = signalKind;
	}
}

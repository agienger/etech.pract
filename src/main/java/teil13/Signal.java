package teil13;

import gatter.Gatter;

import java.util.ArrayList;

import main.FullTimingSimulator;
import event.Event;

/**
 * @author Lion Gienger August 2015
 *
 */
public class Signal {
	/**
	 * gatterList: Liste der Gatter, in welches dieses Signal muendet signaName:
	 * die ID des Signals signalValue: der Wert des Signals
	 */
	private String signalName;
	private boolean signalValue;
	private ArrayList<Gatter> gatterList;
	private int setCounterPre = 0;

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
	 * @param name
	 *            Setzt den Namen des Signals
	 */
	public void setName(String name) {
		this.signalName = name;
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
			logSolutions(time);
		}
	}

	/**
	 * Prueft ob das Signal nQ oder Q ist, wenn ja werden die Werte geschrieben
	 */
	private void logSolutions(int time) {
		if ((this.getName().startsWith("Out")
				|| this.getName().startsWith("memOut") || this.getName()
				.startsWith("CntAddr")) && !this.getName().equals("memOut3")) {
			String solutionLine = time + ": " + this.getName() + " = "
					+ Integer.toString(this.getValue() == true ? 1 : 0);
			FullTimingSimulator.addCalculatedSolution(solutionLine);
		}
	}
}

package circuit;

import gatter.Gatter;

import java.util.ArrayList;

import simulator.DateiSimulator;

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
	private SignalKind signalKind;
	

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
	
	public Signal(String name , SignalKind kind){
		this(name);
		setSignalKind(kind);
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
	 * @param signalName Name des Signals
	 * <br>Setzt den Namen des Signals
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
	 * @param value Wert des Signals
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
			SignalKind kind = Signal.getSignalFromList(Circuit.getSignalList(),this.getName()).getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				DateiSimulator.logCurrentState(time);
			}
		}
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

	public static Signal getSignalFromList(ArrayList<Signal> signalList, String sigName) {
		for (Signal signal : signalList) {
			if (signal.getName().equals(sigName)) {
				return signal;
			}
		}
		return null;
	}
	

	

	

	
}

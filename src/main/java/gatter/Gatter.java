package gatter;

import java.util.ArrayList;

import circuit.Event;
import circuit.Signal;

/**
 * Basisklasse Gatter aus der alle Gatter abgeleitet werden.
 * 
 * @author Lion Gienger
 */
public abstract class Gatter {

	/**
	 * Array der Inputsignalen
	 * dieses Gatters
	 */
	private Signal[] inputSignalArray;
	/**
	 * Das Ausgangssignal des Gatters
	 */
	private Signal output;

	/**
	 * Die Verzögerung des Gatters
	 */
	private int waitTime;
	
	/**
	 * Der zuletzt berechnete Output-Wert des Gatters
	 */
	protected boolean lastCalculatedValue = false;

	
	/**
	 * Statische Liste aller Gatter der Schaltung
	 */
	private static ArrayList<Gatter> globalGatterList = new ArrayList<Gatter>();

	/**
	 * Konstruktor der Klasse Gatter, initialisiert ein Signal Array mit {@code anzahl}
	 * Feldern und Wartezeit {@code wTime}.
	 * 
	 * @param anzahl Anzahl der Eingänge
	 * @param wTime Delay Time des Gatters
	 */
	public Gatter(int anzahl, int wTime) {
		inputSignalArray = new Signal[anzahl];
		waitTime = wTime;
		globalGatterList.add(this);
	}

	/**
	 * Beim Schaltungsaufbau wird hier das n-te Eingangssignal
	 * inputSignal definiert. Außerdem weist die Methode diesem Signal dieses Gatter als
	 * Ziel zu.
	 * 
	 * @param inputNummer Nummer des Eingangs
	 * @param inputSignal Input Signal
	 */
	public void setInput(int inputNummer, Signal inputSignal) {
		inputSignalArray[inputNummer] = inputSignal;
		inputSignal.connectTo(this);
	}

	/**
	 * Beim Schaltungsaufbau wird hier das Ausgangssignal signal gesetzt.
	 * 
	 * @param s Ausgangssignal
	 */
	public void setOutput(Signal s) {
		output = s;
	}

	/**
	 * Berechnet den Wert des Ausgangssignals. Wenn die Eventque noch nicht gestartet wurde (Einschwingphase),
	 * So wird nur dann ein neues Event erzeugt, wenn sich der Wett des Signals geändert hat.
	 * 
	 * 
	 */
	public void setOutputValue() {
		boolean calculatedValue = calculateOutputValue();

		if (!Event.getEventQueue().isStarted()) {
			output.setValue(calculatedValue);
			lastCalculatedValue = calculatedValue;
		} else {
			if (calculatedValue != lastCalculatedValue) {
				lastCalculatedValue = calculatedValue;
				new Event(output,
						Event.getEventQueue().getRunTime() + waitTime,
						calculatedValue);
			}
		}

	}

	/**
	 * @return Das Array der Eingsngssignalen.
	 */
	public Signal[] getInputSignalArray() {
		return inputSignalArray;
	}

	/**
	 * Abstrakte Methode der Gatter-spezifischen Berechnung das Ausgangssignal des Gatters.
	 * 
	 * @return the output value
	 */
	public abstract boolean calculateOutputValue();

	
	public boolean getLastCalculatedValue() {
		return lastCalculatedValue;
	}

	/**
	 * Diese Methode berechnet alle Gatter der Gatterliste neu und 
	 */
	public static void recalculate() {
		for (Gatter gatter : globalGatterList) {
			gatter.getClass();
			gatter.setOutputValue();
		}
	}

	public Signal getOutput() {
		return output;
	}

	public int getWaitTime() {
		return waitTime;
	}

}

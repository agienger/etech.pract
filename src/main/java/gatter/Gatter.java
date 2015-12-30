package gatter;

import java.util.ArrayList;

import event.Event;
import event.Signal;

/**
 * Basisklasse Gatter
 * 
 * @author Lion Gienger
 */
/**
 * @author Lion Gienger August 2015
 *
 */
public abstract class Gatter {

	/**
	 * inputSignalArray Ist ein arary von Signalen mit allen Inputsignales
	 * dieses Gatters
	 */
	private Signal[] inputSignalArray;
	/**
	 * output ist das Ausgangssignal des Gatters
	 */
	private Signal output;

	private int waitTime;
	private boolean lastCalculatedValue = false;

	public static ArrayList<Gatter> globalGatterList = new ArrayList<Gatter>();

	/**
	 * Konstruktor der Klasse Nand, initialisiert ein Signal Array mit anzahl
	 * Feldern und Wartezeit wTime
	 * 
	 * @param anzahl
	 */
	public Gatter(int anzahl, int wTime) {
		inputSignalArray = new Signal[anzahl];
		waitTime = wTime;
		globalGatterList.add(this);
	}

	/**
	 * Schaltungsaufbau: Definiert das inputNummer-te Eingangssignal
	 * inputSignal. Ausserdem weist die Methode diesem Signal dieses Gatter als
	 * Ziel zu.
	 * 
	 * @param inputNummer
	 * @param inputSignal
	 */
	public void setInput(int inputNummer, Signal inputSignal) {
		inputSignalArray[inputNummer] = inputSignal;
		inputSignal.connectTo(this);
	}

	/**
	 * Schaltungsaufbau: Setzt das Ausgangssignal signal
	 * 
	 * @param s
	 */
	public void setOutput(Signal s) {
		output = s;

	}

	/**
	 * Berechnet den Wert des Ausgangssignals durch Aufruf der Methode @ref
	 * calculateNand(). Schreibt die Werte der Endsignale s0 and s1
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

	public Signal[] getInputSignalArray() {
		return inputSignalArray;
	}

	/**
	 * Dies Methode berechnet das Ausgangssignal des Gatters. Wenn null zurück
	 * kommt, wird das output Signal nicht gesetzt
	 * 
	 * @return the output value
	 */
	public abstract boolean calculateOutputValue();

	public boolean getLastCalculatedValue() {
		return lastCalculatedValue;
	}

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

package teil13;

import java.util.ArrayList;

import main.CircuitFile;
import gatter.Gatter;

/**
 * Die Klasse Event stellt Events zur Verfügung, die automatisch in eine vorher
 * durch {@link #setEventQueue(EventQueue)} zu spezifizierende EventQueue
 * geladen werden.
 * 
 */
public class Event {
	private static EventQueue eventQueue;
	private Signal cSignal;
	private int cTime;
	private boolean cValue;

	public static ArrayList<ArrayList<String>> inAndOutPuts = new ArrayList<ArrayList<String>>();

	/**
	 * Erzeugt ein neues Event und fügt es in die EventQueue an der richtigen
	 * Stelle ein.
	 * 
	 * @param signal
	 *            Signal, das geändert werden soll
	 * @param time
	 *            Zeitpunkt zu dem die Änderung geschehen soll
	 * @param value
	 *            neuer Wert auf den das Signal zum angegebenen Zeitpunkt
	 *            geändert wird
	 */
	public Event(Signal signal, int time, boolean value) {
		cSignal = signal;
		cTime = time;
		cValue = value;
		eventQueue.addEvent(this);
	}

	/**
	 * Diese Methode setzt die EventQueue, die später von allen Events genutzt
	 * werden soll.
	 * 
	 * @param e
	 *            EventQueue
	 */
	public static void setEventQueue(EventQueue e) {
		eventQueue = e;
	}

	/**
	 * Diese Methode gibt die EventQueue zurück.
	 * 
	 * @return EventQueue
	 */
	public static EventQueue getEventQueue() {
		return eventQueue;
	}

	/**
	 * Die Methode propagate() führt das Event aus. Dabei wird das Event aus der
	 * EventQueue gelöscht und das eventbetreffende Signal geändert.
	 */
	public void propagate() {
		if (!eventQueue.isStarted()) {
			eventQueue.start();
		}
		eventQueue.remove(this);
		eventQueue.setRunTime(cTime);
		cSignal.setValue(cValue);
		if (eventQueue.getListSize() == 0
				|| cTime < eventQueue.getFirst().getStartTime()) {
//			ArrayList<String> resultRow = new ArrayList<String>();
//			resultRow.add(Integer.toString(cTime));
//			for (String sig : inAndOutPuts.get(0)) {
//				resultRow
//						.add(CircuitFile.signals.get(sig).getValue() == false ? "0"
//								: "1");
//			}
//			inAndOutPuts.add(resultRow);
			Gatter.recalculate();
		}
	}

	/**
	 * Diese Methode gibt den Zeitpunkt, an dem das Event ausgeführt werden soll
	 * zurück.
	 * 
	 * @return Zeitpunkt
	 */
	public int getStartTime() {
		return cTime;
	}

	/**
	 * Diese Methode gibt das Signal, das geändert werden soll zurück.
	 * 
	 * @return Signal
	 */
	public Signal getSignal() {
		return cSignal;
	}

	/**
	 * Diese Methode gibt den Wert zurück auf den dieses Event betreffende
	 * Signal geändert werden soll.
	 * 
	 * @return Wert des Signals
	 */
	public boolean getValue() {
		return cValue;
	}

	public String toString() {
		return ("Signal " + cSignal.getName() + " wird zum Zeitpunkt " + cTime
				+ " auf " + cValue + " geändert");
	}

	public boolean isLater(Event e) {
		return (e.getStartTime() < this.cTime);
	}

	public boolean willBeOverwritten(Event e) {
		return (this.cSignal.getName().equals(e.getSignal().getName()) && this.cTime == e
				.getStartTime());
	}
}

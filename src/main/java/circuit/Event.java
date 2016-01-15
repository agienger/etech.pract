package circuit;

import gatter.Gatter;

import java.util.ArrayList;

/**
 * @author Lion Gienger
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
	 * werden.
	 * 
	 * @param e Die EventQueue
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
	 * EventQueue gelöscht und die Queue-Laufzeit aktualisiert, sowie der Status des Signals geändert.
	 * Danach werden alle Gatter neu berechnet, wenn entweder die EventQueue leer ist, oder der Zeitpunkt
	 * des Events vor dem ersten ersten Event der Queue liegt.
	 */
	public void propagate() {
		if (!eventQueue.isStarted()) {
			eventQueue.start();
		}
		eventQueue.remove(this);
		eventQueue.setRunTime(cTime);
		cSignal.setValue(cValue);
		if (eventQueue.getEventListe().size() == 0
				|| cTime < eventQueue.getFirst().getEventTime()) {
			Gatter.recalculate();
		}
	}

	/**
	 * Diese Methode gibt den Zeitpunkt, an dem das Event ausgeführt werden soll
	 * zurück.
	 * 
	 * @return Teitpunkt des Events
	 */
	public int getEventTime() {
		return cTime;
	}

	/**
	 * Diese Methode gibt das zu ändernde Signal zurück.
	 * 
	 * @return Signal
	 */
	public Signal getSignal() {
		return cSignal;
	}

	/**
	 * Diese Methode gibt den Wert zurück auf den dieses Event betreffende
	 * Signal geändert wird.
	 * 
	 * @return Wert des Signals
	 */
	public boolean getValue() {
		return cValue;
	}

	/**
	 * Berchnet, ob das zu vergleichende Event früher ist als das aktuelle
	 * @param e Event
	 * @return Is früher?
	 */
	public boolean isEarlier(Event e) {
		return (e.getEventTime() < this.cTime);
	}

	/**
	 * Prüft, ob das aktuell Event mit dem Event e überschrieben werden soll, . Dies ist der Fall, 
	 * wenn beide Events, das selbe Signal betreffen und die Zeiten gleich sind
	 * @param e Event
	 * @return Soll überschreiben werden?
	 */
	public boolean willBeOverwritten(Event e) {
		return (this.cSignal.getName().equals(e.getSignal().getName()) && this.cTime == e
				.getEventTime());
	}
}

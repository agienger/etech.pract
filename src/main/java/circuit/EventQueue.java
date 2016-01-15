package circuit;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Die Klasse EventQueue stellt eine Liste dar, die alle Events enthält. <br>
 * Außerdem verwaltet sie eine statische Variable eventTime, die den Zeitablauf
 * der Schaltung darstellt.
 * 
 * @author Lion Gienger
 * @version 1.2 17.06.06
 */
/**
 * @author Armin
 *
 */
/**
 * @author Armin
 *
 */
/**
 * @author Armin
 *
 */
/**
 * @author Armin
 *
 */
/**
 * @author Armin
 *
 */
/**
 * @author Armin
 *
 */
/**
 * @author Armin
 *
 */
public class EventQueue {
	/**
	 * List der Events
	 */
	private LinkedList<Event> eventListe;
	/**
	 * Momentane Laufzeit der Queue
	 */
	private int runTime;

	/**
	 * Gibt an, ob die EventQueue bereits gestartet ist.
	 */
	private boolean isStarted = false;

	/**
	 * Im Konstruktor der Klasse EventQueue werden die Laufzeit und die Event
	 * Liste initialisiert.
	 * 
	 */
	public EventQueue() {
		runTime = 0;
		eventListe = new LinkedList<Event>();
	}

	/**
	 * Setzt die Laufzeit der Queue.
	 * 
	 * @param time
	 *            Die zu setzende Laufzeit der Queue
	 */
	public void setRunTime(int time) throws RuntimeException {
		runTime = time;
	}

	/**
	 * Gibt die aktuelle Laufzeit der Queue zurück.
	 * 
	 * @return Laufzeit der Queue
	 */
	public int getRunTime() {
		return runTime;
	}

	/**
	 * Sortiert ein Event in die EventQueue ein <br>
	 * Neue Events werden dabei hinter andere Events mit gleicher Zeit
	 * einsortiert. Außerdem werden Events, die zur gleichen Zeit dasselbe
	 * Signal betreffen überschrieben.
	 * 
	 * @param e
	 *            das Event, das neu einsortiert werden soll.
	 */
	public void addEvent(Event e) {
		ListIterator<Event> iterator = eventListe.listIterator();
		while (true) {
			if (!iterator.hasNext()) {
				eventListe.add(e);
				return;
			}
			Event checkedEvent = iterator.next();
			if (checkedEvent.willBeOverwritten(e)) {
				iterator.add(e);
				iterator.previous();
				eventListe.remove(checkedEvent);
				return;
			}
			if (checkedEvent.isEarlier(e)) {
				iterator.previous();
				iterator.add(e);
				return;
			}
		}
	}

	/**
	 * Die Methode prüft, ob in der EventQueue noch Elemente enthalten sind.
	 * 
	 * @return true, wenn noch Elemente in der Liste stehen; ansonsten false.
	 */
	public boolean hasMore() {
		return !eventListe.isEmpty();
	}

	/**
	 * Die Methode gibt das erste {@link circuit.Event} der EventQueue zurück.
	 * 
	 * @return erstes Event
	 */
	public Event getFirst() {
		return (Event) eventListe.getFirst();
	}

	/**
	 * Prüft, ob die EventQueue schon gestartet wurde. <br>
	 * 
	 * @return ist Eventqueue gestartet?
	 */
	public boolean isStarted() {
		return isStarted;
	}

	public String toString() {
		String output = "";
		ListIterator<Event> iterator = eventListe.listIterator();
		while (iterator.hasNext()) {
			Event e = iterator.next();
			output += e.getEventTime() + ":" + e.getSignal().getName() + ":"
					+ e.getSignal().getValue() + ", ";
		}
		return output;
	}

	/**
	 * Startet die EventQueue indem {@code isStarted} auf {@code true} gesetzt
	 * wird.
	 */
	public void start() {
		isStarted = true;
	}

	// public int getListSize() {
	// return eventListe.size();
	// }

	/**
	 * Löscht das Event aus der Liste
	 * 
	 * @param event Event
	 */
	public void remove(Event event) {
		eventListe.remove(event);
	}

	/**
	 * @return Die EventListe
	 */
	public LinkedList<Event> getEventListe() {
		return eventListe;
	}

}

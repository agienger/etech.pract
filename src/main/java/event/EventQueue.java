package event;

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
public class EventQueue {
	public LinkedList<Event> eventListe;
	private  int runTime;
	private boolean isStarted = false;

	/**
	 * Der Konstruktor der Klasse EventQueue.
	 * 
	 */
	public EventQueue() {
		runTime = 0;
		eventListe = new LinkedList<Event>();
	}

	/**
	 * Die Methode setRunTime() setzt die zentrale Zeitvariable. <br>
	 * 
	 * @param nTime
	 *            neue Zeit als Integer
	 */
	public void setRunTime(int nTime) throws RuntimeException {
		runTime = nTime;
	}

	/**
	 * Die Methode addEvent() sortiert ein Event in die EventQueue ein <br>
	 * Neue Events werden dabei hinter andere Events mit gleicher Zeit
	 * einsortiert. Außerdem werden Events, die zur gleichen Zeit dasselbe
	 * Signal betreffen überschrieben.
	 * 
	 * @param e
	 *            das einzusortierende Event
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
			if (checkedEvent.isLater(e)) {
				iterator.previous();
				iterator.add(e);
				return;
			}
		}
	}

	/**
	 * Die Methode hasMore() prüft, ob in der EventQueue noch Elemente enthalten
	 * sind.
	 * 
	 * @return true, wenn noch Elemente in der Liste stehen; ansonsten false.
	 */
	public boolean hasMore() {
		return !eventListe.isEmpty();
	}

	/**
	 * Die Methode getFirst() gibt das erste Event der EventQueue als
	 * {@link Event} zurück.
	 * 
	 * @return erster Event
	 */
	public Event getFirst() {
		return (Event) eventListe.getFirst();
	}

	/**
	 * Die Methode getRunTime() gibt die aktuelle Zeit der Schaltung zurück.
	 * 
	 * @return zentrale Schaltungszeit als Integer
	 */
	public  int getRunTime() {
		return runTime;
	}

	/**
	 * Die Methode isStarted() prüft, ob die EventQueue schon gestartet wurde. <br>
	 * Die EventQueue wird durch {@link #start()} in {@link Event#propagate()}
	 * gestartet.
	 * 
	 * @return true, wenn Initialisierungsphase abgeschlossen; ansonsten false
	 */
	public boolean isStarted() {
		return isStarted;
	}

	public String toString() {
		String output = "";
		ListIterator<Event> iterator = eventListe.listIterator();
		while (iterator.hasNext()) {
			Event e = iterator.next();
			output += e.getStartTime() + ":" + e.getSignal().getName() + ":"
					+ e.getSignal().getValue() + ", ";
		}
		return output;
	}

	/**
	 * Die Methode start() startet die EventQueue. <br>
	 */
	public void start() {
		isStarted = true;
	}

	public int getListSize() {
		return eventListe.size();
	}

	public void remove(Event event) {
		eventListe.remove(event);
		
	}

}

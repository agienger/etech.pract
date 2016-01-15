package circuit;

import java.io.File;
import java.util.ArrayList;

import simulator.DateiLeser;

/**
 * @author Lion Gienger
 * Erzeugt eine EventQueu, die aus dem Eventfile gelesen wird.
 */
public class EventProvider {

	public EventProvider(File eventFile, ArrayList<Signal> signalList) {
		DateiLeser fileReader = new DateiLeser(eventFile.getPath());

		while (fileReader.nochMehrZeilen()) {
			String line = fileReader.gibZeile();
			if (line.startsWith("#") || line.replaceAll("\\s", "").equals("")) {
				continue;
			} else {
				String[] eventData = line.split("\\s+");
				new Event(Signal.getSignalFromList(signalList, eventData[1]),
						Integer.parseInt(eventData[0]), toBoolean(eventData[2]));
			}
		}
	}

	public EventQueue getEventqueue() {
		return Event.getEventQueue();
	}

	/**
	 * Wandet Strin nach boolean um ({@code 1 = true, 0 = false})
	 * @param value Wert des Signals als String
	 * @return Wert als booelan
	 */
	private boolean toBoolean(String value) {
		if (value.equals("1")) {
			return true;
		}
		return false;
	}

}

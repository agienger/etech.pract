package main;

import java.io.File;
import java.util.ArrayList;

import event.Event;
import event.EventQueue;
import event.Signal;
import event.SignalList;
import file.DateiLeser;

public class EventProvider {

	public EventProvider(File eventFile,  ArrayList<Signal> signalList) {
		DateiLeser	fileReader = new DateiLeser(eventFile.getPath());

		while (fileReader.nochMehrZeilen()) {
			String line = fileReader.gibZeile();
			if (line.startsWith("#") || line.replaceAll("\\s", "").equals("")) {
				continue;
			} else {
				String[] eventData = line.split("\\s+");
				new Event(SignalList.getSignalFromList(signalList, eventData[1]), Integer.parseInt(eventData[0]),
						toBoolean(eventData[2]));
			}
		}
	}
	
	public EventQueue getEventqueue() {
		return Event.getEventQueue();
	}

	private boolean toBoolean(String value) {
		if (value.equals("1")) {
			return true;
		}
		return false;
	}


}
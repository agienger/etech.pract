package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import main.EventProvider;

import org.junit.Test;

import event.Event;
import event.EventQueue;
import event.Signal;

public class EventProviderTest {

	@Test
	public void test() throws URISyntaxException {
		File eventFile = new File(ClassLoader.getSystemResource(
				"exampleFiles/beispiel1o.events").toURI());
		ArrayList<Signal> signalList = new ArrayList<Signal>();
		signalList.add(new Signal("a"));
		signalList.add(new Signal("b"));
		signalList.add(new Signal("cin"));
		EventQueue queue = new EventQueue();
		Event.setEventQueue(queue);
		EventProvider ep = new EventProvider(eventFile, signalList);
		assertEquals(12, ep.getEventqueue().getListSize());
	}

}

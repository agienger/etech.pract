package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;

import main.EventProvider;

import org.junit.Test;

import event.Event;
import event.EventQueue;
import event.Signal;

public class EventProviderTest {

	@Test
	public void test() throws URISyntaxException {
		File eventFile = new File(ClassLoader.getSystemResource(
				"events/beispiel1o.events").toURI());
		new Signal("a");
		new Signal("b");
		new Signal("cin");
		EventQueue queue = new EventQueue();
		Event.setEventQueue(queue);
		EventProvider ep = new EventProvider(eventFile);
		assertEquals(12, ep.getEventqueue().getListSize());
	}

}

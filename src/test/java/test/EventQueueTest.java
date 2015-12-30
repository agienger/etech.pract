package test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

import event.Event;
import event.EventQueue;
import event.Signal;

public class EventQueueTest {

	private Signal s1 = new Signal("s1");;
	private Signal s2 = new Signal("s2");;
	private Signal s3 = new Signal("s3");;

	private LinkedList<Event> eventListe;

	@Before
	public void setUp() throws Exception {
		EventQueue queue;
		queue = new EventQueue();
		Event.setEventQueue(queue);
		eventListe = queue.eventListe;
	}

	@Test
	public void testFuegeEinElementInLeereListeEin() {
		new Event(s2, 10, true);
		
		ListIterator<Event> iterator = eventListe.listIterator();

		Event e = iterator.next();
		assertEquals("s2", e.getSignal().getName());
		assertEquals(1, eventListe.size());
	}
	
	@Test
	public void testFuege3ElementeEin() {
		new Event(s1, 10, true);
		new Event(s2, 20, true);
		new Event(s3, 30, true);
		
		ListIterator<Event> iterator = eventListe.listIterator();

		Event e = iterator.next();
		assertEquals("s1", e.getSignal().getName());
		
		e = iterator.next();
		assertEquals("s2", e.getSignal().getName());
		e = iterator.next();
		assertEquals("s3", e.getSignal().getName());
		assertEquals(3, eventListe.size());
	}
	
	@Test
	public void testFuege3ElementeGemischtEin() {
		new Event(s3, 30, true);
		new Event(s1, 10, true);
		new Event(s2, 20, false);
		
		
		ListIterator<Event> iterator = eventListe.listIterator();

		Event e = iterator.next();
		assertEquals("s1", e.getSignal().getName());
		e = iterator.next();
		assertEquals("s2", e.getSignal().getName());
		e = iterator.next();
		assertEquals("s3", e.getSignal().getName());
		assertEquals(3, eventListe.size());
	}
	
	@Test
	public void testFuege3ElementeEinMitGleicherZeit() {
		new Event(s3, 5, true);
		new Event(s1, 10, true);
		new Event(s2, 5, false);
		
		
		ListIterator<Event> iterator = eventListe.listIterator();

		Event e = iterator.next();
		assertEquals("s3", e.getSignal().getName());
		e = iterator.next();
		assertEquals("s2", e.getSignal().getName());
		e = iterator.next();
		assertEquals("s1", e.getSignal().getName());
		assertEquals(3, eventListe.size());
	}
	
	@Test
	public void testUeberschreibeEinEvent() {
		new Event(s3, 30, true);
		new Event(s1, 10, true);
		new Event(s1, 10, false);
		
		
		ListIterator<Event> iterator = eventListe.listIterator();

		Event e = iterator.next();
		assertEquals("s1", e.getSignal().getName());
		assertEquals(false, e.getValue());
		
		e = iterator.next();
		assertEquals("s3", e.getSignal().getName());
		assertEquals(2, eventListe.size());
		
	}
	
	@Test
	public void testUeberschreibeEinEvent2() {
		new Event(s1, 10, true);
		new Event(s1, 10, false);
		
		
		ListIterator<Event> iterator = eventListe.listIterator();

		Event e = iterator.next();
		assertEquals("s1", e.getSignal().getName());
		assertEquals(false, e.getValue());
		
		assertEquals(1, eventListe.size());
		
	}

}

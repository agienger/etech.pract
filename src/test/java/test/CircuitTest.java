package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import circuit.Circuit;
import circuit.Signal;

public class CircuitTest {
	ArrayList<Signal> signalList;
	Circuit circuit;
	
	@Before
	public void setUp() {
		signalList = new ArrayList<Signal>();
		Circuit.setSignalList(signalList);
	}
	
	@Test
	public void testBeispiel1o() throws URISyntaxException {
		File circuitFile = new File(ClassLoader.getSystemResource(
				"circuits/beispiel1o.cir").toURI());
		circuit = new Circuit(circuitFile);
		signalList = Circuit.getSignalList();
		assertEquals(8, signalList.size());
		Signal testSignal = signalList.get(0);
		assertEquals("a", testSignal.getName());
		assertEquals(false, testSignal.getValue());
		assertEquals(2, testSignal.getGatterList().size());
		assertEquals("i1", testSignal.getGatterList().get(0).getOutput()
				.getName());
	}
	
	@Test
	public void testLatch() throws URISyntaxException {
		File circuitFile = new File(ClassLoader.getSystemResource(
				"circuits/beispiel-latch.cir").toURI());
		circuit = new Circuit(circuitFile);
		signalList = Circuit.getSignalList();
		assertEquals(6, signalList.size());
		Signal testSignal = signalList.get(3);
		assertEquals("i1", testSignal.getName());
		assertEquals(false, testSignal.getValue());
		assertEquals(2, testSignal.getGatterList().size());
		assertEquals("i2", testSignal.getGatterList().get(0).getOutput()
				.getName());
	}
	
	@Test
	public void testBlume() throws URISyntaxException {
		File circuitFile = new File(ClassLoader.getSystemResource(
				"circuits/_blume.cir").toURI());
		circuit = new Circuit(circuitFile);
		signalList = Circuit.getSignalList();
		assertEquals(21, signalList.size());
		Signal testSignal = signalList.get(0);
		assertEquals("warm", testSignal.getName());
		assertEquals(false, testSignal.getValue());
		assertEquals(2, testSignal.getGatterList().size());
		assertEquals("s1", testSignal.getGatterList().get(0).getOutput()
				.getName());
	}

	@Test
	public void testFlipFlop() throws URISyntaxException {
		File circuitFile = new File(ClassLoader.getSystemResource(
				"circuits/beispiel-flipflop.cir").toURI());
		circuit = new Circuit(circuitFile);
		signalList = Circuit.getSignalList();
		assertEquals(19, signalList.size());
		Signal testSignal = signalList.get(0);
		assertEquals("a", testSignal.getName());
		assertEquals(false, testSignal.getValue());
		assertEquals(3,testSignal.getGatterList().size());
		assertEquals("s0", testSignal.getGatterList().get(0).getOutput()
				.getName());
	}



}

package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import main.Circuit;

import org.junit.Test;

import event.Signal;

public class CircuitTest {

	@Test
	public void test() throws URISyntaxException {
		File circuitFile = new File(ClassLoader.getSystemResource(
				"circuits/beispiel1o.cir").toURI());
		new Circuit(circuitFile);
		ArrayList<Signal> signalList = Circuit.getSignalList();
		assertEquals(8, signalList.size());
		Signal testSignal = signalList.get(0);
		assertEquals("a", testSignal.getName());
		assertEquals(false, testSignal.getValue());
		assertEquals(2, testSignal.getGatterList().size());
		assertEquals("i1", testSignal.getGatterList().get(0).getOutput()
				.getName());
	}

}

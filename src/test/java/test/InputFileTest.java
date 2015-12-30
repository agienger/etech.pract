package test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import main.CircuitOld;

import org.junit.Test;

public class InputFileTest {

	@Test
	public void testCircuit() throws URISyntaxException {
		
		String circuitFileName = "exampleFiles/beispiel1o.cir";
		File circuitFile= new File(ClassLoader.getSystemResource(circuitFileName).toURI());
		
		CircuitOld cirquit = new CircuitOld(circuitFile);
		List<String> expectedInputs = Arrays.asList("a", "b", "cin");
		List<String> expectedOutputs = Arrays.asList("s", "cout");
		List<String> expectedSignals = Arrays.asList("i1", "i2", "i3");
//		for (int i = 0; i < expectedInputs.size(); i++) {
//			assertTrue(cirquit.getInputs().containsKey(expectedInputs.get(i)));
//		}
//		for (int i = 0; i < expectedOutputs.size(); i++) {
//			assertTrue(cirquit.getOutputs().containsKey(expectedOutputs.get(i)));
//		}
		for (int i = 0; i < expectedSignals.size(); i++) {
			assertTrue(cirquit.getSignals().containsKey(expectedSignals.get(i)));
		}
		//assertTrue(cirquit.getGates().containsKey("g1"));
//		Gatter gate = cirquit.getGates().get("g1");
//		assertEquals(4,  gate.getWaitTime());
//		assertEquals(2,  gate.getInputSignalArray().length);
//		AND2  Delay 4;, actual);
	}
	
	@Test
	public void testEvent() throws URISyntaxException {
		String eventFileName = "exampleFiles/beispiel1o.events";
		File eventFile= new File(ClassLoader.getSystemResource(eventFileName).toURI());
		
		List<String> expectedInputs = Arrays.asList("a", "b", "cin");
		List<String> expectedOutputs = Arrays.asList("s", "cout");
		List<String> expectedSignals = Arrays.asList("i1", "i2", "i3");
//		for (int i = 0; i < expectedInputs.size(); i++) {
//			assertTrue(cirquit.getInputs().containsKey(expectedInputs.get(i)));
//		}
//		for (int i = 0; i < expectedOutputs.size(); i++) {
//			assertTrue(cirquit.getOutputs().containsKey(expectedOutputs.get(i)));
//		}
//		for (int i = 0; i < expectedSignals.size(); i++) {
//			assertTrue(cirquit.getSignals().containsKey(expectedSignals.get(i)));
//		}
		//assertTrue(cirquit.getGates().containsKey("g1"));
//		Gatter gate = cirquit.getGates().get("g1");
//		assertEquals(4,  gate.getWaitTime());
//		assertEquals(2,  gate.getInputSignalArray().length);
//		AND2  Delay 4;, actual);
	}

}

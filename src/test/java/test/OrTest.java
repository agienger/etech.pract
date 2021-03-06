package test;

import static org.junit.Assert.assertEquals;
import gatter.Or;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import circuit.Event;
import circuit.EventQueue;
import circuit.Signal;

@RunWith(Parameterized.class)
public class OrTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
				{ true, true, true }, { true, false, true },{ false, true, true }, { false, false, false } 
				
		});
	}

	Signal s1 = new Signal("s1");
	Signal s2 = new Signal("s2");
	Signal s3 = new Signal("s3");

	private boolean s1Value;
	private boolean s2Value;
	private boolean expectedOutputValue;
	
	public OrTest(boolean s1V, boolean s2V, boolean expectedV) {
		s1Value= s1V;
		s2Value= s2V;
		expectedOutputValue= expectedV;
    }

	@Test
	public void norTest() {
		EventQueue e= new EventQueue();
		Event.setEventQueue(e);
		Or or = new Or(2, 0);
		s1.setValue(s1Value);
		s2.setValue(s2Value);
		or.setInput(0, s1);
		or.setInput(1, s2);
		assertEquals(expectedOutputValue, or.calculateOutputValue());
	}

}

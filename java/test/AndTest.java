package test;

import static org.junit.Assert.assertEquals;
import gatter.And;

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
public class AndTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { true, true, true },
				{ true, false, false }, { false, true, false },
				{ false, false, false }

		});
	}

	Signal s1 = new Signal("s1");
	Signal s2 = new Signal("s2");
	Signal s3 = new Signal("s3");

	private boolean s1Value;
	private boolean s2Value;
	private boolean expectedOutputValue;

	public AndTest(boolean s1V, boolean s2V, boolean expectedV) {
		s1Value = s1V;
		s2Value = s2V;
		expectedOutputValue = expectedV;
	}

	@Test
	public void test() {
		EventQueue e= new EventQueue();
		Event.setEventQueue(e);
		And and = new And(2, 1);
		s1.setValue(s1Value);
		s2.setValue(s2Value);
		and.setInput(0, s1);
		and.setInput(1, s2);
		assertEquals(expectedOutputValue, and.calculateOutputValue());
	}

}

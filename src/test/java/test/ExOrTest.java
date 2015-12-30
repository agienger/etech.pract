package test;

import static org.junit.Assert.assertEquals;
import event.Event;
import event.EventQueue;
import event.Signal;
import gatter.Exor;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExOrTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
				{ true, true, false, false }, { true, false, false, true },{ true, true, true , true}, { false, false, false, false } 
		});
	}

	Signal s1 = new Signal("s1");
	Signal s2 = new Signal("s2");
	Signal s3 = new Signal("s3");

	private boolean s1Value;
	private boolean s2Value;
	private boolean s3Value;
	private boolean expectedOutputValue;
	
	public ExOrTest(boolean s1V, boolean s2V, boolean s3V, boolean expectedV) {
		s1Value= s1V;
		s2Value= s2V;
		s3Value= s3V;
		expectedOutputValue= expectedV;
    }

	@Test
	public void exorTest() {
		EventQueue e= new EventQueue();
		Event.setEventQueue(e);
		Exor exor = new Exor(3, 0);
		s1.setValue(s1Value);
		s2.setValue(s2Value);
		s3.setValue(s3Value);
		exor.setInput(0, s1);
		exor.setInput(1, s2);
		exor.setInput(2, s3);
		assertEquals(expectedOutputValue, exor.calculateOutputValue());
	}

}

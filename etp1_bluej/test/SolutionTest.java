package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import verify.Result;
import circuit.CircuitState;

public class SolutionTest {
	
	@Before
	public void setUp() {
		Result.clear();
	}

	@Test
	public void addStatetoEmptyList() {
		ArrayList<String> stat1 = new ArrayList<String>();
		stat1.add("0");stat1.add("1");stat1.add("0");
		CircuitState circ1 = new CircuitState(10, stat1);
		Result.addState(circ1);
		assertEquals(1,Result.results.size());
	}
	
	@Test
	public void addSameStatetoList() {
		ArrayList<String> stat1 = new ArrayList<String>();
		stat1.add("0");stat1.add("1");stat1.add("0");
		CircuitState circ1 = new CircuitState(10, stat1);
		CircuitState circ2 = new CircuitState(20, stat1);
		Result.addState(circ1);
		Result.addState(circ2);

		assertEquals(1,Result.results.size());
	}
	
	@Test
	public void addDifferentStatetoList() {
		ArrayList<String> stat1 = new ArrayList<String>();
		stat1.add("0");stat1.add("1");stat1.add("0");
		ArrayList<String> stat2 = new ArrayList<String>();
		stat2.add("0");stat2.add("1");stat2.add("1");
		CircuitState circ1 = new CircuitState(10, stat1);
		CircuitState circ2 = new CircuitState(20, stat2);
		Result.addState(circ1);
		Result.addState(circ2);

		assertEquals(2,Result.results.size());
	}

}
